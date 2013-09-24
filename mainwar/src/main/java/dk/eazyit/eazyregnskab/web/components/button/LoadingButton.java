package dk.eazyit.eazyregnskab.web.components.button;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class LoadingButton extends Button {


    private static final Logger LOG = LoggerFactory.getLogger(LoadingButton.class);

    public LoadingButton(String id) {
        super(id);
        init();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public LoadingButton(String id, IModel<String> model) {
        super(id, model);
        init();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);

    }

    private void init() {
        add(new LoadingBehavior(new StringResourceModel("button.loading", this, null)));
    }


}
