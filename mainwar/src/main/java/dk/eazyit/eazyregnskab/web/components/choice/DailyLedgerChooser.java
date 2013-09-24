package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.web.components.models.entities.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.models.lists.DailyLedgerListModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class DailyLedgerChooser extends SessionAwareDropDownChoice<DailyLedger> {

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerChooser.class);

    public DailyLedgerChooser(String id) {
        super(id);
        setDefaultModel(new DailyLedgerModel(getCurrentDailyLedger()));
        setChoices(new DailyLedgerListModel());
        setChoiceRenderer(new ChoiceRenderer<DailyLedger>("name", "id"));
        setOutputMarkupPlaceholderTag(true);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
        add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                DailyLedger changed = getConvertedInput();
                LOG.debug("Changed DailyLeger to " + changed);
                getSession().setAttribute(DailyLedger.ATTRIBUTE_NAME, changed);
                target.add(getPage());
            }
        }));

    }


}
