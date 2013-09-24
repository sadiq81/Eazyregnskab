package dk.eazyit.eazyregnskab.web.components.choice;

import com.vaynberg.wicket.select2.Select2Choice;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.select2providers.FinanceAccountProviderAllAcccounts;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class FinanceAccountSelect2ChoiceAllAccounts extends Select2Choice<FinanceAccount> {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountSelect2ChoiceAllAccounts.class);

    public FinanceAccountSelect2ChoiceAllAccounts(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        setProvider(new FinanceAccountProviderAllAcccounts());
        getSettings().setAllowClear(true);
    }

    public FinanceAccountSelect2ChoiceAllAccounts(String id, IModel<FinanceAccount> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
        setProvider(new FinanceAccountProviderAllAcccounts());
        getSettings().setAllowClear(true);
    }


}
