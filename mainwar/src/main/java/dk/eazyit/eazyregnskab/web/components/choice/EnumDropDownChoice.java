package dk.eazyit.eazyregnskab.web.components.choice;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author
 */
public class EnumDropDownChoice<t extends Enum<t>> extends DropDownChoice<t> {

    private static final Logger LOG = LoggerFactory.getLogger(EnumDropDownChoice.class);

    public EnumDropDownChoice(String id) {
        super(id);
        setChoiceRenderer(new EnumChoiceRenderer<t>(this));
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());

    }

    public EnumDropDownChoice(String id, IModel<t> model) {
        super(id);
        setModel(model);
        setChoiceRenderer(new EnumChoiceRenderer<t>(this));
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public EnumDropDownChoice(String id, List<t> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer<t>(this));
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public EnumDropDownChoice(String id, List<t> choices, EnumChoiceRenderer<t> choiceRenderer) {
        super(id, choices);
        setChoiceRenderer(choiceRenderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }


    public EnumDropDownChoice(String id, IModel<t> model, EnumChoiceRenderer<t> choiceRenderer) {
        super(id);
        setModel(model);
        setChoiceRenderer(choiceRenderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public EnumDropDownChoice(String id, IModel<t> model, List<t> choices, EnumChoiceRenderer<t> renderer) {
        super(id, model, choices, renderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }
}

