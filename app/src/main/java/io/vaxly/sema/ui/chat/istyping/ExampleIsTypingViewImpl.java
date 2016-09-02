package io.vaxly.sema.ui.chat.istyping;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.badoo.barf.mvp.PresenterFactory;
import io.vaxly.sema.R;
import io.vaxly.sema.data.model.ExampleUser;
import io.vaxly.sema.ui.chat.istyping.ExampleIsTypingPresenter.ExampleIsTypingView;
import io.vaxly.sema.ui.widgets.ChatTextInputView;
import com.badoo.chateau.extras.ViewFinder;

import java.util.concurrent.TimeUnit;

public class ExampleIsTypingViewImpl implements ExampleIsTypingView {

    private static final long HIDE_IS_TYPING_INDICATOR = TimeUnit.SECONDS.toMillis(5);

    private final Handler mTypingHandler = new Handler(Looper.getMainLooper());
    private final ActionBar mSupportActionBar;

    public ExampleIsTypingViewImpl(PresenterFactory<ExampleIsTypingView, ExampleIsTypingPresenter> factory,
                                   @NonNull ViewFinder viewFinder,
                                   @NonNull ActionBar supportActionBar) {
        ExampleIsTypingPresenter presenter = factory.init(this);
        mSupportActionBar = supportActionBar;
        ChatTextInputView input = viewFinder.findViewById(R.id.chat_input);
        input.setOnTypingListener(presenter::onUserTyping);
    }

    @Override
    public void showOtherUserTyping(@NonNull ExampleUser user) {
        mSupportActionBar.setSubtitle(R.string.is_typing);
        mTypingHandler.removeCallbacksAndMessages(null);
        mTypingHandler.postDelayed(this::clearIsTyping, HIDE_IS_TYPING_INDICATOR);
    }

    private void clearIsTyping() {
        mSupportActionBar.setSubtitle("");
    }
}
