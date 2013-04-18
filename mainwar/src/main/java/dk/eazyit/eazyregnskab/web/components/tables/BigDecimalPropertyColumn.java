package dk.eazyit.eazyregnskab.web.components.tables;

import dk.eazyit.eazyregnskab.web.components.models.FormattedBigdecimalModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.math.BigDecimal;

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
       item.add(new Label(componentId,new FormattedBigdecimalModel(getDataModel(rowModel))));
    }

    @Override
    public IModel getDataModel(IModel<T> rowModel) {
        return new PropertyModel<BigDecimal>(rowModel.getObject(), getPropertyExpression());
    }
}