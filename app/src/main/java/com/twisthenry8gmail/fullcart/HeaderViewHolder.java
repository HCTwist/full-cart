package com.twisthenry8gmail.fullcart;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link RecyclerView.ViewHolder} implementation for a standard header
 */
class HeaderViewHolder extends RecyclerView.ViewHolder implements ModelAdapter.Update {

    private final HeaderInterface headerInterface;

    private final ContentTextSwitcher title;

    HeaderViewHolder(View v, HeaderInterface headerInterface) {

        super(v);
        this.headerInterface = headerInterface;
        title = v.findViewById(R.id.list_header_title);
    }

    @Override
    public void generate(Model model) {

        //Content
        title.setCurrentText(model.getCategory().getName());

        registerListeners(model);
    }

    @Override
    public void changed(Object payload) {

        Category category = (Category) payload;
        title.setText(category.getName());
    }

    /**
     * Register actions on the header
     * @param model the model
     */
    private void registerListeners(Model model) {

        // Remove items under header
        itemView.setOnLongClickListener(v -> {

            String message = itemView.getContext().getString(R.string.delete_bundle_message, model.getCategory().getName());
            DeleteDialogFragment deleteDialogFragment = DeleteDialogFragment.getInstance(message, getAdapterPosition());

            deleteDialogFragment.setTargetFragment(headerInterface.getDeleteTargetFragment(), 0);
            deleteDialogFragment.show(((AppCompatActivity) itemView.getContext()).getSupportFragmentManager(), DeleteDialogFragment.TAG);

            return true;
        });
    }

    interface HeaderInterface {

        ListFragment getDeleteTargetFragment();
    }
}
