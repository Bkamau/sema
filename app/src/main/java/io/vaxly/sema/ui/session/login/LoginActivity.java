package io.vaxly.sema.ui.session.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.badoo.barf.mvp.PresenterFactory;
import io.vaxly.sema.R;
import io.vaxly.sema.ui.BaseActivity;
import io.vaxly.sema.ui.ExampleConfiguration;
import io.vaxly.sema.ui.Injector;
import io.vaxly.sema.ui.conversations.list.ConversationListActivity;
import io.vaxly.sema.ui.session.login.LoginPresenter.LoginFlowListener;
import io.vaxly.sema.ui.session.login.LoginPresenter.LoginView;
import io.vaxly.sema.ui.session.register.RegisterActivity;
import io.vaxly.sema.session.SignIn;
import com.badoo.chateau.extras.ViewFinder;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoginFlowListener {

    public static class DefaultConfiguration extends ExampleConfiguration<LoginActivity> {

        @Override
        public void inject(LoginActivity target) {
            final PresenterFactory<LoginView, LoginPresenter> presenterFactory = new PresenterFactory<>(v -> createLoginPresenter(v, target));
            new LoginViewImpl(ViewFinder.from(target), presenterFactory);
            target.registerPresenter(presenterFactory.get());
        }

        protected LoginPresenter createLoginPresenter(@NonNull LoginView view, @NonNull LoginFlowListener flowListener) {
            return new LoginPresenterImpl<>(view, flowListener, new SignIn<>(getSessionRepo()));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Injector.inject(this);
    }

    @Override
    public void userLoggedIn() {
        openConversationList();
    }

    @Override
    public void userNotRegistered() {
        openRegistration();
    }

    private void openConversationList() {
        finish(); // Make sure you cannot return here by pressing back from Conversations
        final Intent intent = new Intent(this, ConversationListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void openRegistration() {
        final Intent intent = new Intent(this, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

