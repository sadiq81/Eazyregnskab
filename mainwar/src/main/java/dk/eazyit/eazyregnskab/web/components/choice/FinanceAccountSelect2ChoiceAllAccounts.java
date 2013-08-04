package dk.eazyit.eazyregnskab.web.components.choice;

import com.vaynberg.wicket.select2.Select2Choice;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.select2providers.FinanceAccountProviderAllAcccounts;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class FinanceAccountSelect2ChoiceAllAccounts extends Select2Choice<FinanceAccount> {


    public FinanceAccountSelect2ChoiceAllAccounts(String id) {
        super(id);
        setProvider(new FinanceAccountProviderAllAcccounts());
        getSettings().setAllowClear(true);
    }

    public FinanceAccountSelect2ChoiceAllAccounts(String id, IModel<FinanceAccount> model) {
        super(id, model);
        setProvider(new FinanceAccountProviderAllAcccounts());
        getSettings().setAllowClear(true);
    }


}
