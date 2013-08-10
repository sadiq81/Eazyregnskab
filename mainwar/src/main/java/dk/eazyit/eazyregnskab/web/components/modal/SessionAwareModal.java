package dk.eazyit.eazyregnskab.web.components.modal;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class SessionAwareModal<T> extends ModalWindow {


    public SessionAwareModal(String id) {
        super(id);
    }

    public SessionAwareModal(String id, IModel<T> model) {
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

    @Override
    public String getCssClassName() {
        return CSS_CLASS_GRAY;
    }

    protected Component addToolTipToComponent(Component component, String text) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", text));
        return component;
    }
}
