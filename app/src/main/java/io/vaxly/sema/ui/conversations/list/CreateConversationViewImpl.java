package io.vaxly.sema.ui.conversations.list;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import com.badoo.barf.mvp.MvpView;
import com.badoo.barf.mvp.PresenterFactory;
import io.vaxly.sema.R;
import io.vaxly.sema.ui.conversations.list.CreateConversationPresenter.CreateConversationView;
import com.badoo.chateau.extras.ViewFinder;

class CreateConversationViewImpl implements CreateConversationView, MvpView {

    private final CreateConversationPresenter mPresenter;

    public CreateConversationViewImpl(@NonNull ViewFinder viewFinder,
                                      @NonNull PresenterFactory<CreateConversationView, CreateConversationPresenter> presenterFactory) {
        mPresenter = presenterFactory.init(this);
        FloatingActionButton startNewChat = viewFinder.findViewById(R.id.conversations_start_new_chat_button);
        startNewChat.setOnClickListener(v -> mPresenter.onCreateNewConversationClicked());
    }
}
