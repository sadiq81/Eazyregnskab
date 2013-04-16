package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.FinancePosting;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.FinancePostingModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author
 */
public class FinancePostingDataProvider extends EazyregnskabSortableDataProvider<FinancePosting> {

    @SpringBean
    FinanceAccountService financeAccountService;

    public FinancePostingDataProvider() {
        Injector.get().inject(this);
    }


    @Override
    public Iterator<FinancePosting> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            Iterator<FinancePosting> iterator = financeAccountService.findFinancePostingByDailyLedgerSubList(
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
    public IModel<FinancePosting> model(FinancePosting object) {
        return new FinancePostingModel(object);
    }
}
