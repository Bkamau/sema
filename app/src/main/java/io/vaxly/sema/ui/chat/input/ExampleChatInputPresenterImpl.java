package io.vaxly.sema.ui.chat.input;

import android.support.annotation.NonNull;

import com.badoo.chateau.core.usecases.messages.SendMessage;
import com.badoo.chateau.ui.chat.input.BaseChatInputPresenter;

import io.vaxly.sema.data.model.ExampleMessage;

public class ExampleChatInputPresenterImpl extends BaseChatInputPresenter<ExampleMessage> implements ExampleChatInputPresenter {

    public ExampleChatInputPresenterImpl(@NonNull String chatId, @NonNull ChatInputView view,
                                         @NonNull SendMessage<ExampleMessage> sendMessage) {
        super(chatId, view, sendMessage);
    }
}
