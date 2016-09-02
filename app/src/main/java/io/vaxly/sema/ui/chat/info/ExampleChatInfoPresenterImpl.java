package io.vaxly.sema.ui.chat.info;

import android.support.annotation.NonNull;

import com.badoo.chateau.core.usecases.conversations.GetConversation;
import com.badoo.chateau.ui.chat.info.BaseChatInfoPresenter;

import io.vaxly.sema.data.model.ExampleConversation;

public class ExampleChatInfoPresenterImpl extends BaseChatInfoPresenter<ExampleConversation> implements ExampleChatInfoPresenter {

    public ExampleChatInfoPresenterImpl(@NonNull ChatInfoView<ExampleConversation> view,
                                        @NonNull String conversationId,
                                        @NonNull GetConversation<ExampleConversation> getConversation) {
        super(view, conversationId, getConversation);
    }
}
