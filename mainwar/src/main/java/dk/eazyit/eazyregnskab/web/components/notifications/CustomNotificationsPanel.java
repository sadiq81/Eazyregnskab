package dk.eazyit.eazyregnskab.web.components.notifications;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

/**
 * @author
 */
public class CustomNotificationsPanel extends NotificationPanel {

    public CustomNotificationsPanel(String id) {
        super(id);
    }

    public CustomNotificationsPanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
    }


}
