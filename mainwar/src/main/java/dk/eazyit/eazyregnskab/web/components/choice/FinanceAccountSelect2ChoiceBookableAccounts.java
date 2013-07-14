package dk.eazyit.eazyregnskab.web.components.choice;

import com.vaynberg.wicket.select2.Select2Choice;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.select2providers.FinanceAccountProviderBookableAccounts;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class FinanceAccountSelect2ChoiceBookableAccounts extends Select2Choice<FinanceAccount> {


    public FinanceAccountSelect2ChoiceBookableAccounts(String id) {
        super(id);
        setProvider(new FinanceAccountProviderBookableAccounts());
        getSettings().setAllowClear(true);
    }

    public FinanceAccountSelect2ChoiceBookableAccounts(String id, IModel<FinanceAccount> model) {
        super(id, model);
        setProvider(new FinanceAccountProviderBookableAccounts());
        getSettings().setAllowClear(true);
    }
}
