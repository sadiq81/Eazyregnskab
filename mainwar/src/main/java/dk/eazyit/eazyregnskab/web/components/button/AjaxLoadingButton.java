package dk.eazyit.eazyregnskab.web.components.button;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author
 */
public class AjaxLoadingButton extends AjaxButton{

    public AjaxLoadingButton(String id) {
        super(id);
        init();
    }

    public AjaxLoadingButton(String id, IModel<String> model) {
        super(id, model);
        init();
    }

    private void init(){
        add(new LoadingBehavior(new StringResourceModel("button.loading",this,null)));
    }


}
