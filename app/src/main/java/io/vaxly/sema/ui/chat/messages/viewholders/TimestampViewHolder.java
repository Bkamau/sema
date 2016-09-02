package io.vaxly.sema.ui.chat.messages.viewholders;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.badoo.chateau.data.models.payloads.TimestampPayload;
import io.vaxly.sema.R;
import io.vaxly.sema.data.model.ExampleMessage;
import io.vaxly.sema.ui.util.MessageViewHolder;

public class TimestampViewHolder extends MessageViewHolder<TimestampPayload> {

    private final TextView mTimestamp;

    public TimestampViewHolder(@NonNull View itemView) {
        super(itemView);
        mTimestamp = (TextView) itemView.findViewById(R.id.day_timestamp);
    }

    @Override
    protected void bindPayload(ExampleMessage message, TimestampPayload payload) {
        mTimestamp.setText(DateUtils.getRelativeTimeSpanString(message.getTimestamp(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS));
    }
}
