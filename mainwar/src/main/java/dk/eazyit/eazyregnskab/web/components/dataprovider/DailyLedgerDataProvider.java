package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.entities.DailyLedgerModel;
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
public class DailyLedgerDataProvider extends EazyregnskabSortableDataProvider<DailyLedger> {

    @SpringBean
    FinanceAccountService financeAccountService;

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerDataProvider.class);


    public DailyLedgerDataProvider() {
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }


    @Override
    public Iterator<DailyLedger> iterator(long first, long count) {

        LOG.debug("Creating iterator of DailyLedger with first " + first + " and count " + count);
        SortParam sortParam = getSort();
        if (sortParam != null) {
            LegalEntity legalEntity = getSelectedLegalEntity().getLegalEntityModel().getObject();
            List<DailyLedger> list = financeAccountService.findDailyLedgerByLegalEntitySubListSortBy(legalEntity, (int) first, (int) count, sortParam.getProperty().toString(), sortParam.isAscending());
            Iterator<DailyLedger> iterator = list.iterator();
            return iterator;
        } else {
            LegalEntity legalEntity = getSelectedLegalEntity().getLegalEntityModel().getObject();
            List<DailyLedger> list = financeAccountService.findDailyLedgerByLegalEntitySubList(legalEntity, (int) first, (int) count);
            Iterator<DailyLedger> iterator = list.iterator();
            return iterator;
        }

    }

    @Override
    public long size() {
        LegalEntity legalEntity = getSelectedLegalEntity().getLegalEntityModel().getObject();
        int size = financeAccountService.countDailyLedgerOfLegalEntity(legalEntity);
        return size;
    }

    @Override
    public IModel<DailyLedger> model(DailyLedger object) {
        return new DailyLedgerModel(object);
    }
}
