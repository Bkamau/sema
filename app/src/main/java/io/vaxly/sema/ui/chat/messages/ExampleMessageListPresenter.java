package io.vaxly.sema.ui.chat.messages;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.badoo.barf.mvp.FlowListener;
import com.badoo.chateau.ui.chat.messages.MessageListPresenter;

import io.vaxly.sema.data.model.ExampleMessage;

/**
 * Extension of MessageListPresenter for the example app.
 */
public interface ExampleMessageListPresenter extends MessageListPresenter<ExampleMessage> {

    /**
     * Notifies that an image has been clicked.
     */
    void onImageClicked(@NonNull Uri uri);

    interface ExampleMessageListFlowListener extends FlowListener {

        void requestOpenImage(@NonNull Uri imageUri);

    }

}
