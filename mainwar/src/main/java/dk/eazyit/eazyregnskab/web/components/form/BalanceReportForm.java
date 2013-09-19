package dk.eazyit.eazyregnskab.web.components.form;

import dk.eazyit.eazyregnskab.domain.EntityWithLongId;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.ReportCompareType;
import dk.eazyit.eazyregnskab.services.ReportService;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.validators.forms.BalanceReportFormValidator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

/**
 * @author
 */
public class BalanceReportForm extends BaseReportForm {

    @SpringBean
    ReportService reportService;

    public BalanceReportForm(String id, WebMarkupContainer refreshOnSubmit) {
        super(id, refreshOnSubmit);
    }

    public BalanceReportForm(String id, IModel model, WebMarkupContainer refreshOnSubmit) {
        super(id, model, refreshOnSubmit);
    }

    @Override
    protected void addToForm() {
        super.addToForm();
        add(new EnumDropDownChoice<>("reportCompareType", Arrays.asList(ReportCompareType.values())).setRequired(true));

        add(new BalanceReportFormValidator());
    }

    @Override
    protected void submit(AjaxRequestTarget target) {

    }

    @Override
    protected boolean exportWithBeans() {
        return true;
    }

    @Override
    protected EntityWithLongId[] getCollectionForReport() {
        List<FinanceAccount> list = reportService.getFinanceAccountsWithSum(getCurrentLegalEntity(), new CompoundPropertyModel<>(getModel()));
        return list.toArray(new EntityWithLongId[list.size()]);
    }
}
