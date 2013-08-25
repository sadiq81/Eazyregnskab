package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.validators.forms.DailyLedgerFormValidator;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
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
        add(name = (PlaceholderTextField) new PlaceholderTextField<String>("name", "DailyLedgerPage").setRequired(true));
        add(nextBookingNumber = new PlaceholderTextField<Integer>("nextBookingNumber", "DailyLedgerPage"));
        add(new DropDownChoice<FinanceAccount>("financeAccount", financeAccountService.findBookableFinanceAccountByLegalEntity(getCurrentLegalEntity()), new ChoiceRenderer<FinanceAccount>("name", "id")));
        add(new DailyLedgerFormValidator(name));
    }

    @Override
    protected void configureComponents() {
    }

    @Override
    public void deleteEntity(DailyLedger dailyLedger) {
        if (dailyLedger.getId() != 0) {
            if (dailyLedgerService.deleteDailyLedger(dailyLedger)) {
                setCurrentDailyLedger(dailyLedgerService.findDailyLedgerByLegalEntity(getCurrentLegalEntity()).get(0));
                getSession().success(new NotificationMessage(new ResourceModel("DailyLedgerPage.daily.ledger.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Deleting Dailyledger " + getModelObject().toString());
                insertNewEntityInModel(dailyLedger);
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("DailyLedgerPage.daily.ledger.is.in.use")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Not able to delete Dailyledger since its in use " + dailyLedger.toString());
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("DailyLedgerPage.daily.ledger.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public DailyLedger buildNewEntity(DailyLedger previous) {
        return new DailyLedger("", getCurrentLegalEntity());
    }

    @Override
    public void saveForm(DailyLedger dailyLedger) {
        dailyLedgerService.saveDailyLedger(dailyLedger, getCurrentLegalEntity());
        getSession().success(new NotificationMessage(new ResourceModel("DailyLedgerPage.changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
        insertNewEntityInModel(dailyLedger);
    }

    @Override
    public FormComponent focusAfterSave() {
        return name;
    }
}