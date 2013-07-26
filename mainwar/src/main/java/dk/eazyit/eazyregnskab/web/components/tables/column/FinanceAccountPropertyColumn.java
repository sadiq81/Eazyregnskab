package dk.eazyit.eazyregnskab.web.components.tables.column;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
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
}
