package io.vaxly.sema.ui.chat.info;

import com.badoo.chateau.ui.chat.info.ChatInfoPresenter;

import io.vaxly.sema.data.model.ExampleConversation;

public interface ExampleChatInfoPresenter extends ChatInfoPresenter<ExampleConversation> {

    interface ExampleChatInfoView extends ChatInfoView<ExampleConversation> {
    }
}
