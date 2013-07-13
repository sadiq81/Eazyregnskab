package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * @author
 */
public class SessionAwareDropDownChoice<T> extends DropDownChoice<T> {

    @SpringBean
    protected LegalEntityService legalEntityService;

    @SpringBean
    protected FinanceAccountService financeAccountService;

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

    protected AppUser getCurrentUser() {
        return (AppUser) getSession().getAttribute(AppUser.ATTRIBUTE_NAME);
    }

    protected LegalEntity getCurrentLegalEntity() {
        return (LegalEntity) getSession().getAttribute(LegalEntity.ATTRIBUTE_NAME);
    }

    protected void setCurrentLegalEntity(LegalEntity legalEntity) {
        getSession().setAttribute(LegalEntity.ATTRIBUTE_NAME, legalEntity);
        setCurrentDailyLedger(legalEntity.getDailyLedgers().get(0));
    }

    protected DailyLedger getCurrentDailyLedger() {
        return (DailyLedger) getSession().getAttribute(DailyLedger.ATTRIBUTE_NAME);
    }

    protected void setCurrentDailyLedger(DailyLedger dailyLedger) {
        getSession().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedger);
    }
}
