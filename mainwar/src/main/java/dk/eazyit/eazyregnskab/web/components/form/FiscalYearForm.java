package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.form.DateTextFieldConfig;
import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.domain.FiscalYearStatus;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderDateField;
import dk.eazyit.eazyregnskab.web.components.validators.forms.FiscalYearFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * @author
 */
public class FiscalYearForm extends BaseCreateEditForm<FiscalYear> {

    PlaceholderDateField start;
    PlaceholderDateField end;

    public FiscalYearForm(String id, IModel<FiscalYear> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(start = (PlaceholderDateField) new PlaceholderDateField("start", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(end = (PlaceholderDateField) new PlaceholderDateField("end", new DateTextFieldConfig().autoClose(true).withFormat("dd-MM-yy").allowKeyboardNavigation(true).showTodayButton(true)).setRequired(true));
        add(new FiscalYearFormValidator(start, end));
    }

    @Override
    protected void configureComponents() {
    }


    @Override
    public void deleteEntity(FiscalYear fiscalYear) {
        if (fiscalYear.getId() != 0) {
            if (fiscalYearService.deleteFiscalYear(fiscalYear)) {
                getSession().success(new NotificationMessage(new ResourceModel("fiscal.year.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                insertNewEntityInModel();
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("fiscal.year.is.in.use")).hideAfter(Duration.seconds(DURATION)));
            }
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("fiscal.year.was.never.saved")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public FiscalYear buildNewEntity() {
        return new FiscalYear().setFiscalYearStatus(FiscalYearStatus.OPEN).setLegalEntity(getCurrentLegalEntity());
    }

    @Override
    public void saveForm(FiscalYear fiscalYear) {
        if (fiscalYearService.save(fiscalYear.setLegalEntity(getCurrentLegalEntity()))) {
            getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
            insertNewEntityInModel();
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("fiscal.year.dates.are.in.use")).hideAfter(Duration.seconds(DURATION)));
        }


    }
}