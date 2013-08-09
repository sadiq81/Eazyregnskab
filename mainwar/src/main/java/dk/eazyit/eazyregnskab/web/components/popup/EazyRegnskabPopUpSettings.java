package dk.eazyit.eazyregnskab.web.components.popup;

import org.apache.wicket.markup.html.link.PopupSettings;

/**
 * @author
 */
public class EazyRegnskabPopUpSettings extends PopupSettings {

    public EazyRegnskabPopUpSettings(String windowName) {
        super(windowName, PopupSettings.RESIZABLE | PopupSettings.SCROLLBARS);
        setWidth(800);
        setHeight(600);
    }
}
