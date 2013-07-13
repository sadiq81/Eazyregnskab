package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.LoginService;
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

    protected AppUser getCurrentUser() {
        return (AppUser) Session.get().getAttribute(AppUser.ATTRIBUTE_NAME);
    }

    protected LegalEntity getCurrentLegalEntity() {
        return (LegalEntity) Session.get().getAttribute(LegalEntity.ATTRIBUTE_NAME);
    }

    protected void setCurrentLegalEntity(LegalEntity legalEntity) {
        Session.get().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);
    }

    protected DailyLedger getCurrentDailyLedger() {
        return (DailyLedger) Session.get().getAttribute(DailyLedger.ATTRIBUTE_NAME);
    }

    protected void setCurrentDailyLedger(DailyLedger dailyLedger) {
        Session.get().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);
    }

}
