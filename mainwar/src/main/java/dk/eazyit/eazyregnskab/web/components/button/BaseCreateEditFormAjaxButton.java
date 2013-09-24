package dk.eazyit.eazyregnskab.web.components.button;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class BaseCreateEditFormAjaxButton extends AjaxButton {

    private static final Logger LOG = LoggerFactory.getLogger(BaseCreateEditFormAjaxButton.class);

    public BaseCreateEditFormAjaxButton(String id, IModel<String> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        add(new LoadingBehavior(new StringResourceModel("button.loading", this, null)));
    }

    @Override
    protected void onError(AjaxRequestTarget target, Form<?> form) {
        LOG.debug("error on delete in form " + form.getId() + " with object " + form.getModelObject().toString());
        target.add(form);
        FeedbackPanel fp = (FeedbackPanel) form.getPage().get("feedback");
        if (fp != null) {
            target.add(fp);
        }
    }

}
