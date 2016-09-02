package io.vaxly.sema.ui.chat.input;

import android.support.annotation.NonNull;

import com.badoo.barf.mvp.PresenterFactory;
import io.vaxly.sema.R;
import io.vaxly.sema.data.model.ExampleMessage;
import io.vaxly.sema.ui.widgets.ChatTextInputView;
import com.badoo.chateau.extras.ViewFinder;
import com.badoo.chateau.ui.chat.input.ChatInputPresenter;

import static com.badoo.chateau.ui.chat.input.ChatInputPresenter.ChatInputView;

public class ChatInputViewImpl implements ChatInputView {

    private final ChatTextInputView mInput;
    @NonNull
    private final ChatInputPresenter<ExampleMessage> mPresenter;

    public ChatInputViewImpl(@NonNull String conversationId,
                             @NonNull ViewFinder viewFinder,
                             @NonNull PresenterFactory<ChatInputView, ChatInputPresenter<ExampleMessage>> presenterFactory) {
        mPresenter = presenterFactory.init(this);
        mInput = viewFinder.findViewById(R.id.chat_input);
        mInput.setOnSendClickListener(v -> mPresenter.onSendMessage(ExampleMessage.createOutgoingTextMessage(conversationId, mInput.getText())));
    }

    @Override
    public void clearText() {
        mInput.clearText();
    }

}
