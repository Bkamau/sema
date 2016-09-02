package io.vaxly.sema.ui.chat.info;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.badoo.barf.mvp.PresenterFactory;
import io.vaxly.sema.data.model.ExampleConversation;
import io.vaxly.sema.ui.chat.info.ExampleChatInfoPresenter.ExampleChatInfoView;

public class ExampleChatInfoViewImpl implements ExampleChatInfoView {


    private final ActionBar mSupportActionBar;

    public ExampleChatInfoViewImpl(@NonNull PresenterFactory<ExampleChatInfoView, ExampleChatInfoPresenter> presenterFactory,
                                   @NonNull ActionBar supportActionBar) {
        mSupportActionBar = supportActionBar;
        presenterFactory.init(this);
    }

    @Override
    public void showConversation(@NonNull ExampleConversation conversation) {
        mSupportActionBar.setTitle(conversation.getName());
    }
}
