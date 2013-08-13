package dk.eazyit.eazyregnskab.web.components.modal;

/**
 * @author
 */

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

import java.io.Serializable;

public abstract class AreYouSureModal extends SessionAwareModal {

    protected ConfirmationAnswer answer;

    public AreYouSureModal(String id, String modalMessageText) {
        super(id);
        answer = new ConfirmationAnswer(false);
        setContent(new YesNoPanel(getContentId(), modalMessageText, this, answer));
        setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            @Override
            public void onClose(AjaxRequestTarget target) {
                if (answer.isAnswer()) {
                    onConfirm(target);
                } else {
                    onCancel(target);
                }
            }
        });
        setInitialWidth(260).setMinimalWidth(260).setInitialHeight(170).setMinimalHeight(170).setResizable(false);
    }


    protected abstract void onConfirm(AjaxRequestTarget target);

    protected abstract void onCancel(AjaxRequestTarget target);

    public class YesNoPanel extends Panel {

        public YesNoPanel(String id, String message, final ModalWindow modalWindow, final ConfirmationAnswer answer) {
            super(id);

            Form yesNoForm = new Form("yesNoForm");

            MultiLineLabel messageLabel = new MultiLineLabel("message", message);
            yesNoForm.add(messageLabel);


            AjaxButton yesButton = new AjaxButton("yesButton", new ResourceModel("button.yes"), yesNoForm) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                    if (target != null) {
                        answer.setAnswer(true);
                        modalWindow.close(target);
                    }
                }
            };

            AjaxButton noButton = new AjaxButton("noButton", new ResourceModel("button.no"), yesNoForm) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form form) {
                    if (target != null) {
                        answer.setAnswer(false);
                        modalWindow.close(target);
                    }
                }
            };

            yesNoForm.add(yesButton);
            yesNoForm.add(noButton);
            add(yesNoForm);
        }
    }

    public class ConfirmationAnswer implements Serializable {

        private boolean answer;

        public ConfirmationAnswer(boolean answer) {
            this.answer = answer;
        }

        public boolean isAnswer() {
            return answer;
        }

        public void setAnswer(boolean answer) {
            this.answer = answer;
        }
    }
}