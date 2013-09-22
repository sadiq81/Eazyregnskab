package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.BaseEntity;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import dk.eazyit.eazyregnskab.session.SessionAware;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * @author
 */
public abstract class BaseForm<T extends BaseEntity> extends Form<T> implements SessionAware {

    protected BaseForm(String id) {
        super(id);
    }

    protected BaseForm(String id, IModel<T> model) {
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

    protected void addToolTipToComponent(Component component, String resourceText) {
        component.add(AttributeModifier.append("rel", "tooltip"));
        component.add(AttributeModifier.append("data-placement", "top"));
        component.add(AttributeModifier.append("data-original-title", new ResourceModel(resourceText)));
    }

    protected void removeToolTipToComponent(Component component) {
        component.add(AttributeModifier.remove("rel"));
        component.add(AttributeModifier.remove("data-placement"));
        component.add(AttributeModifier.remove("data-original-title"));
    }
}
