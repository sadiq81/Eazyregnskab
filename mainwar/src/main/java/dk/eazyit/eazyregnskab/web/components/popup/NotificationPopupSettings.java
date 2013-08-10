package dk.eazyit.eazyregnskab.web.components.popup;

import org.apache.wicket.markup.html.link.PopupSettings;

/**
 * @author
 */
public class NotificationPopupSettings extends PopupSettings {

    public NotificationPopupSettings(String windowName) {
        super(windowName);
        setHeight(100);
        setWidth(300);
    }
}
