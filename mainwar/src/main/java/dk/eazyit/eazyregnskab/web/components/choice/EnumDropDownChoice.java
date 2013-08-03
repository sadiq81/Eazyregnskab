package dk.eazyit.eazyregnskab.web.components.choice;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public class EnumDropDownChoice<T extends Enum<T>> extends DropDownChoice<T> {

    private static final Logger LOG = LoggerFactory.getLogger(EnumDropDownChoice.class);

    public EnumDropDownChoice(String id) {
        super(id);
        setChoiceRenderer(new EnumChoiceRenderer<T>(this));
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public EnumDropDownChoice(String id, IModel<T> model) {
        super(id);
        setModel(model);
        setChoiceRenderer(new EnumChoiceRenderer<T>(this));
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public EnumDropDownChoice(String id, List<T> choices) {
        super(id, choices);
        setChoiceRenderer(new EnumChoiceRenderer<T>(this));
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public EnumDropDownChoice(String id, List<T> choices, EnumChoiceRenderer<T> choiceRenderer) {
        super(id, choices);
        setChoiceRenderer(choiceRenderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }


    public EnumDropDownChoice(String id, IModel<T> model, EnumChoiceRenderer<T> choiceRenderer) {
        super(id);
        setModel(model);
        setChoiceRenderer(choiceRenderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public EnumDropDownChoice(String id, IModel<T> model, List<T> choices, EnumChoiceRenderer<T> renderer) {
        super(id, model, choices, renderer);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id" + this.getId());
    }

    public void addChoice(List<T> add) {
        List<T> choices = new ArrayList<T>(getChoices());
        choices.addAll(add);
        setChoices(choices);
    }

    public void removeChoice(List<T> remove) {
        List<T> choices = new ArrayList<T>(getChoices());
        choices.removeAll(remove);
        setChoices(choices);
    }
}

