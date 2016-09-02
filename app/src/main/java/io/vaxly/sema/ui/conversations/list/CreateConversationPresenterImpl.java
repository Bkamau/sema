package io.vaxly.sema.ui.conversations.list;

import android.support.annotation.NonNull;

import com.badoo.barf.mvp.BaseRxPresenter;

/**
 * Implementation of CreateConversationPresenter which delegates the operation to the flow listener
 */
class CreateConversationPresenterImpl extends BaseRxPresenter implements CreateConversationPresenter {

    private CreateConversationFlowListener mFlowListener;

    CreateConversationPresenterImpl(@NonNull CreateConversationFlowListener flowListener) {
        mFlowListener = flowListener;
    }

    @Override
    public void onCreateNewConversationClicked() {
        mFlowListener.requestCreateNewConversation();
    }

}
