package io.vaxly.sema.ui.chat.messages;

import android.support.annotation.NonNull;

import io.vaxly.sema.data.model.ExampleMessage;

import java.util.List;

/**
 * Preprocessor that can add or remove items from the list of messages before it's displayed
 */
public interface ItemPreProcessor {

    List<ExampleMessage> doProcess(@NonNull List<ExampleMessage> input);
}
