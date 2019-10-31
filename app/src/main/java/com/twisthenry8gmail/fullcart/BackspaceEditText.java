package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * An implementation of {@link android.widget.EditText} that allows you to set a listener for
 * backspace events for the soft keyboard
 */
public class BackspaceEditText extends AppCompatEditText {

    private Runnable backspaceListener;

    public BackspaceEditText(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

        return new InputConnectionWrapper(super.onCreateInputConnection(outAttrs), true) {

            @Override
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {

                if (backspaceListener != null && getText() != null && getText().toString().isEmpty() && beforeLength == 1 && afterLength == 0) {
                    backspaceListener.run();
                }
                return super.deleteSurroundingText(beforeLength, afterLength);
            }

            @Override
            public boolean sendKeyEvent(KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && backspaceListener != null && getText() != null && getText().toString().isEmpty()) {
                    backspaceListener.run();
                }
                return super.sendKeyEvent(event);
            }
        };
    }

    /**
     * Attach a backspace listener
     *
     * @param listener the listener to attach
     */
    void setBackspaceListener(Runnable listener) {

        backspaceListener = listener;
    }
}
