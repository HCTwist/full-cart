package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ModelAdapterPantry extends ModelAdapter<ModelPantry, ListItemPantry> {

    ModelAdapterPantry(Context context, ListFragmentInterface<ListItemPantry> listInterface, HeaderViewHolder.HeaderInterface headerInterface) {

        super(context, listInterface, headerInterface);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == Model.Type.STANDARD_HEADER.ordinal()) {

            View layout = LayoutInflater.from(getContext()).inflate(R.layout.standard_header, parent, false);
            return new HeaderViewHolder(layout, getHeaderInterface());
        }
        else {
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.pantry_content, parent, false);
            return new PantryContentViewHolder(layout, getListInterface());
        }
    }

    @Override
    void loadItems() {

        AsyncLoaderPantry loader = new AsyncLoaderPantry(this);
        loader.execute();
    }

    @Override
    ModelPantry buildHeaderModel(ListItemPantry item) {

        return ModelPantry.buildHeaderModel(item.getCategory());
    }

    @Override
    ModelPantry buildContentModel(ListItemPantry item) {

        return ModelPantry.buildContentModel(item);
    }
}
