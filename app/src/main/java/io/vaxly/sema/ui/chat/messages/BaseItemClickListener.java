package io.vaxly.sema.ui.chat.messages;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import io.vaxly.sema.data.model.ExampleMessage;

public class BaseItemClickListener implements MessageListAdapter.ItemClickListener {

    @NonNull
    private final RecyclerView mRecyclerView;
    private MessageListAdapter mAdapter;

    BaseItemClickListener(@NonNull RecyclerView recyclerView, @NonNull MessageListAdapter adapter) {
        mRecyclerView = recyclerView;
        mAdapter = adapter;
    }

    @Override
    public void onClick(@NonNull ExampleMessage message) {
        if(!(mRecyclerView.isComputingLayout() || mRecyclerView.isAnimating())) {
            mAdapter.toggleTimestampForMessage(message);
        }
    }

    @Override
    public boolean onLongPress(@NonNull ExampleMessage message) {
        return true;
    }
}
