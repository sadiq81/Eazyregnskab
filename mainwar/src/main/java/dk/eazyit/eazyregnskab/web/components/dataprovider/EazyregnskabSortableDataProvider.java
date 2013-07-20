package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.session.SessionAware;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;

/**
 * @author
 */
public abstract class EazyregnskabSortableDataProvider<T> extends SortableDataProvider<T, String> implements SessionAware {

    public EazyregnskabSortableDataProvider() {
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
