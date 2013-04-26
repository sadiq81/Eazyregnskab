package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.web.components.models.lists.DailyLedgerListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class DailyLedgerDropDownChoice extends SessionAwareDropDownChoice<DailyLedger> {

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerDropDownChoice.class);

    public DailyLedgerDropDownChoice(String id) {
        super(id);
        setDefaultModel(getCurrentDailyLedger().getDailyLedgerModel());
        setChoices(new DailyLedgerListModel());
        setChoiceRenderer(new ChoiceRenderer<DailyLedger>("name", "id"));

        setOutputMarkupPlaceholderTag(true);

        add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                LOG.debug("Changed DailyLeger to " + getCurrentDailyLedger().getDailyLedgerModel().getObject());
                target.add(getPage());
            }
        }));

    }
}
