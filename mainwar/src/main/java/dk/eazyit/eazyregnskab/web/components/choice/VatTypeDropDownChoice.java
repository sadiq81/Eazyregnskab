package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.models.lists.VatTypeListModel;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class VatTypeDropDownChoice extends SessionAwareDropDownChoice<VatType> {

    private static final Logger LOG = LoggerFactory.getLogger(VatTypeDropDownChoice.class);

    public VatTypeDropDownChoice(String id) {
        super(id);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        setChoices(new VatTypeListModel());
        setChoiceRenderer(new ChoiceRenderer<VatType>("name", "id"));
        setOutputMarkupPlaceholderTag(true);
    }
}
