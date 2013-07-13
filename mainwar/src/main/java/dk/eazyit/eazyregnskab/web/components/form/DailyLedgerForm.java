package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.validators.forms.DailyLedgerFormValidator;
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
    PlaceholderTextField nextBookingNumber;

    public DailyLedgerForm(String id, IModel<DailyLedger> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(name = (PlaceholderTextField) new PlaceholderTextField<String>("name").setRequired(true));
        add(nextBookingNumber = new PlaceholderTextField<Integer>("nextBookingNumber"));
        add(new DailyLedgerFormValidator(name));
    }

    @Override
    public void deleteEntity(DailyLedger dailyLedger) {
        if (dailyLedger.getId() != 0) {
            if (financeAccountService.deleteDailyLedger(dailyLedger)) {
                setCurrentDailyLedger(financeAccountService.findDailyLedgerByLegalEntity(getCurrentLegalEntity()).get(0));
                getSession().success(new NotificationMessage(new ResourceModel("daily.ledger.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Deleting Dailyledger " + getModelObject().toString());
                insertNewEntityInModel();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("daily.ledger.is.in.use")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Not able to delete Dailyledger since its in use " + dailyLedger.toString());
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("daily.ledger.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public DailyLedger buildNewEntity() {
        return new DailyLedger("", getCurrentLegalEntity());
    }

    @Override
    public void saveForm(DailyLedger dailyLedger) {
        financeAccountService.saveDailyLedger(dailyLedger, getCurrentLegalEntity());
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel();
    }
}