package dk.eazyit.eazyregnskab.web.components.link;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author
 */
public abstract class LoadingAjaxLink<T> extends AjaxLink<T> {

    protected LoadingAjaxLink(String id) {
        super(id);
        add(new LoadingBehavior(new StringResourceModel("button.loading",this,null)));
    }

    protected LoadingAjaxLink(String id, IModel<T> model) {
        super(id, model);
        add(new LoadingBehavior(new StringResourceModel("button.loading",this,null)));
    }
}
