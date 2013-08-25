package dk.eazyit.eazyregnskab.web.components.tables.tables;

import dk.eazyit.eazyregnskab.web.components.tables.item.FinanceAccountItem;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.CustomExportToolbar;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.ItemsPerPageToolBar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NoRecordsToolbar;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

/**
 * @author
 */
public class FinanceAccountAjaxFallbackDefaultDataTable extends DataTable {

    public FinanceAccountAjaxFallbackDefaultDataTable(String id, List<? extends IColumn> iColumns, ISortableDataProvider dataProvider, int rowsPerPage) {
        super(id, iColumns, dataProvider, rowsPerPage);
        setOutputMarkupId(true);
        setVersioned(false);
        addBottomToolbar(new ItemsPerPageToolBar(this));

        //TODO add name of legal entity to export
        addTopToolbar(new CustomExportToolbar(this, new ResourceModel("datatable.export-to"), new ResourceModel("ChartOfAccountsPage.datatable.export-file-name")));

        addTopToolbar(new AjaxNavigationToolbar(this));
        addTopToolbar(new AjaxFallbackHeadersToolbar(this, dataProvider));
        addBottomToolbar(new NoRecordsToolbar(this));
    }

    @Override
    protected Item newRowItem(final String id, final int index, final IModel model) {
        return new FinanceAccountItem(id, index, model);
    }

}