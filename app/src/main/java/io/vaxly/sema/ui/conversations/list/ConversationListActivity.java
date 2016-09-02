package io.vaxly.sema.ui.conversations.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.badoo.barf.mvp.PresenterFactory;
import com.badoo.chateau.core.usecases.conversations.DeleteConversations;
import com.badoo.chateau.core.usecases.conversations.LoadConversations;
import com.badoo.chateau.core.usecases.conversations.SubscribeToConversationUpdates;
import io.vaxly.sema.R;
import io.vaxly.sema.data.model.ExampleConversation;
import io.vaxly.sema.ui.BaseActivity;
import io.vaxly.sema.ui.ExampleConfiguration;
import io.vaxly.sema.ui.Injector;
import io.vaxly.sema.ui.chat.ChatActivity;
import io.vaxly.sema.ui.conversations.create.selectusers.SelectUserActivity;
import io.vaxly.sema.ui.conversations.list.CreateConversationPresenter.CreateConversationFlowListener;
import io.vaxly.sema.ui.conversations.list.CreateConversationPresenter.CreateConversationView;
import com.badoo.chateau.extras.ViewFinder;
import com.badoo.chateau.ui.conversations.list.BaseConversationListPresenter;
import com.badoo.chateau.ui.conversations.list.ConversationListPresenter;
import com.badoo.chateau.ui.conversations.list.ConversationListPresenter.ConversationListFlowListener;
import com.badoo.chateau.ui.conversations.list.ConversationListPresenter.ConversationListView;

public class ConversationListActivity extends BaseActivity
    implements
        ConversationListFlowListener<ExampleConversation>,
    CreateConversationFlowListener {

    public static class DefaultConfiguration extends ExampleConfiguration<ConversationListActivity> {

        @Override
        public void inject(ConversationListActivity target) {
            createConversationListView(target);
            createCreateConversationView(target);
        }

        protected ExampleConversationListView createConversationListView(@NonNull ConversationListActivity activity) {
            final PresenterFactory<ConversationListView<ExampleConversation>, ConversationListPresenter<ExampleConversation>> presenterFactory = new PresenterFactory<>(v -> createConversationListPresenter(v, activity));
            final ExampleConversationListView view = new ExampleConversationListView(ViewFinder.from(activity), activity.getToolbar(), presenterFactory);
            activity.registerPresenter(presenterFactory.get());
            return view;
        }

        protected ConversationListPresenter<ExampleConversation> createConversationListPresenter(@NonNull ConversationListView<ExampleConversation> view, @NonNull ConversationListFlowListener<ExampleConversation> flowListener) {
            final LoadConversations<ExampleConversation> loadConversations = new LoadConversations<>(getConversationRepo());
            final SubscribeToConversationUpdates subscribeToConversationUpdates = new SubscribeToConversationUpdates(getConversationRepo());
            final DeleteConversations deleteConversations = new DeleteConversations(getConversationRepo());
            return new BaseConversationListPresenter<>(view, flowListener, loadConversations, subscribeToConversationUpdates, deleteConversations);
        }

        protected CreateConversationView createCreateConversationView(@NonNull ConversationListActivity activity) {
            return new CreateConversationViewImpl(ViewFinder.from(activity), new PresenterFactory<>(v -> createCreateConversationPresenter(v, activity)));
        }

        protected CreateConversationPresenter createCreateConversationPresenter(@NonNull CreateConversationView view, @NonNull CreateConversationFlowListener flowListener) {
            return new CreateConversationPresenterImpl(flowListener);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_conversations);
        setTitle(R.string.title_activity_conversations);
        Injector.inject(this);
    }

    @Override
    public void requestOpenConversation(@NonNull ExampleConversation conversation) {
        final Intent intent = ChatActivity.create(this, conversation.getId(), conversation.getName());
        startActivity(intent);
    }

    @Override
    public void requestCreateNewConversation() {
        final Intent intent = new Intent(this, SelectUserActivity.class);
        startActivity(intent);
    }
}
