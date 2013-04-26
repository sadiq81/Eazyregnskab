package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.entities.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.validators.forms.DailyLedgerFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class DailyLedgerForm extends BaseCreateEditForm<DailyLedger> {

    private static final Logger LOG = LoggerFactory.getLogger(DailyLedgerForm.class);

    PlaceholderTextField name;

    public DailyLedgerForm(String id, IModel<DailyLedger> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(name = (PlaceholderTextField) new PlaceholderTextField<String>("name").setRequired(true));
        add(new DailyLedgerFormValidator(name));
    }

    @Override
    public void deleteEntity(DailyLedger dailyLedger) {
        super.deleteEntity(dailyLedger);
        if (dailyLedger.getId() != 0) {
            if (financeAccountService.deleteDailyLedger(dailyLedger)) {
                getCurrentDailyLedger().setDailyLedgerModel(new DailyLedgerModel(financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()).get(0)));
                getSession().success(new NotificationMessage(new ResourceModel("daily.ledger.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Deleting Dailyledger " + getModelObject().toString());
                newEntity();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("daily.ledger.is.in.use")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Not able to delete Dailyledger since its in use " + dailyLedger.toString());
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("daily.ledger.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public CompoundPropertyModel<DailyLedger> newEntity() {
        return new CompoundPropertyModel<DailyLedger>(new DailyLedger("", getSelectedLegalEntity().getLegalEntityModel().getObject()));
    }

    @Override
    public void saveForm(DailyLedger dailyLedger) {
        super.saveForm(null);
        financeAccountService.saveDailyLedger(dailyLedger, getSelectedLegalEntity().getLegalEntityModel().getObject());
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
    }
}