package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.*;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.session.SessionAware;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author
 */
public abstract class SessionAwareForm<T> extends Form<T> implements SessionAware {


    @SpringBean
    protected LegalEntityService legalEntityService;
    @SpringBean
    protected FinanceAccountService financeAccountService;
    @SpringBean
    protected DailyLedgerService dailyLedgerService;
    @SpringBean
    protected FiscalYearService fiscalYearService;
    @SpringBean
    protected VatTypeService vatTypeService;

    protected final static int DURATION = 5;
    protected final static Double DOUBLE_ZERO = new Double(0);

    public SessionAwareForm(String id) {
        super(id);
    }

    public SessionAwareForm(String id, IModel<T> model) {
        super(id, model);
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

    protected abstract void configureComponents();
}
