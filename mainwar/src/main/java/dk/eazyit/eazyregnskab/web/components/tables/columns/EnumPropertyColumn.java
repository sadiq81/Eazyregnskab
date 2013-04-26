package dk.eazyit.eazyregnskab.web.components.tables.columns;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author
 */
public class EnumPropertyColumn<T> extends PropertyColumn<T, String> {

    public EnumPropertyColumn(IModel<String> displayModel, String propertyExpression) {
            super(displayModel, propertyExpression);
        }

    public EnumPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        item.add(new EnumLabel(componentId,(Enum)getDataModel(rowModel).getObject()));
    }

    @Override
    public IModel<Object> getDataModel(IModel<T> rowModel) {
        return new PropertyModel<Object>(rowModel.getObject(),getPropertyExpression());
    }

    @Override
    public String getCssClass() {
        return super.getCssClass();
    }
}