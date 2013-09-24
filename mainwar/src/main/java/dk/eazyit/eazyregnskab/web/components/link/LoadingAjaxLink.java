package dk.eazyit.eazyregnskab.web.components.link;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class LoadingAjaxLink<T> extends AjaxLink<T> {

    private static final Logger LOG = LoggerFactory.getLogger(LoadingAjaxLink.class);

    protected LoadingAjaxLink(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        add(new LoadingBehavior(new StringResourceModel("button.loading", this, null)));
    }

    protected LoadingAjaxLink(String id, IModel<T> model) {
        super(id, model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model);
        add(new LoadingBehavior(new StringResourceModel("button.loading", this, null)));
    }
}
