package com.twisthenry8gmail.roundedbottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BottomRoundedFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getContext() == null || getArguments() == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        View layout = LayoutInflater.from(getContext()).inflate(R.layout.main, container, false);
        FrameLayout contentContainer = layout.findViewById(R.id.content);
        contentContainer.addView(getContent(inflater, container, savedInstanceState));

        return layout;
    }

    protected abstract View getContent(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
}
