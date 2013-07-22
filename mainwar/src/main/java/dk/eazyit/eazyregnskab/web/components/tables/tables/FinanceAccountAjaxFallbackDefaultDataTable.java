package dk.eazyit.eazyregnskab.web.components.tables.tables;

import dk.eazyit.eazyregnskab.web.components.tables.item.FinanceAccountItem;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.ItemsPerPageToolBar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public class FinanceAccountAjaxFallbackDefaultDataTable extends AjaxFallbackDefaultDataTable {

    public FinanceAccountAjaxFallbackDefaultDataTable(String id, List<? extends IColumn> iColumns, ISortableDataProvider dataProvider, int rowsPerPage) {
        super(id, iColumns, dataProvider, rowsPerPage);
        addBottomToolbar(new ItemsPerPageToolBar(this));
    }

    @Override
    protected Item newRowItem(final String id, final int index, final IModel model) {
        return new FinanceAccountItem(id, index, model);
    }
}
