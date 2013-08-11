package dk.eazyit.eazyregnskab.web.components.tables.column;

import dk.eazyit.eazyregnskab.web.components.label.ToolTipLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author
 */
public class ToolTipPropertyColumn<T> extends PropertyColumn<T, String> {

    String toolTipExpression;

    public ToolTipPropertyColumn(IModel<String> displayModel, String propertyExpression, String toolTipExpression) {
        super(displayModel, propertyExpression);
        this.toolTipExpression = toolTipExpression;
    }

    public ToolTipPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression, String toolTipExpression) {
        super(displayModel, sortProperty, propertyExpression);
        this.toolTipExpression = toolTipExpression;
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        item.add(new ToolTipLabel(componentId, getDataModel(rowModel), getToolTipDataModel(rowModel).getObject()));
    }

    public IModel<String> getToolTipDataModel(IModel<T> rowModel) {
        PropertyModel<String> propertyModel = new PropertyModel<String>(rowModel, toolTipExpression);
        return propertyModel;
    }
}
