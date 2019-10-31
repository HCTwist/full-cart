package com.twisthenry8gmail.fullcart;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

abstract class ModelAdapter<M extends Model<I>, I extends ListItem> extends EmptyRecyclerAdapter {

    private final Context context;
    private final ListFragmentInterface<I> fragmentInterface;
    private final HeaderViewHolder.HeaderInterface headerInterface;

    private ArrayList<M> entries;

    private RecyclerView recyclerView;

    ModelAdapter(Context context, ListFragmentInterface<I> listInterface, HeaderViewHolder.HeaderInterface headerInterface) {

        this.context = context;
        this.fragmentInterface = listInterface;
        this.headerInterface = headerInterface;
    }

    @Override
    public int getItemViewType(int position) {

        return entries.get(position).getType().ordinal();
    }

    @Override
    public int getItemCount() {

        return entries == null ? 0 : entries.size();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((Update) holder).generate(getEntries().get(position));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {

        if (payloads.isEmpty()) {

            onBindViewHolder(holder, position);
        }
        else {

            for (Object payload : payloads) {

                ((Update) holder).changed(payload);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        this.recyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * @return the context
     */
    Context getContext() {

        return context;
    }

    /**
     * @return the relevant {@link ListFragmentInterface} implementation
     */
    ListFragmentInterface<I> getListInterface() {

        return fragmentInterface;
    }

    /**
     * @return the relevant {@link HeaderViewHolder.HeaderInterface} implementation
     */
    HeaderViewHolder.HeaderInterface getHeaderInterface() {

        return headerInterface;
    }

    /**
     * @return the list of rows of the adapter
     */
    ArrayList<M> getEntries() {

        return entries;
    }

    /**
     * Fill the adapter with entries
     *
     * @param entries the entries
     */
    void populate(ArrayList<M> entries) {

        this.entries = entries;
        notifyDataSetChanged();
    }

    /**
     * Adds {@link ListItem}s to the adapter and updates the content appropriately
     *
     * @param items the items to add
     */
    void addItems(ArrayList<I> items) {

        // Pass to check whether headers exist
        ArrayList<Boolean> headersFound = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {

            headersFound.add(false);

            for (int j = 0; j < getEntries().size(); j++) {

                M model = getEntries().get(j);
                if (model.getType() == Model.Type.STANDARD_HEADER && model.isItemsHeader(items.get(i))) {
                    headersFound.set(i, true);
                    break;
                }
            }
        }

        // Create headers
        ArrayList<Category> headersAdded = new ArrayList<>();
        for (int i = 0; i < headersFound.size(); i++) {

            if (!headersFound.get(i) && !headersAdded.contains(items.get(i).getCategory())) {
                addModel(buildHeaderModel(items.get(i)));
                headersAdded.add(items.get(i).getCategory());
            }
        }

        for (I item : items) {

            addModel(buildContentModel(item));
        }
    }

    /**
     * Check where a model would be added to the adapter
     *
     * @param model the model to be added
     * @return the position the model would be added in
     */
    private int mockAddModel(M model) {

        if (!entries.isEmpty() && model.compareTo(entries.get(0)) <= 0) {

            return 0;
        }

        for (int i = 0; i < entries.size() - 1; i++) {

            M modelCurrent = entries.get(i);
            M modelNext = entries.get(i + 1);

            if (model.compareTo(modelCurrent) >= 0 && model.compareTo(modelNext) <= 0) {

                return i + 1;
            }
        }

        return entries.size();
    }

    /**
     * Adds a model to the adapter and registers the change
     *
     * @param model the model to add
     * @param index the position to add the model
     */
    void addModel(M model, int index) {

        entries.add(index, model);
        notifyItemInserted(index);
    }

    /**
     * Adds a model to the adapter and registers the change. This uses the
     * {@link #mockAddModel(Model)} method to check where the model should be added.
     *
     * @param model the model to be added
     * @see #addModel(Model, int)
     */
    private void addModel(M model) {

        addModel(model, mockAddModel(model));
    }

    /**
     * Removes a model from the adapter and registers the change
     *
     * @param index the position to remove from the adapter
     */
    void removeModel(int index) {

        entries.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * Edit an item from the adapter and registers necessary moves and changes using an
     * {@link ItemChange} payload
     *
     * @param oldItem the item to edit
     * @param newItem the new item
     */
    void editItem(I oldItem, I newItem) {

        int oldIndex = 0;
        boolean removeOldHeader = false;

        boolean newHeaderFound = false;

        // First pass to resolve positions and header information
        for (int i = 0; i < entries.size(); i++) {

            M m = entries.get(i);

            if (m.getType() == Model.Type.CONTENT && m.getItem().equals(oldItem)) {
                oldIndex = i;

                M headerCandidate = entries.get(i - 1);
                if (headerCandidate.getType() == Model.Type.STANDARD_HEADER) {

                    if (!headerCandidate.isItemsHeader(newItem) && (i == entries.size() - 1 || entries.get(i + 1).getType() != Model.Type.CONTENT)) {

                        removeOldHeader = true;
                    }
                }
            }

            if (m.getType() == Model.Type.STANDARD_HEADER && m.isItemsHeader(newItem)) {
                newHeaderFound = true;
            }
        }

        M newModel = buildContentModel(newItem);

        entries.remove(oldIndex);
        int newIndex = mockAddModel(newModel);
        entries.add(newIndex, newModel);
        notifyStaticMove(oldIndex, newIndex);
        notifyItemChanged(newIndex, new ItemChange(newItem));

        if (!newHeaderFound) {

            addModel(buildHeaderModel(newItem));
        }
        if (removeOldHeader) {

            removeModel(oldIndex - 1 + (newIndex < oldIndex ? (!newHeaderFound ? 2 : 1) : 0));
        }
    }

    /**
     * Notifies the adapter of an item being moved without excessive scrolling
     *
     * @param fromPosition previous position of the item
     * @param toPosition   new position of the item
     * @see #notifyItemMoved(int, int)
     */
    private void notifyStaticMove(int fromPosition, int toPosition) {

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View anchor;

        if (manager != null && (anchor = manager.getChildAt(0)) != null) {
            int anchorPosition = manager.findFirstCompletelyVisibleItemPosition();
            int anchorOffset = anchor.getTop();

            notifyItemMoved(fromPosition, toPosition);

            manager.scrollToPositionWithOffset(anchorPosition, anchorOffset);
        }
        else {

            notifyItemMoved(fromPosition, toPosition);
        }
    }

    void removeItem(I item) {

        for (int i = 0; i < getEntries().size(); i++) {

            M model = getEntries().get(i);

            if (model.getType() == Model.Type.CONTENT && model.getItem().equals(item)) {

                boolean removeHeader = getEntries().get(i - 1).getType() == Model.Type.STANDARD_HEADER &&
                        (i == getEntries().size() - 1 || getEntries().get(i + 1).getType() != Model.Type.CONTENT);

                removeModel(i);
                if (removeHeader) removeModel(i - 1);
                return;
            }
        }
    }

    void notifyCategoriesChanged(ArrayList<Category> changeKeys, ArrayList<Category> changeValues) {

        for (int i = 0; i < entries.size(); i++) {

            Model<I> m = entries.get(i);

            if (m.getType() == Model.Type.STANDARD_HEADER) {

                int index = changeKeys.indexOf(m.getCategory());
                if (index >= 0) {
                    Category newCategory = changeValues.get(index);
                    m.setCategory(newCategory);
                    notifyItemChanged(i, newCategory);
                }
            }
            else if (m.getType() == Model.Type.CONTENT) {

                int index = changeKeys.indexOf(m.getItem().getCategory());
                if (index >= 0) {
                    m.getItem().setCategory(changeValues.get(index));
                    ItemChange change = new ItemChange(m.getItem());
                    change.forceCategoryUpdate = true;
                    notifyItemChanged(i, change);
                }
            }
        }
    }

    abstract M buildHeaderModel(I item);

    abstract M buildContentModel(I item);

    interface Update {

        void generate(Model model);

        void changed(Object payload);
    }

    class ItemChange {

        final I item;
        boolean forceCategoryUpdate = false;

        ItemChange(I item) {

            this.item = item;
        }
    }
}
