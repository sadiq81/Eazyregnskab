package dk.eazyit.eazyregnskab.web.components.tables;

import dk.eazyit.eazyregnskab.web.components.panels.CheckBoxListSelectorPanel;
import dk.eazyit.eazyregnskab.web.components.panels.CheckBoxPanel;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.LinkedList;
import java.util.List;

/**
 * @author
 */
public class CheckboxPropertyColumn<T> extends PropertyColumn<T, String> {

    List<CheckBox> checkBoxList = new LinkedList<CheckBox>();

    public CheckboxPropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    public CheckboxPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
        CheckBoxPanel checkBoxPanel = new CheckBoxPanel(componentId, (getDataModel(rowModel)));
        checkBoxList.add(checkBoxPanel.getCheckbox());
        item.add(checkBoxPanel);
    }

    @Override
    public IModel getDataModel(IModel<T> rowModel) {
        return new PropertyModel<Boolean>(rowModel.getObject(), getPropertyExpression());
    }

    @Override
    public Component getHeader(String componentId) {
        return new CheckBoxListSelectorPanel(componentId, checkBoxList);
    }
}


