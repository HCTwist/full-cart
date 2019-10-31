package com.twisthenry8gmail.fullcart;

import androidx.annotation.NonNull;

class ModelPantry extends Model<ListItemPantry> {

    static ModelPantry buildContentModel(ListItemPantry item) {

        ModelPantry model = new ModelPantry();
        model.setType(Type.CONTENT);
        model.setItem(item);
        return model;
    }

    static ModelPantry buildHeaderModel(Category category) {

        ModelPantry model = new ModelPantry();
        model.setType(Type.STANDARD_HEADER);
        model.setCategory(category);
        return model;
    }

    @Override
    public int compareTo(@NonNull Model<ListItemPantry> model) {

        ModelPantry m = (ModelPantry) model;

        if (getType() == Type.CONTENT && m.getType() == Type.CONTENT) {

            if (getItem().getCategory().equals(m.getItem().getCategory())) {

                return getItem().compareTo(m.getItem());
            }
            else {

                return getItem().getCategory().compareTo(m.getItem().getCategory());
            }
        }
        else if (getType() == Type.STANDARD_HEADER && m.getType() == Type.STANDARD_HEADER) {

            return getCategory().compareTo(m.getCategory());
        }
        else if (getType() == Type.CONTENT && m.getType() == Type.STANDARD_HEADER || getType() == Type.STANDARD_HEADER && m.getType() == Type.CONTENT) {

            boolean isContent = getType() == Type.CONTENT;
            ModelPantry content = isContent ? this : m;
            ModelPantry header = isContent ? m : this;
            int factor = isContent ? 1 : -1;

            int c = factor*content.getItem().getCategory().compareTo(header.getCategory());
            return c == 0 ? factor : c;
        }

        throw new RuntimeException("Invalid comparison of " + getType().name() + " and " + m.getType().name());
    }
}
