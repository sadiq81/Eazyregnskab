package dk.eazyit.eazyregnskab.web.components.tables.item;

import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.domain.DataTableCssClass;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class ExportableRowItem<T extends BaseEntity> extends Item<T> {

    public ExportableRowItem(String id, int index, IModel<T> model) {
        super(id, index, model);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {

        T object = getModelObject();
        if (object instanceof DataTableCssClass) {
            tag.put("class", ((DataTableCssClass) object).getCssClassForDataTable());
        }

    }

}
