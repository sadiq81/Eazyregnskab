package dk.eazyit.eazyregnskab.web.components.choice;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 */
public class EnumDropDownChoice<t extends Enum<t>> extends DropDownChoice<t> {

    public EnumDropDownChoice(String id) {
        super(id);
        setChoiceRenderer(new EnumChoiceRenderer<t>(this));
    }

    public EnumDropDownChoice(String id, IModel<t> model) {
        super(id);
        setModel(model);
        setChoiceRenderer(new EnumChoiceRenderer<t>(this));
    }

    public EnumDropDownChoice(String id, List<t> choices, EnumChoiceRenderer<t> choiceRenderer) {
        super(id, choices);
        setChoiceRenderer(choiceRenderer);
    }


    public EnumDropDownChoice(String id, IModel<t> model, EnumChoiceRenderer<t> choiceRenderer) {
        super(id);
        setModel(model);
        setChoiceRenderer(choiceRenderer);
    }

    public EnumDropDownChoice(String id, IModel<t> model, List<t> choices, EnumChoiceRenderer<t> renderer) {
        super(id, model, choices, renderer);
    }

    @Override
    public List<? extends t> getChoices() {
        t modelObject = getModelObject();
        if (modelObject != null) {
            return Arrays.asList(modelObject.getDeclaringClass().getEnumConstants());
        } else {

            return new ArrayList<t>();
        }
    }
}

