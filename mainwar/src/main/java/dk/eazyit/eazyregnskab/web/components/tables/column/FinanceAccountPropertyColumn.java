package dk.eazyit.eazyregnskab.web.components.tables.column;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.label.TruncatedLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class FinanceAccountPropertyColumn<T> extends PropertyColumn<FinanceAccount, T> {

    public FinanceAccountPropertyColumn(IModel<String> displayModel, T sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    public FinanceAccountPropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    public void populateItem(Item<ICellPopulator<FinanceAccount>> item, String componentId, IModel<FinanceAccount> rowModel) {
        item.add(new TruncatedLabel(componentId, getDataModel(rowModel), 30));
    }
}
