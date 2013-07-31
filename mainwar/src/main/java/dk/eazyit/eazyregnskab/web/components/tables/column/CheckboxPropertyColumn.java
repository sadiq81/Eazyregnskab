package dk.eazyit.eazyregnskab.web.components.tables.column;

import dk.eazyit.eazyregnskab.web.components.panels.CheckBoxHeaderPanel;
import dk.eazyit.eazyregnskab.web.components.panels.CheckBoxPanel;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author
 */
public class CheckboxPropertyColumn<T> extends PropertyColumn<T, String> {

    public CheckboxPropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        CheckBoxPanel checkBoxPanel = new CheckBoxPanel(componentId, (getDataModel(rowModel)));
        item.add(checkBoxPanel);
    }

    @Override
    public IModel getDataModel(IModel<T> rowModel) {
        PropertyModel model = new PropertyModel<Boolean>(rowModel.getObject(), getPropertyExpression());
        return model;
    }

    @Override
    public Component getHeader(String componentId) {
        return new CheckBoxHeaderPanel(componentId);
    }
}


