package dk.eazyit.eazyregnskab.web.components.panels.action;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.DailyLedgerModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class DailyLedgerActionPanel extends ActionPanel<DailyLedger> {

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerActionPanel.class);
    BaseCreateEditForm<DailyLedger> form;

    /**
     * @param id    component id
     * @param model model for contact
     */
    public DailyLedgerActionPanel(String id, IModel<DailyLedger> model, BaseCreateEditForm<DailyLedger> form) {
        super(id, model);
        this.form = form;
    }

    @Override
    protected BaseCreateEditForm<DailyLedger> selectItem(DailyLedger dailyLedger) {
        LOG.debug("Selected item " + dailyLedger.toString());
        form.setDefaultModel(new CompoundPropertyModel<DailyLedger>(new DailyLedgerModel(dailyLedger)));
        return form;
    }

    @Override
    protected void deleteItem(DailyLedger dailyLedger) {
        form.deleteEntity(dailyLedger);
    }

}