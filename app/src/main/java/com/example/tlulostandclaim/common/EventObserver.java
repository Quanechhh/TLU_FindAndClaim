package com.example.tlulostandclaim.common;

import androidx.lifecycle.Observer;

import java.util.function.Consumer;

public class EventObserver<T> implements Observer<Event<T>> {

    private final Consumer<T> onEventUnhandledContent;

    public EventObserver(Consumer<T> onEventUnhandledContent) {
        this.onEventUnhandledContent = onEventUnhandledContent;
    }

    @Override
    public void onChanged(Event<T> event) {
        if (event == null) {
            return;
        }
        T value = event.getContentIfNotHandled();
        if (value != null) {
            onEventUnhandledContent.accept(value);
        }
    }
}
