package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EmptyView extends FrameLayout {

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);

        inflate(context, R.layout.empty_view, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmptyView);

        Drawable imageRes = a.getDrawable(R.styleable.EmptyView_emptyImage);
        String text = a.getString(R.styleable.EmptyView_emptyText);

        ImageView imageView = findViewById(R.id.empty_view_image);
        TextView textView = findViewById(R.id.empty_view_text);

        imageView.setImageDrawable(imageRes);
        textView.setText(text);

        a.recycle();
    }
}
