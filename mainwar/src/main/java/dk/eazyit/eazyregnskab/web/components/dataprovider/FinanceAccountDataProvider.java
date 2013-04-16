package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.FinanceAccountModel;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author
 */
public class FinanceAccountDataProvider extends EazyregnskabSortableDataProvider<FinanceAccount> {

    @SpringBean
    FinanceAccountService financeAccountService;

    public FinanceAccountDataProvider() {
        Injector.get().inject(this);
    }


    @Override
    public Iterator<FinanceAccount> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            return Collections.EMPTY_LIST.iterator();
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
