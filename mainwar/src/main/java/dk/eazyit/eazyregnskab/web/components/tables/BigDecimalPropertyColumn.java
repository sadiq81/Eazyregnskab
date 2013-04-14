package dk.eazyit.eazyregnskab.web.components.tables;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class BigDecimalPropertyColumn<T> extends PropertyColumn<T, String> {

    public BigDecimalPropertyColumn(IModel<String> displayModel, String propertyExpression) {
            super(displayModel, propertyExpression);
        }

    public BigDecimalPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        super.populateItem(item, componentId, rowModel);
    }

    @Override
    public IModel<Object> getDataModel(IModel<T> rowModel) {
        return super.getDataModel(rowModel);
    }

    @Override
    public String getCssClass() {
        return super.getCssClass();
    }
}