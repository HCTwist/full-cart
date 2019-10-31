package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.twisthenry8gmail.fullcart.ShoppingContentViewHolder.TransferInterface;

public class ModelAdapterShopping extends ModelAdapter<ModelShopping, ListItemShopping> {

    private final TransferInterface transferInterface;

    ModelAdapterShopping(Context context, ListFragmentInterface<ListItemShopping> listInterface, TransferInterface transferInterface, HeaderViewHolder.HeaderInterface headerInterface) {

        super(context, listInterface, headerInterface);
        this.transferInterface = transferInterface;
    }

    @Override
    ModelShopping buildHeaderModel(ListItemShopping item) {

        return ModelShopping.buildHeaderModel(item.getCategory(), item.isChecked());
    }

    @Override
    ModelShopping buildContentModel(ListItemShopping item) {

        return ModelShopping.buildContentModel(item);
    }

    @Override
    void loadItems() {

        AsyncLoaderShopping loader = new AsyncLoaderShopping(this);
        loader.execute();
    }

    @Override
    void editItem(ListItemShopping oldItem, ListItemShopping newItem) {

        super.editItem(oldItem, newItem);
        ensureBasketHeader();
    }

    @Override
    void removeItem(ListItemShopping item) {

        super.removeItem(item);
        ensureBasketHeader();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == Model.Type.STANDARD_HEADER.ordinal()) {

            View layout = LayoutInflater.from(getContext()).inflate(R.layout.standard_header, parent, false);
            return new HeaderViewHolder(layout, getHeaderInterface());
        }
        else if (viewType == Model.Type.CONTENT.ordinal()) {
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.shopping_content, parent, false);
            return new ShoppingContentViewHolder(layout, getListInterface(), transferInterface);
        }
        else {
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.basket_header, parent, false);
            return new BasketViewHolder(layout);
        }
    }

    void ensureBasketHeader() {

        for (int i = 0; i < getEntries().size() - 1; i++) {

            ModelShopping m = getEntries().get(i);
            if (m.getType() == Model.Type.STANDARD_HEADER && m.isCategoryChecked()) {
                if (i == 0 || getEntries().get(i - 1).getType() != Model.Type.BASKET_HEADER) {

                    addModel(ModelShopping.buildBasketHeaderModel(), i);
                }
                return;
            }
            else if (m.getType() == Model.Type.BASKET_HEADER) {
                ModelShopping mNext = getEntries().get(i + 1);
                if (mNext.getType() != Model.Type.STANDARD_HEADER || !mNext.isCategoryChecked()) {

                    removeModel(i);
                }
                return;
            }
        }

        if (getEntries().size() > 0 && getEntries().get(getEntries().size() - 1).getType() == Model.Type.BASKET_HEADER) {
            removeModel(getEntries().size() - 1);
        }
    }

    class BasketViewHolder extends RecyclerView.ViewHolder implements Update {

        BasketViewHolder(@NonNull View itemView) {

            super(itemView);
        }

        @Override
        public void generate(Model model) {

        }

        @Override
        public void changed(Object payload) {

        }
    }
}
