package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author
 */
public class EazyregnskabSesssion extends WebSession {

    @SpringBean
    FinanceAccountService financeAccountService;

    public EazyregnskabSesssion(Request request) {
        super(request);
    }

    public AppUser getCurrentUser() {
        return (AppUser) Session.get().getAttribute(AppUser.ATTRIBUTE_NAME);
    }

    public LegalEntity getCurrentLegalEntity() {
        return (LegalEntity) Session.get().getAttribute(LegalEntity.ATTRIBUTE_NAME);
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        Session.get().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);
        setCurrentDailyLedger(financeAccountService.findDailyLedgerByLegalEntity(legalEntity).get(0));
    }

    public DailyLedger getCurrentDailyLedger() {
        DailyLedger ledger = (DailyLedger) Session.get().getAttribute(DailyLedger.ATTRIBUTE_NAME);
        if (ledger != null && !(ledger.getLegalEntity().equals(getCurrentLegalEntity()))) {
            throw new NullPointerException("Current dailyLedger is not reflecting current LegalEntity");
        }
        return ledger;
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        Session.get().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);
    }
}
