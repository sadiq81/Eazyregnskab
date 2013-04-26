package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.session.CurrentDailyLedger;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import dk.eazyit.eazyregnskab.session.CurrentUser;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author
 */
public abstract class BaseFormValidator extends AbstractFormValidator {

    private static final long serialVersionUID = 1L;

    private FormComponent<?>[] components;

    @SpringBean
    protected LoginService loginService;
    @SpringBean
    protected VatTypeDAO vatTypeDAO;
    @SpringBean
    protected DailyLedgerDAO dailyLedgerDAO;

    protected BaseFormValidator(FormComponent... components) {

        this.components = new FormComponent[components.length];

        for (int i = 0; i < components.length; i++) {
            this.components[i] = components[i];
        }
    }

    /**
     * @see org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
     */
    @Override
    public FormComponent<?>[] getDependentFormComponents() {
        return components;
    }

    protected CurrentUser getCurrentUser() {
        return (CurrentUser) Session.get().getAttribute(CurrentUser.ATTRIBUTE_NAME);
    }

    protected CurrentLegalEntity getSelectedLegalEntity() {
        return (CurrentLegalEntity) Session.get().getAttribute(CurrentLegalEntity.ATTRIBUTE_NAME);
    }

    protected CurrentDailyLedger getCurrentDailyLedger() {
        return (CurrentDailyLedger) Session.get().getAttribute(CurrentDailyLedger.ATTRIBUTE_NAME);
    }

}
