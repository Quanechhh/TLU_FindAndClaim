package com.example.tlulostandclaim.common;

import androidx.lifecycle.Observer;
import java.util.function.Consumer;
/**
 * Wrapper for data that is exposed via LiveData representing an event.
 * Ensures that the content is only handled once.
 */
public class Event<T> {

    private final T content;
    private boolean hasBeenHandled = false; // public getter, private setter in Kotlin

    public Event(T content) {
        this.content = content;
    }

    /**
     * Returns the content and prevents its use again.
     * @return the content if it has not been handled yet, or null otherwise
     */
    public T getContentIfNotHandled() {
        if (hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     * @return the wrapped content
     */
    public T peekContent() {
        return content;
    }

    /**
     * Indicates whether this event’s content has already been handled.
     * (Kotlin’s `var hasBeenHandled = ... private set` becomes a private field + public getter.)
     */
    public boolean hasBeenHandled() {
        return hasBeenHandled;
    }
}

