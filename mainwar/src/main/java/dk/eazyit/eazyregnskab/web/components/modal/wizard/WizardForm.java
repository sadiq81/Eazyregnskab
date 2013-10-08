package dk.eazyit.eazyregnskab.web.components.modal.wizard;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class WizardForm<T> extends Form<T> {

    private static final String FEEDBACK_ID = "feedback";

    WizardModal modal;
    NotificationPanel feedback;

    StepPanel current;
    List<StepPanel> panelList = new ArrayList<>();

    AjaxButton previous;
    AjaxButton next;
    AjaxButton cancel;
    AjaxButton finish;

    WizardForm(String id, WizardModal modal) {
        super(id);
        this.modal = modal;

        setOutputMarkupPlaceholderTag(true);
        add(feedback = (NotificationPanel) new NotificationPanel(FEEDBACK_ID).setOutputMarkupPlaceholderTag(true));
        add(previous = (AjaxButton) new StepButton("previous").setDefaultFormProcessing(false));
        add(next = new StepButton("next"));
        add(finish = new StepButton("finish"));
        add(cancel = (AjaxButton) new StepButton("cancel").setDefaultFormProcessing(false));
    }

    public void buttonPressed(String id, AjaxRequestTarget target) {
        switch (id) {
            case "previous": {
                target.add(addOrReplace(current, current = getPrevious()));
//                target.appendJavaScript("window.parent.Wicket.Window.current.autoSizeWindow();");
                break;
            }
            case "next": {
                current.onNext(target);
                target.add(addOrReplace(current, current = getNext()));

//                target.appendJavaScript("window.parent.Wicket.Window.current.autoSizeWindow();");
                break;
            }
            case "cancel": {
                modal.close(target);
                break;
            }
            case "finish": {
                modal.close(target);
                break;
            }
            default: {
                throw new IllegalArgumentException("button not configured");
            }
        }
    }

    protected StepPanel getNext() {
        return panelList.get(panelList.indexOf(current) + 1);
    }

    protected StepPanel getPrevious() {
        return panelList.get(panelList.indexOf(current) - 1);
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        int index = panelList.indexOf(current);
        previous.setVisibilityAllowed(current.isPreviousAvailable() && index > 0);
        next.setVisibilityAllowed(current.isNextAvailable() && index < panelList.size() - 1);
        finish.setVisibilityAllowed(current.isFinishAvailable() && panelList.get(panelList.size() - 1) == current);
    }

    public void addPanel(StepPanel panel) {
        if (panelList.size() == 0) add(current = panel);
        panelList.add(panel);
    }

    public void reset() {
        addOrReplace(current, current = panelList.get(0));
    }

    class StepButton extends AjaxButton {
        StepButton(String id) {
            super(id);
        }

        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            super.onSubmit(target, form);
            target.add(feedback, form);
            buttonPressed(this.getId(), target);
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            super.onError(target, form);
            target.add(feedback, form);
            target.appendJavaScript("$('html, body').animate({ scrollTop: 0 }, 'slow');");
        }
    }
}