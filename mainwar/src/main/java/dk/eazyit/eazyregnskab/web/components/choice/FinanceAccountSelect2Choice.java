package dk.eazyit.eazyregnskab.web.components.choice;

import com.vaynberg.wicket.select2.Select2Choice;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.select2providers.FinanceAccountProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;

/**
 * @author
 */
public class FinanceAccountSelect2Choice extends Select2Choice<FinanceAccount> {


    public FinanceAccountSelect2Choice(String id) {
        super(id);
        setProvider(new FinanceAccountProvider());
        getSettings().setAllowClear(true);
    }

    public FinanceAccountSelect2Choice(String id, final FinanceAccountSelect2Choice reverseFinanceAccountChoice, final VatTypeDropDownChoice vatTypeChoice) {
        super(id);
        setProvider(new FinanceAccountProvider());
        getSettings().setAllowClear(true);
        add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                FinanceAccount financeAccount = (FinanceAccount) getFormComponent().getModelObject();
                FinanceAccount reverse = financeAccount.getStandardReverseFinanceAccount();
                VatType vatType = financeAccount.getVatType();
                if (reverse != null) {
                    reverseFinanceAccountChoice.setModelObject(reverse);
                    target.add(reverseFinanceAccountChoice);
                }
                if (vatType != null) {
                    vatTypeChoice.setModelObject(vatType);
                    target.add(vatTypeChoice);
                }
            }
        });

    }
}
