package io.vaxly.sema;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.badoo.barf.data.repo.DelegatingRepository;
import com.badoo.barf.data.repo.Repository;
import com.badoo.barf.data.repo.annotations.HandlesUtil;
import io.vaxly.sema.data.model.ExampleConversation;
import io.vaxly.sema.data.model.ExampleMessage;
import io.vaxly.sema.data.model.ExampleUser;
import io.vaxly.sema.data.repos.conversations.ParseConversationDataSource;
import io.vaxly.sema.data.repos.istyping.ParseIsTypingDataSource;
import io.vaxly.sema.data.repos.messages.ExampleMessageMemoryDataSource;
import io.vaxly.sema.data.repos.messages.ImageUploadService;
import io.vaxly.sema.data.repos.messages.ParseMessageDataSource;
import io.vaxly.sema.data.repos.session.ParseSessionDataSource;
import io.vaxly.sema.data.repos.user.ParseUserDataSource;
import io.vaxly.sema.data.util.ParseHelper;
import io.vaxly.sema.ui.ExampleConfiguration;
import io.vaxly.sema.ui.Injector;
import io.vaxly.sema.ui.chat.ChatActivity;
import io.vaxly.sema.ui.conversations.create.namegroup.NameGroupActivity;
import io.vaxly.sema.ui.conversations.create.selectusers.SelectUserActivity;
import io.vaxly.sema.ui.conversations.list.ConversationListActivity;
import io.vaxly.sema.ui.session.login.LoginActivity;
import io.vaxly.sema.ui.session.register.RegisterActivity;
import io.vaxly.sema.ui.util.BackgroundListenerRegistrar;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import net.hockeyapp.android.CrashManager;

public class ChatExampleApp extends Application {

    private static final int SOCKET_PORT = 9000;
    private static final String HOCKEY_APP_ID = "4f7532de86894fe1a8caf9b388dd888d"; //TODO Move to build param

    private final BackgroundListenerRegistrar mBackgroundListenerRegistrar = new BackgroundListenerRegistrar();

    private SocketNotificationClient mSocketClient;

    @Override
    public void onCreate() {
        super.onCreate();
        final Broadcaster broadcaster = new Broadcaster(getApplicationContext());

        CrashManager.register(this, HOCKEY_APP_ID);
        // Register for socket notifications
        mSocketClient = new SocketNotificationClient(broadcaster, BuildConfig.SOCKET_NOTIFICATION_ENDPOINT, SOCKET_PORT);

        mBackgroundListenerRegistrar.register(this);
        mBackgroundListenerRegistrar.registerBackgroundListener(new BackgroundListenerRegistrar.BackgroundListener() {
            @Override
            public void movedToForeground() {
                mSocketClient.start();
            }

            @Override
            public void movedToBackground() {
                mSocketClient.stop();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(new Broadcaster.UserSignInStateChangedReceiver() {

            @Override
            public void onUserSignedIn() {
                mSocketClient.restart();
            }

            @Override
            public void onUserSignedOut() {
                mSocketClient.restart();
            }
        }, Broadcaster.getUserSignInStateChangedFilter());

        // Parse
        Parse.initialize(this, BuildConfig.PARSE_APP_ID, BuildConfig.PARSE_CLIENT_KEY);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            installation.put("user", user);
        }
        installation.saveInBackground();

        // Fresco
        final ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
            .setDownsampleEnabled(true)
            .setWebpSupportEnabled(true)
            .build();
        Fresco.initialize(getApplicationContext(), config);

        // Register the repositories and their data sources
        ExampleConfiguration.setSessionRepository(createSessionRepo(broadcaster));
        ExampleConfiguration.setConversationRepository(createConversationsRepo());
        ExampleConfiguration.setUsersRepository(createUserRepository());
        ExampleConfiguration.setMessageRepository(createMessageRepo());
        ExampleConfiguration.setIsTypingRepository(createIsTypingRepository());

        registerInjections();
    }

    private Repository<ExampleUser> createIsTypingRepository() {
        final DelegatingRepository<ExampleUser> typingRepository = new DelegatingRepository<>();
        HandlesUtil.registerHandlersFromAnnotations(typingRepository, new ParseIsTypingDataSource(getApplicationContext(), ParseHelper.INSTANCE));
        return typingRepository;
    }

    private Repository<ExampleUser> createSessionRepo(Broadcaster broadcaster) {
        final DelegatingRepository<ExampleUser> repo = new DelegatingRepository<>();
        HandlesUtil.registerHandlersFromAnnotations(repo, new ParseSessionDataSource(broadcaster, ParseHelper.INSTANCE));
        return repo;
    }

    private Repository<ExampleConversation> createConversationsRepo() {
        final DelegatingRepository<ExampleConversation> conversationRepository = new DelegatingRepository<>();
        final ParseConversationDataSource parseConversationDataSource = new ParseConversationDataSource(ParseHelper.INSTANCE);

        HandlesUtil.registerHandlersFromAnnotations(conversationRepository, parseConversationDataSource);
        Broadcaster.ConversationUpdatedReceiver pullLatestMessagesReceiver = new Broadcaster.ConversationUpdatedReceiver() {
            @Override
            public void onConversationUpdated(@NonNull String conversationId, long timestamp) {
                parseConversationDataSource.reloadConversation(conversationId);
            }

            @Override
            public void onImageUploaded(@NonNull String conversationId, @NonNull String messageId) {
                // Not used
            }
        };
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(pullLatestMessagesReceiver, Broadcaster.getConversationUpdatedFilter());
        return conversationRepository;
    }

    @NonNull
    private Repository<ExampleUser> createUserRepository() {
        final DelegatingRepository<ExampleUser> userRepository = new DelegatingRepository<>();
        HandlesUtil.registerHandlersFromAnnotations(userRepository, new ParseUserDataSource(ParseHelper.INSTANCE));
        return userRepository;
    }

    private Repository<ExampleMessage> createMessageRepo() {
        final DelegatingRepository<ExampleMessage> messageRepository = new DelegatingRepository<>();
        final ParseMessageDataSource.ImageUploader imageUploader = (localId, uri) -> startService(ImageUploadService.createIntent(getApplicationContext(), localId, uri));
        final ParseMessageDataSource networkDataSource = new ParseMessageDataSource(LocalBroadcastManager.getInstance(this), imageUploader, ParseHelper.INSTANCE);
        final ExampleMessageMemoryDataSource messageMemoryDataSource = new ExampleMessageMemoryDataSource(networkDataSource);

        HandlesUtil.registerHandlersFromAnnotations(messageRepository, messageMemoryDataSource);
        return messageRepository;
    }

    protected void registerInjections() {
        Injector.register(RegisterActivity.class, new RegisterActivity.DefaultConfiguration());
        Injector.register(LoginActivity.class, new LoginActivity.DefaultConfiguration());

        Injector.register(ConversationListActivity.class, new ConversationListActivity.DefaultConfiguration());
        Injector.register(NameGroupActivity.class, new NameGroupActivity.DefaultConfiguration());
        Injector.register(SelectUserActivity.class, new SelectUserActivity.DefaultConfiguration());

        Injector.register(ChatActivity.class, new ChatActivity.DefaultConfiguration());
    }
}
