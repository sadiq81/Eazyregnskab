package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.FinanceAccountModel;
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
public class FinanceAccountDataProvider extends EazyregnskabSortableDataProvider<FinanceAccount> {

    @SpringBean
    FinanceAccountService financeAccountService;

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountDataProvider.class);

    public FinanceAccountDataProvider() {
        Injector.get().inject(this);
        LOG.trace("creating " + this.getClass().getSimpleName());
    }


    @Override
    public Iterator<FinanceAccount> iterator(long first, long count) {

        LOG.debug("Creating iterator of FinanceAccount with first " + first + " and count " + count);
        SortParam sortParam = getSort();
        if (sortParam != null) {
            LOG.debug(" and sorting after " + sortParam.getProperty());
            LegalEntity legalEntity = getSelectedLegalEntity().getLegalEntityModel().getObject();
                        List<FinanceAccount> list = financeAccountService.findFinanceAccountByLegalEntitySubListSortBy(legalEntity, (int) first, (int) count, sortParam.getProperty().toString(), sortParam.isAscending());
                        Iterator<FinanceAccount> iterator = list.iterator();
                        return iterator;
        } else {
            Iterator<FinanceAccount> iterator = financeAccountService.findFinanceAccountByLegalEntitySubList(
                    getSelectedLegalEntity().getLegalEntityModel().getObject(),
                    (int) first,
                    (int) count).iterator();
            return iterator;
        }

    }

    @Override
    public long size() {
        int size = financeAccountService.countFinanceAccountOfLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject());
        return size;
    }

    @Override
    public IModel<FinanceAccount> model(FinanceAccount object) {
        return new FinanceAccountModel(object);
    }
}
