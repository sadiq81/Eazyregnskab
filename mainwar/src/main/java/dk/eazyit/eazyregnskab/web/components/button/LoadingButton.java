package dk.eazyit.eazyregnskab.web.components.button;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.LoadingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author
 */
public class LoadingButton extends Button {

    public LoadingButton(String id) {
        super(id);
        init();
    }

    public LoadingButton(String id, IModel<String> model) {
        super(id, model);
        init();
    }

    private void init(){
        add(new LoadingBehavior(new StringResourceModel("button.loading",this,null)));
    }


}
