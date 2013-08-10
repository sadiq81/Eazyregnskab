package dk.eazyit.eazyregnskab.web.components.popup;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * @author
 */
public class NotificationPopUp extends WebPage {

    public NotificationPopUp(String text, int hideAfterSeconds) {
        add(new Label("info", text));
    }
}
