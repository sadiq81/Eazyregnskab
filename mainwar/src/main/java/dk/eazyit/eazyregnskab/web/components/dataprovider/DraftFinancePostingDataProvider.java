package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.entities.DraftFinancePostingModel;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
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
public class DraftFinancePostingDataProvider extends EazyregnskabSortableDataProvider<DraftFinancePosting> {

    @SpringBean
    FinanceAccountService financeAccountService;

    List<DraftFinancePosting> list;

    private static final Logger LOG = LoggerFactory.getLogger(DraftFinancePostingDataProvider.class);

    public DraftFinancePostingDataProvider() {
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }


    @Override
    public Iterator<DraftFinancePosting> iterator(long first, long count) {

        LOG.debug("Creating iterator of DraftFinancePosting with first " + first + " and count " + count);
        SortParam sortParam = getSort();
        if (sortParam != null) {
            LOG.debug(" and sorting after " + sortParam.getProperty());
            if (list == null) {
                DailyLedger dailyLedger = getCurrentDailyLedger();
                list = financeAccountService.findFinancePostingByDailyLedgerSubListSortBy(dailyLedger, (int) first, (int) count, sortParam.getProperty().toString(), sortParam.isAscending());
            }
            Iterator<DraftFinancePosting> iterator = list.iterator();
            return iterator;
        } else {
            if (list == null) {
                list = financeAccountService.findFinancePostingByDailyLedgerSubList(
                        getCurrentDailyLedger(),
                        (int) first,
                        (int) count);
            }
            return list.iterator();
        }
    }

    @Override
    public long size() {
        int size = financeAccountService.countFinancePostingOfDailyLedger(getCurrentDailyLedger());
        return size;
    }

    @Override
    public IModel model(DraftFinancePosting object) {
        return new DraftFinancePostingModel(object);
    }

    @Override
    public void detach() {
        super.detach();
        list = null;
    }

    @Override
    public void setSort(SortParam<String> param) {
        super.setSort(param);
        list = null;
    }

    @Override
    public void setSort(String property, SortOrder order) {
        super.setSort(property, order);
        list = null;
    }
}
