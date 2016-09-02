package io.vaxly.sema.ui.util;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.badoo.chateau.data.models.payloads.Payload;
import io.vaxly.sema.R;
import io.vaxly.sema.data.model.ExampleMessage;
import com.badoo.chateau.extras.recycle.BindableViewHolder;

import java.text.DateFormat;
import java.util.Date;

public abstract class MessageViewHolder<P extends Payload> extends BindableViewHolder<ExampleMessage> {

    private final View mError;
    @Nullable
    private final TextView mTimestamp;
    private final DateFormat mDateFormat;

    public MessageViewHolder(View itemView) {
        super(itemView);
        mTimestamp = (TextView) itemView.findViewById(R.id.message_timestamp);
        mDateFormat = DateFormat.getDateTimeInstance();
        mError = itemView.findViewById(R.id.message_error);
    }

    @Override
    public void bind(ExampleMessage message) {
        super.bind(message);
        //noinspection unchecked
        bindPayload(message, (P) message.getPayload());
        if (mError != null) {
            mError.setVisibility(message.isFromMe() && message.isFailedToSend() ? View.VISIBLE : View.GONE);
        }
    }

    public void showTimestamp(boolean show) {
        if (mTimestamp != null) {
            mTimestamp.setVisibility(show ? View.VISIBLE : View.GONE);
            if (show) {
                mTimestamp.setText(
                    mDateFormat.format(new Date(getBoundItem().getTimestamp()))
                );
            }
        }
    }

    protected abstract void bindPayload(ExampleMessage message, P payload);
}
