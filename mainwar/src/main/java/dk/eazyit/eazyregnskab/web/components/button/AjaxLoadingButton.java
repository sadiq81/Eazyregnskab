package dk.eazyit.eazyregnskab.web.components.button;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class AjaxLoadingButton extends AjaxButton {

    private static final Logger LOG = LoggerFactory.getLogger(AjaxButton.class);

    public AjaxLoadingButton(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        init();
    }

    public AjaxLoadingButton(String id, IModel<String> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and this model " + model);
        init();
    }

    private void init() {
        add(new LoadingBehavior(new StringResourceModel("button.loading", this, null)));
    }


}
