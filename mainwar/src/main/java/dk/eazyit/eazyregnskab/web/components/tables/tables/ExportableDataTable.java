package dk.eazyit.eazyregnskab.web.components.tables.tables;

import dk.eazyit.eazyregnskab.web.components.tables.item.ExportableRowItem;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.CustomExportToolbar;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.ItemsPerPageToolBar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

/**
 * @author
 */
public abstract class ExportableDataTable<T, S> extends DataTable<T, S> {

    public ExportableDataTable(String id, List<? extends IColumn<T, S>> iColumns, IDataProvider dataProvider, int rowsPerPage, String filenameResourceText, float[] columnWidths) {
        super(id, iColumns, dataProvider, rowsPerPage);
        setOutputMarkupId(true);
        setVersioned(false);
        addBottomToolbar(new ItemsPerPageToolBar(this));

        addTopToolbar(new CustomExportToolbar(this, new ResourceModel("datatable.export-to"), new ResourceModel(filenameResourceText), columnWidths));

        if (dataProvider instanceof ISortableDataProvider) {
            addTopToolbar(new AjaxFallbackHeadersToolbar(this, (ISortableDataProvider) dataProvider));
        } else {
            addTopToolbar(new HeadersToolbar(this, null));
        }

        addTopToolbar(new AjaxNavigationToolbar(this));
        addBottomToolbar(new NoRecordsToolbar(this));
    }

    @Override
    protected Item newRowItem(final String id, final int index, final IModel model) {
        return new ExportableRowItem(id, index, model);
    }

    public abstract String getTitle();
}