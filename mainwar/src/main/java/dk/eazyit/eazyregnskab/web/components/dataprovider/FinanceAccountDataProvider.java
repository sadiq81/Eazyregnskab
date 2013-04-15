package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.app.secure.settings.ChartOfAccountsPage;
import dk.eazyit.eazyregnskab.web.components.models.FinanceAccountModel;
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
public class FinanceAccountDataProvider extends SortableDataProvider<FinanceAccount, String> {

    @SpringBean
    FinanceAccountService financeAccountService;

    LoggedInPage parent;

    public FinanceAccountDataProvider(ChartOfAccountsPage parent) {
        this.parent = parent;
        Injector.get().inject(this);
    }



    @Override
    public Iterator<FinanceAccount> iterator(long first, long count) {

        SortParam sortParam = getSort();
        if (sortParam != null) {
            Iterator<FinanceAccount> iterator = financeAccountService.findFinanceAccountByLegalEntitySubListOrderBy(
                    parent.getSelectedLegalEntity().getLegalEntityModel().getObject(),
                    (int) first,
                    (int) count,
                    (String) sortParam.getProperty(),
                    sortParam.isAscending())
                    .iterator();

            return iterator;
        } else {
            Iterator<FinanceAccount> iterator = financeAccountService.findFinanceAccountByLegalEntitySubList(parent.getSelectedLegalEntity().getLegalEntityModel().getObject(), (int) first, (int) count).iterator();
            return iterator;
        }

    }

    @Override
    public long size() {
        int size = financeAccountService.countFinanceAccountOfLegalEntity(parent.getSelectedLegalEntity().getLegalEntityModel().getObject());
        return size;
    }

    @Override
    public IModel<FinanceAccount> model(FinanceAccount object) {
        return new FinanceAccountModel(object);
    }
}
