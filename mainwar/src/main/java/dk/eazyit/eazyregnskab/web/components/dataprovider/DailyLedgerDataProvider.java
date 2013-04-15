package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;

/**
 * @author
 */
public class DailyLedgerDataProvider extends SortableDataProvider<DailyLedger, String> {

    @SpringBean
    FinanceAccountService financeAccountService;

    LoggedInPage parent;

    public DailyLedgerDataProvider(LoggedInPage parent) {
        this.parent = parent;
        Injector.get().inject(this);
    }


    @Override
    public Iterator<DailyLedger> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            Iterator<DailyLedger> iterator = financeAccountService.findDailyLedgerByLegalEntitySubListOrderBy(
                    parent.getSelectedLegalEntity().getLegalEntityModel().getObject(),
                    (int) first,
                    (int) count,
                    (String) sortParam.getProperty(),
                    sortParam.isAscending())
                    .iterator();

            return iterator;
        } else {
            Iterator<DailyLedger> iterator = financeAccountService.findDailyLedgerByLegalEntitySubList(parent.getSelectedLegalEntity().getLegalEntityModel().getObject(), (int) first, (int) count).iterator();
            return iterator;
        }

    }

    @Override
    public long size() {
        int size = financeAccountService.countDailyLedgerOfLegalEntity(parent.getSelectedLegalEntity().getLegalEntityModel().getObject());
        return size;
    }

    @Override
    public IModel<DailyLedger> model(DailyLedger object) {
        return new DailyLedgerModel(object);
    }
}
