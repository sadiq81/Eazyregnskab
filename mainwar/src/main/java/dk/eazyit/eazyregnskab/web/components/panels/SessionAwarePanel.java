package dk.eazyit.eazyregnskab.web.components.panels;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.SessionAware;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public abstract class SessionAwarePanel extends Panel implements SessionAware {

    protected SessionAwarePanel(String id) {
        super(id);
        addToPage();
    }

    protected SessionAwarePanel(String id, IModel<?> model) {
        super(id, model);
        addToPage();
    }

    protected abstract void addToPage();

    public AppUser getCurrentUser() {
        return (AppUser) Session.get().getAttribute(AppUser.ATTRIBUTE_NAME);
    }

    public LegalEntity getCurrentLegalEntity() {
        return (LegalEntity) Session.get().getAttribute(LegalEntity.ATTRIBUTE_NAME);
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        Session.get().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);
        setCurrentDailyLedger(legalEntity.getDailyLedgers().get(0));
    }

    public DailyLedger getCurrentDailyLedger() {
        DailyLedger ledger = (DailyLedger) Session.get().getAttribute(DailyLedger.ATTRIBUTE_NAME);
        if (ledger != null && !getCurrentLegalEntity().getDailyLedgers().contains(ledger)) {
            throw new NullPointerException("Current dailyLedger is not reflecting current LegalEntity");
        }
        return ledger;
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        Session.get().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);
    }
}
