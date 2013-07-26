package dk.eazyit.eazyregnskab.web.components.tables.columns;

import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.panels.action.FiscalYearActionPanel;
import dk.eazyit.eazyregnskab.web.components.tables.column.DatePropertyColumn;
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
public class ColumnsForFiscalYearsPage extends ArrayList<IColumn<FiscalYear, String>> {

    public ColumnsForFiscalYearsPage(final BaseCreateEditForm<FiscalYear> form) {
        add(new DatePropertyColumn<FiscalYear>(new ResourceModel("start"), "start"));
        add(new DatePropertyColumn<FiscalYear>(new ResourceModel("end"), "end"));
        add(new PropertyColumn<FiscalYear, String>(new ResourceModel("daysBetween"), "daysBetween"));
        add(new AbstractColumn<FiscalYear, String>(new ResourceModel("action")) {
            @Override
            public void populateItem(Item<ICellPopulator<FiscalYear>> cellItem, String componentId, IModel<FiscalYear> rowModel) {
                cellItem.add(new FiscalYearActionPanel(componentId, rowModel, form));
            }
        });

    }
}