package dk.eazyit.eazyregnskab.web.components.tables.tables;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.web.components.tables.item.EazyRowItem;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.ItemsPerPageToolBar;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackHeadersToolbar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxNavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public class EazyDataTable<T, S> extends DataTable<T, S> {

    public EazyDataTable(String id, List<? extends IColumn<T, S>> iColumns, IDataProvider dataProvider) {
        super(id, iColumns, dataProvider, Integer.MAX_VALUE);
        setOutputMarkupId(true);
        setVersioned(false);
        addBottomToolbar(new ItemsPerPageToolBar(this));

        if (dataProvider instanceof ISortableDataProvider) {
            addTopToolbar(new AjaxFallbackHeadersToolbar(this, (ISortableDataProvider) dataProvider));
        } else {
            addTopToolbar(new HeadersToolbar(this, null));
        }

        addTopToolbar(new AjaxNavigationToolbar(this));
        addBottomToolbar(new NoRecordsToolbar(this));
        setItemReuseStrategy(new ReuseIfModelsEqualStrategy());
    }


    protected void addHeadersToolbar(IDataProvider dataProvider) {

    }

    @Override
    protected Item newRowItem(final String id, final int index, final IModel model) {
        return new EazyRowItem(id, index, model);
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