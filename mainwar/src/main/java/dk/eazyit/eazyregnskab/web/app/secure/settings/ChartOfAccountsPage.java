package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.FinanceAccountModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.BigDecimalPropertyColumn;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.chart.of.accounts", parentPage = BaseDataPage.class, subLevel = 1)
public class ChartOfAccountsPage extends LoggedInPage {

    @SpringBean
    FinanceAccountService financeAccountService;

    public ChartOfAccountsPage() {
        super();
    }

    public ChartOfAccountsPage(IModel<?> model) {
        super(model);
    }

    public ChartOfAccountsPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        List<IColumn<FinanceAccount, String>> columns = new ArrayList<IColumn<FinanceAccount, String>>();
        columns.add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("name"), "name", "name"));
        columns.add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("accountNumber"), "accountNumber", "accountNumber"));
        columns.add(new BigDecimalPropertyColumn<FinanceAccount>(new ResourceModel("vatType"), "vatType.percentage", "vatType.name"));
        columns.add(new PropertyColumn<FinanceAccount, String>(new ResourceModel("financeAccountType"), "financeAccountType", "financeAccountType"));
        AjaxFallbackDefaultDataTable dataTable = new AjaxFallbackDefaultDataTable("chartOfAccounts", columns, new SortableLegalEntityProvider(), 20);
        add(dataTable);


    }

    public class SortableLegalEntityProvider extends SortableDataProvider<FinanceAccount, String> {
        @Override
        public Iterator<FinanceAccount> iterator(long first, long count) {

            SortParam sortParam = getSort();
            if (sortParam != null) {
                Iterator<FinanceAccount> iterator = financeAccountService.findFinanceAccountByLegalEntitySubListOrderBy(
                        getSelectedLegalEntity().getLegalEntityModel().getObject(),
                        (int) first,
                        (int) count,
                        (String) sortParam.getProperty(),
                        sortParam.isAscending())
                        .iterator();

                return iterator;
            } else {
                Iterator<FinanceAccount> iterator = financeAccountService.findFinanceAccountByLegalEntitySubList(getSelectedLegalEntity().getLegalEntityModel().getObject(), (int) first, (int) count).iterator();
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


}
