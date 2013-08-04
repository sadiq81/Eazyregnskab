package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.services.PostingService;
import dk.eazyit.eazyregnskab.web.components.models.entities.DraftFinancePostingModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author
 */
public class FinancePostingDataProvider extends EazyregnskabSortableDataProvider<DraftFinancePosting> {

    @SpringBean
    PostingService postingService;

    private static final Logger LOG = LoggerFactory.getLogger(FinancePostingDataProvider.class);

    public FinancePostingDataProvider() {
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }


    @Override
    public Iterator<DraftFinancePosting> iterator(long first, long count) {

        LOG.debug("Creating iterator of DraftFinancePosting with first " + first + " and count " + count);
        SortParam sortParam = getSort();
        if (sortParam != null) {
            LOG.debug(" and sorting after " + sortParam.getProperty());
            DailyLedger dailyLedger = getCurrentDailyLedger();
            List<DraftFinancePosting> list = postingService.findDraftPostingByDailyLedgerSubListSortBy(dailyLedger, (int) first, (int) count, sortParam.getProperty().toString(), sortParam.isAscending());
            Iterator<DraftFinancePosting> iterator = list.iterator();
            return iterator;
        } else {
            Iterator<DraftFinancePosting> iterator = postingService.findDraftPostingByDailyLedgerSubList(
                    getCurrentDailyLedger(),
                    (int) first,
                    (int) count).iterator();
            return iterator;
        }
    }

    @Override
    public long size() {
        int size = postingService.countDraftFinancePostingOfDailyLedger(getCurrentDailyLedger());
        return size;
    }

    @Override
    public IModel<DraftFinancePosting> model(DraftFinancePosting object) {
        return new DraftFinancePostingModel(object);
    }
}
