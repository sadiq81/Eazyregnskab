package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.FinanceAccountType;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author
 */
public class FinanceAccountTypeDropDownChoice extends EnumDropDownChoice<FinanceAccountType> {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceAccountTypeDropDownChoice.class);

    public FinanceAccountTypeDropDownChoice(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public FinanceAccountTypeDropDownChoice(String id, IModel<FinanceAccountType> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    public FinanceAccountTypeDropDownChoice(String id, List<FinanceAccountType> choices) {
        super(id, choices);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public FinanceAccountTypeDropDownChoice(String id, List<FinanceAccountType> choices, EnumChoiceRenderer<FinanceAccountType> choiceRenderer) {
        super(id, choices, choiceRenderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public FinanceAccountTypeDropDownChoice(String id, IModel<FinanceAccountType> model, EnumChoiceRenderer<FinanceAccountType> choiceRenderer) {
        super(id, model, choiceRenderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    public FinanceAccountTypeDropDownChoice(String id, IModel<FinanceAccountType> model, List<FinanceAccountType> choices, EnumChoiceRenderer<FinanceAccountType> renderer) {
        super(id, model, choices, renderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        if (((FinanceAccount) getForm().getModelObject()).isSystemAccount()) {
            addChoice(FinanceAccountType.getSystemAccounts());
        } else {
            removeChoice(FinanceAccountType.getSystemAccounts());
        }
    }
}
