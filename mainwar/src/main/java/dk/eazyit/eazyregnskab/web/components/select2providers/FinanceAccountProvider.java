package dk.eazyit.eazyregnskab.web.components.select2providers;

import com.vaynberg.wicket.select2.Response;
import com.vaynberg.wicket.select2.TextChoiceProvider;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author
 */
public class FinanceAccountProvider extends TextChoiceProvider<FinanceAccount> {

    @SpringBean
    private FinanceAccountService financeAccountService;


    public FinanceAccountProvider() {
        Injector.get().inject(this);
    }

    @Override
    protected String getDisplayText(FinanceAccount choice) {
        return choice.getAccountNumber() + " " + choice.getName();
    }

    @Override
    protected Object getId(FinanceAccount choice) {
        return choice.getId();
    }

    @Override
    public void query(String term, int page, Response<FinanceAccount> response) {
        response.addAll(queryMatches(term, page, 10));
        response.setHasMore(response.size() == 10);
    }

    @Override
    public Collection<FinanceAccount> toChoices(Collection<String> ids) {
        ArrayList<FinanceAccount> list = new ArrayList<FinanceAccount>();

        for (String id : ids) {
            list.add(financeAccountService.findFinanceAccountById(Long.parseLong(id)));
        }
        return list;

    }

    private List<FinanceAccount> queryMatches(String term, int page, int pageSize) {

        List<FinanceAccount> result = new ArrayList<FinanceAccount>();

        List<FinanceAccount> list = financeAccountService.findFinanceAccountByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject());

        term = term.toUpperCase();

        final int offset = page * pageSize;

        int matched = 0;
        for (FinanceAccount financeAccount : list) {
            if (result.size() == pageSize) {
                break;
            }

            if (financeAccount.getName().toUpperCase().contains(term) || financeAccount.getAccountNumber().toString().toUpperCase().contains(term)) {
                matched++;
                if (matched > offset) {
                    result.add(financeAccount);
                }
            }
        }
        return result;
    }


    protected CurrentLegalEntity getSelectedLegalEntity() {
        return (CurrentLegalEntity) Session.get().getAttribute(CurrentLegalEntity.ATTRIBUTE_NAME);
    }

}
