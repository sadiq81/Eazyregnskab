package dk.eazyit.eazyregnskab.web.components.choice;

import com.vaynberg.wicket.select2.Select2Choice;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.select2providers.FinanceAccountProvider;

/**
 * @author
 */
public class FinanceAccountSelect2Choice extends Select2Choice<FinanceAccount> {


    public FinanceAccountSelect2Choice(String id) {
        super(id);
        setProvider(new FinanceAccountProvider());
        getSettings().setAllowClear(true);
    }
}
