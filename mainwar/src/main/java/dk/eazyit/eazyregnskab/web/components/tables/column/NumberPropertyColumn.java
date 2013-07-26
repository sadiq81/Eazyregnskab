package dk.eazyit.eazyregnskab.web.components.tables.column;

import dk.eazyit.eazyregnskab.web.components.models.entities.FormattedNumberModel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author
 */
public class NumberPropertyColumn<T> extends PropertyColumn<T, String> {

    Integer maximumFractionDigits;
    Integer minimumFractionDigits;

    public NumberPropertyColumn(IModel<String> displayModel, String propertyExpression, Integer minimumFractionDigits, Integer maximumFractionDigits) {
        super(displayModel, propertyExpression);
        this.minimumFractionDigits = minimumFractionDigits;
        this.maximumFractionDigits = maximumFractionDigits;
    }


    public NumberPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression, Integer minimumFractionDigits, Integer maximumFractionDigits) {
        super(displayModel, sortProperty, propertyExpression);
        this.minimumFractionDigits = minimumFractionDigits;
        this.maximumFractionDigits = maximumFractionDigits;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        item.add(new Label(componentId, new FormattedNumberModel(getDataModel(rowModel), minimumFractionDigits, maximumFractionDigits)));
    }

    @Override
    public String getCssClass() {
        return "align-right";
    }

    @Override
    public IModel getDataModel(IModel<T> rowModel) {
        return new PropertyModel<Number>(rowModel.getObject(), getPropertyExpression());
    }
}