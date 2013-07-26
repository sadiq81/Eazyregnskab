package dk.eazyit.eazyregnskab.web.components.validators.forms;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.FiscalYearService;
import dk.eazyit.eazyregnskab.services.LoginService;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.session.SessionAware;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author
 */
public abstract class BaseFormValidator extends AbstractFormValidator implements SessionAware {

    private static final long serialVersionUID = 1L;

    private FormComponent<?>[] components;

    @SpringBean
    protected LoginService loginService;
    @SpringBean
    protected FinanceAccountService financeAccountService;
    @SpringBean
    protected FiscalYearService fiscalYearService;

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

    public AppUser getCurrentUser() {
            return ((EazyregnskabSesssion) Session.get()).getCurrentUser();
        }

        public LegalEntity getCurrentLegalEntity() {
            return ((EazyregnskabSesssion) Session.get()).getCurrentLegalEntity();
        }

        public void setCurrentLegalEntity(LegalEntity legalEntity) {
            ((EazyregnskabSesssion) Session.get()).setCurrentLegalEntity(legalEntity);
        }

        public DailyLedger getCurrentDailyLedger() {
            return ((EazyregnskabSesssion) Session.get()).getCurrentDailyLedger();
        }

        public void setCurrentDailyLedger(DailyLedger dailyLedger) {
            ((EazyregnskabSesssion) Session.get()).setCurrentDailyLedger(dailyLedger);
        }



}
