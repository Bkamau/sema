package io.vaxly.sema.ui.conversations;

import java.util.Comparator;

import io.vaxly.sema.data.model.ExampleConversation;

/**
 * Sorts conversations in descending order
 */
public class ConversationByLastMessageComparator implements Comparator<ExampleConversation> {

    @Override
    public int compare(ExampleConversation lhsConversation, ExampleConversation rhsConversation) {
        final long lhs = lhsConversation.getLastMessage() != null ? lhsConversation.getLastMessage().getTimestamp() : 0;
        final long rhs = rhsConversation.getLastMessage() != null ? rhsConversation.getLastMessage().getTimestamp() : 0;
        return lhs > rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    @Override
    public boolean equals(Object object) {
        return object == this;
    }
}
