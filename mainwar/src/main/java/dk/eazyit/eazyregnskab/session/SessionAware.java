package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;

/**
 * @author
 */
public interface SessionAware {

    AppUser getCurrentUser();

    LegalEntity getCurrentLegalEntity();

    void setCurrentLegalEntity(LegalEntity legalEntity);

    DailyLedger getCurrentDailyLedger();

    void setCurrentDailyLedger(DailyLedger dailyLedger);
}
