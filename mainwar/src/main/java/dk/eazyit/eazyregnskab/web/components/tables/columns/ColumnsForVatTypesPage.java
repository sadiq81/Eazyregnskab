package dk.eazyit.eazyregnskab.web.components.tables.columns;

import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.panels.action.VatTypeActionPanel;
import dk.eazyit.eazyregnskab.web.components.tables.column.NumberPropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.ArrayList;

/**
 * @author
 */
public class ColumnsForVatTypesPage extends ArrayList<IColumn<VatType, String>> {

    public ColumnsForVatTypesPage(final BaseCreateEditForm<VatType> form) {
        add(new PropertyColumn<VatType, String>(new ResourceModel("VatTypesPage.name"), "name", "name"));
        add(new PropertyColumn<VatType, String>(new ResourceModel("VatTypesPage.finance.account"), "financeAccount.name"));
        add(new PropertyColumn<VatType, String>(new ResourceModel("VatTypesPage.finance.account.reverse"), "financeAccountReverse.name"));
        add(new NumberPropertyColumn<VatType>(new ResourceModel("VatTypesPage.percentage"), "percentage", "percentage", 2, 2));
        add(new AbstractColumn<VatType, String>(new ResourceModel("VatTypesPage.action")) {
            @Override
            public void populateItem(Item<ICellPopulator<VatType>> cellItem, String componentId, IModel<VatType> rowModel) {
                cellItem.add(new VatTypeActionPanel(componentId, rowModel, form));
            }
        });

    }
}
