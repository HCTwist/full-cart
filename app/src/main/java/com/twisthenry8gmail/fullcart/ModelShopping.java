package com.twisthenry8gmail.fullcart;

import androidx.annotation.NonNull;

class ModelShopping extends Model<ListItemShopping> {

    private boolean categoryChecked;

    static ModelShopping buildContentModel(ListItemShopping item) {

        ModelShopping model = new ModelShopping();
        model.setType(Type.CONTENT);
        model.setItem(item);
        return model;
    }

    static ModelShopping buildHeaderModel(Category category, boolean categoryChecked) {

        ModelShopping model = new ModelShopping();
        model.setType(Type.STANDARD_HEADER);
        model.setCategory(category);
        model.categoryChecked = categoryChecked;
        return model;
    }

    static ModelShopping buildBasketHeaderModel() {

        ModelShopping model = new ModelShopping();
        model.setType(Type.BASKET_HEADER);
        return model;
    }

    boolean isCategoryChecked() {

        return categoryChecked;
    }

    @Override
    boolean isItemsHeader(ListItemShopping item) {

        return super.isItemsHeader(item) && isCategoryChecked() == item.isChecked();
    }

    @Override
    public int compareTo(@NonNull Model<ListItemShopping> model) {

        ModelShopping m = (ModelShopping) model;

        if (getType() == Type.CONTENT && m.getType() == Type.CONTENT) {

            if (getItem().isChecked() == m.getItem().isChecked()) {
                if (getItem().getCategory().equals(m.getItem().getCategory())) {

                    return getItem().compareTo(m.getItem());
                }
                else {

                    return getItem().getCategory().compareTo(m.getItem().getCategory());
                }
            }
            else {
                return getItem().isChecked() ? 1 : -1;
            }
        }
        else if (getType() == Type.STANDARD_HEADER && m.getType() == Type.STANDARD_HEADER) {

            if (categoryChecked == m.categoryChecked) {

                return getCategory().compareTo(m.getCategory());
            }
            else {

                return categoryChecked ? 1 : -1;
            }
        }
        else if (getType() == Type.CONTENT && m.getType() == Type.STANDARD_HEADER || getType() == Type.STANDARD_HEADER && m.getType() == Type.CONTENT) {

            boolean isContent = getType() == Type.CONTENT;
            ModelShopping content = isContent ? this : m;
            ModelShopping header = isContent ? m : this;
            int factor = isContent ? 1 : -1;

            if (header.categoryChecked == content.getItem().isChecked()) {

                int c = factor * content.getItem().getCategory().compareTo(header.getCategory());
                return c == 0 ? factor : c;
            }
            else {

                return factor * (content.getItem().isChecked() ? 1 : -1);
            }
        }
        else if (getType() == Type.STANDARD_HEADER && m.getType() == Type.BASKET_HEADER || getType() == Type.BASKET_HEADER && m.getType() == Type.STANDARD_HEADER) {

            boolean isHeader = getType() == Type.STANDARD_HEADER;
            ModelShopping header = isHeader ? this : m;
            return (isHeader ? 1 : -1) * (header.categoryChecked ? 1 : -1);
        }
        else if (getType() == Type.CONTENT && m.getType() == Type.BASKET_HEADER || getType() == Type.BASKET_HEADER && m.getType() == Type.CONTENT) {

            boolean isContent = getType() == Type.CONTENT;
            ModelShopping content = isContent ? this : m;
            return (isContent ? 1 : -1) * (content.getItem().isChecked() ? 1 : -1);
        }

        throw new RuntimeException("Invalid comparison of " + getType().name() + " and " + m.getType().name());
    }
}
