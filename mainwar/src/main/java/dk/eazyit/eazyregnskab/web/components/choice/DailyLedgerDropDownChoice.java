package dk.eazyit.eazyregnskab.web.components.choice;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class DailyLedgerDropDownChoice extends SessionAwareDropDownChoice<DailyLedger> {

    @SpringBean
    FinanceAccountService financeAccountService;

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerDropDownChoice.class);

    public DailyLedgerDropDownChoice(String id) {
        super(id);
        setDefaultModel(getCurrentDailyLedger().getDailyLedgerModel());
        setChoices(financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()));
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
