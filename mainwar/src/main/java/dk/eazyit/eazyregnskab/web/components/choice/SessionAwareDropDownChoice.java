package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.session.CurrentDailyLedger;
import dk.eazyit.eazyregnskab.session.CurrentLegalEntity;
import dk.eazyit.eazyregnskab.session.CurrentUser;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * @author
 */
public class SessionAwareDropDownChoice<T> extends DropDownChoice<T> {

    public SessionAwareDropDownChoice(String id) {
        super(id);
    }

    public SessionAwareDropDownChoice(String id, List<? extends T> choices) {
        super(id, choices);
    }

    public SessionAwareDropDownChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
    }

    public SessionAwareDropDownChoice(String id, IModel<T> model, List<? extends T> choices) {
        super(id, model, choices);
    }

    public SessionAwareDropDownChoice(String id, IModel<T> model, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
    }

    public SessionAwareDropDownChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, choices);
    }

    public SessionAwareDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices) {
        super(id, model, choices);
    }

    public SessionAwareDropDownChoice(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
    }

    public SessionAwareDropDownChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
    }

    protected CurrentUser getCurrentUser() {
        return (CurrentUser) getSession().getAttribute(CurrentUser.ATTRIBUTE_NAME);
    }

    protected CurrentLegalEntity getSelectedLegalEntity() {
        return (CurrentLegalEntity) getSession().getAttribute(CurrentLegalEntity.ATTRIBUTE_NAME);
    }

    protected CurrentDailyLedger getCurrentDailyLedger() {
        return (CurrentDailyLedger) getSession().getAttribute(CurrentDailyLedger.ATTRIBUTE_NAME);
    }
}
