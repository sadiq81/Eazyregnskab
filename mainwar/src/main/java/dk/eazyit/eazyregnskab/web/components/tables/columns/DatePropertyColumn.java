package dk.eazyit.eazyregnskab.web.components.tables.columns;

import dk.eazyit.eazyregnskab.web.components.models.entities.FormattedDateModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

/**
 * @author
 */
public class DatePropertyColumn<T> extends PropertyColumn<T, String> {

    private static final long serialVersionUID = 1L;


    public DatePropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        item.add(new Label(componentId, new FormattedDateModel(getDataModel(rowModel))));
    }

    @Override
    public IModel getDataModel(IModel<T> rowModel) {
        return new PropertyModel<Date>(rowModel.getObject(), getPropertyExpression());
    }
}
