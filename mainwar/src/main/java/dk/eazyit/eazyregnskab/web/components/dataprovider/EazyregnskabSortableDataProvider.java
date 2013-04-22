package dk.eazyit.eazyregnskab.web.components.dataprovider;

import dk.eazyit.eazyregnskab.session.CurrentDailyLedger;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import dk.eazyit.eazyregnskab.session.CurrentUser;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class EazyregnskabSortableDataProvider<T> extends SortableDataProvider<T, String> {

    public EazyregnskabSortableDataProvider() {
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
