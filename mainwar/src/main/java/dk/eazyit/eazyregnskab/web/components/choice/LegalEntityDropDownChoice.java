package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class LegalEntityDropDownChoice extends SessionAwareDropDownChoice<LegalEntity> {

    LoggedInPage parent;
    static final Logger LOG = LoggerFactory.getLogger(LegalEntityDropDownChoice.class);

    public LegalEntityDropDownChoice(String id, final LoggedInPage parent) {
        super(id);
        this.parent = parent;
        setDefaultModel(getSelectedLegalEntity().getLegalEntityModel());
        setChoices(legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()));
        setChoiceRenderer(new ChoiceRenderer<LegalEntity>("name", "id"));
        setOutputMarkupPlaceholderTag(true);
        add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                LOG.debug("Changed legal entity selections");
                parent.changedLegalEntity(target);
                target.add(getPage());
            }
        }));
    }


}
