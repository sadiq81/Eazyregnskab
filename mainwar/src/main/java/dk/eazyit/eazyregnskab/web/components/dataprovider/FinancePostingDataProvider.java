package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.FinancePosting;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.app.secure.bookkeeping.BookkeepingPage;
import dk.eazyit.eazyregnskab.web.components.models.FinancePostingModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;

/**
 * @author
 */
public class FinancePostingDataProvider extends SortableDataProvider<FinancePosting, String> {

    @SpringBean
    FinanceAccountService financeAccountService;

    BookkeepingPage parent;

    public FinancePostingDataProvider(BookkeepingPage parent) {
        this.parent = parent;
        Injector.get().inject(this);
    }


    @Override
    public Iterator<FinancePosting> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            Iterator<FinancePosting> iterator = financeAccountService.findFinancePostingByDailyLedgerSubListOrderBy(
                    parent.getDailyLedger(),
                    (int) first,
                    (int) count,
                    (String) sortParam.getProperty(),
                    sortParam.isAscending())
                    .iterator();

            return iterator;
        } else {
            Iterator<FinancePosting> iterator = financeAccountService.findFinancePostingByDailyLedgerSubList(
                    parent.getDailyLedger(),
                    (int) first,
                    (int) count).iterator();

            return iterator;
        }

    }

    @Override
    public long size() {
        int size = financeAccountService.countFinancePostingOfDailyLedger(parent.getDailyLedger());
        return size;
    }

    @Override
    public IModel<FinancePosting> model(FinancePosting object) {
        return new FinancePostingModel(object);
    }
}
