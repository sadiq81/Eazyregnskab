package dk.eazyit.eazyregnskab.web.components.tables.tables;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.web.components.tables.item.ExportableRowItem;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.ItemsPerPageToolBar;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.export.CustomExportToolbar;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.List;

/**
 * @author
 */
public class ExportableDataTable<T, S> extends DataTable<T, S> {

    public ExportableDataTable(String id, List<? extends IColumn<T, S>> iColumns, IDataProvider dataProvider, String filenameResourceText, Page page) {
        super(id, iColumns, dataProvider, 20);
        setItemsPerPage(getCurrentUser().getItemsPerPage());
        setOutputMarkupId(true);
        setVersioned(false);
        addBottomToolbar(new ItemsPerPageToolBar(this));

        addFirstToolBar(filenameResourceText, page);

        if (dataProvider instanceof ISortableDataProvider) {
            addTopToolbar(new AjaxFallbackHeadersToolbar(this, (ISortableDataProvider) dataProvider));
        } else {
            addTopToolbar(new HeadersToolbar(this, null));
        }

        addTopToolbar(new AjaxNavigationToolbar(this));
        addBottomToolbar(new NoRecordsToolbar(this));
        setItemReuseStrategy(new ReuseIfModelsEqualStrategy());
    }

    protected void addFirstToolBar(String filenameResourceText, Page page) {
        addTopToolbar(new CustomExportToolbar(this, new ResourceModel("datatable.export-to"), new ResourceModel(filenameResourceText), page));
    }

    @Override
    protected Item newRowItem(final String id, final int index, final IModel model) {
        return new ExportableRowItem(id, index, model);
    }

    public AppUser getCurrentUser() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentUser();
    }

    public LegalEntity getCurrentLegalEntity() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentLegalEntity();
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        ((EazyregnskabSesssion) Session.get()).setCurrentLegalEntity(legalEntity);
    }

    public DailyLedger getCurrentDailyLedger() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentDailyLedger();
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        ((EazyregnskabSesssion) Session.get()).setCurrentDailyLedger(dailyLedger);
    }
}