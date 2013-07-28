package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.web.components.models.entities.LegalEntityModel;
import dk.eazyit.eazyregnskab.web.components.models.lists.LegalEntityListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class LegalEntityChooser extends SessionAwareDropDownChoice<LegalEntity> {

    static final Logger LOG = LoggerFactory.getLogger(LegalEntityChooser.class);

    public LegalEntityChooser(String id) {
        super(id);
        setDefaultModel(new LegalEntityModel(getCurrentLegalEntity()));
        setChoices(new LegalEntityListModel());
        setChoiceRenderer(new ChoiceRenderer<LegalEntity>("name", "id"));
        setOutputMarkupPlaceholderTag(true);
        add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                LegalEntity changed = getConvertedInput();
                LOG.debug("Changed legal entity selections to " + changed);
                getSession().setAttribute(LegalEntity.ATTRIBUTE_NAME, changed);
                getSession().setAttribute(DailyLedger.ATTRIBUTE_NAME, dailyLedgerService.findDailyLedgerByLegalEntity(changed).get(0));
                target.add(getPage());
            }
        }));
    }
}
