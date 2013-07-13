package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.models.lists.VatTypeListModel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

/**
 * @author
 */
public class VatTypeDropDownChoice extends SessionAwareDropDownChoice<VatType> {

    public VatTypeDropDownChoice(String id) {
        super(id);
        setChoices(new VatTypeListModel());
        setChoiceRenderer(new ChoiceRenderer<VatType>("name", "id"));
        setOutputMarkupPlaceholderTag(true);
    }
}
