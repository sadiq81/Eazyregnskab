package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.DraftFinancePostingModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author
 */
public class FinancePostingDataProvider extends EazyregnskabSortableDataProvider<DraftFinancePosting> {

    @SpringBean
    FinanceAccountService financeAccountService;

    public FinancePostingDataProvider() {
        Injector.get().inject(this);
    }


    @Override
    public Iterator<DraftFinancePosting> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            Iterator<DraftFinancePosting> iterator = financeAccountService.findFinancePostingByDailyLedgerSubList(
                    getCurrentDailyLedger().getDailyLedgerModel().getObject(),
                    (int) first,
                    (int) count).iterator();
            return iterator;
        }
    }

    @Override
    public long size() {
        int size = financeAccountService.countFinancePostingOfDailyLedger(getCurrentDailyLedger().getDailyLedgerModel().getObject());
        return size;
    }

    @Override
    public IModel<DraftFinancePosting> model(DraftFinancePosting object) {
        return new DraftFinancePostingModel(object);
    }
}
