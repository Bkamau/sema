package io.vaxly.sema.ui.session.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.badoo.barf.mvp.PresenterFactory;
import io.vaxly.sema.R;
import io.vaxly.sema.data.model.ExampleUser;
import io.vaxly.sema.ui.BaseActivity;
import io.vaxly.sema.ui.ExampleConfiguration;
import io.vaxly.sema.ui.Injector;
import io.vaxly.sema.ui.conversations.list.ConversationListActivity;
import io.vaxly.sema.ui.session.login.LoginActivity;
import io.vaxly.sema.ui.session.register.RegistrationPresenter.RegistrationView;
import io.vaxly.sema.session.Register;
import com.badoo.chateau.extras.ViewFinder;

import static io.vaxly.sema.ui.session.register.RegistrationPresenter.*;

public class RegisterActivity extends BaseActivity implements RegistrationFlowListener {

    public static class DefaultConfiguration extends ExampleConfiguration<RegisterActivity> {

        @Override
        public void inject(RegisterActivity target) {
            final PresenterFactory<RegistrationView, RegistrationPresenter> presenterFactory = new PresenterFactory<>(v -> createRegistrationPresenter(v, target));
            new RegistrationViewImpl(ViewFinder.from(target), presenterFactory);
            target.registerPresenter(presenterFactory.get());
        }

        protected RegistrationPresenterImpl<ExampleUser> createRegistrationPresenter(@NonNull RegistrationView view, @NonNull RegistrationFlowListener flowListener) {
            return new RegistrationPresenterImpl<>(view, flowListener, new Register<>(getSessionRepo()));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Injector.inject(this);
    }

    void setRegistrationPresenter(@NonNull RegistrationPresenter presenter) {
        registerPresenter(presenter);
    }

    @Override
    public void userRegistered() {
        openConversationsList();
    }

    @Override
    public void userAlreadyRegistered() {
        openLogin();
    }

    private void openConversationsList() {
        final Intent intent = new Intent(this, ConversationListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    private void openLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
