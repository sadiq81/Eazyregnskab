package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author
 */
public class DailyLedgerDataProvider extends EazyregnskabSortableDataProvider<DailyLedger> {

    @SpringBean
    FinanceAccountService financeAccountService;

    public DailyLedgerDataProvider() {
        Injector.get().inject(this);
    }


    @Override
    public Iterator<DailyLedger> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            return Collections.EMPTY_LIST.iterator();
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
