package dk.eazyit.eazyregnskab.web.components.tables.columns;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.panels.action.DailyLedgerActionPanel;
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
public class ColumnsForDailyLedgerPage extends ArrayList<IColumn<DailyLedger, String>> {


    public ColumnsForDailyLedgerPage(final BaseCreateEditForm<DailyLedger> form) {
        add(new PropertyColumn<DailyLedger, String>(new ResourceModel("DailyLedgerPage.name"), "name", "name"));
        add(new AbstractColumn<DailyLedger, String>(new ResourceModel("DailyLedgerPage.action")) {
            @Override
            public void populateItem(Item<ICellPopulator<DailyLedger>> cellItem, String componentId, IModel<DailyLedger> rowModel) {
                cellItem.add(new DailyLedgerActionPanel(componentId, rowModel, form));
            }
        });

    }
}
