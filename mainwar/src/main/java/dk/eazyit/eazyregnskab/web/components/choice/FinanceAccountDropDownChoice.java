package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author
 */
public class FinanceAccountDropDownChoice<T> extends DropDownChoice<T> {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountDropDownChoice.class);

    public FinanceAccountDropDownChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        setRequired(isSumAccount());
        setVisibilityAllowed(isSumAccount());
    }

    public boolean isSumAccount() {
        return ((FinanceAccount) getForm().getModelObject()).getFinanceAccountType() == FinanceAccountType.SUM;
    }
}
