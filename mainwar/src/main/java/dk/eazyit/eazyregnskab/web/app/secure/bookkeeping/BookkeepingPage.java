package dk.eazyit.eazyregnskab.web.app.secure.bookkeeping;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

@MenuPosition(name = "bookkeeping.daybook", parentPage = BookkeepingPage.class, subLevel = 0)
public class BookkeepingPage extends LoggedInPage {

    private static final long serialVersionUID = 1L;

    @SpringBean
    FinanceAccountService financeAccountService;

    private DropDownChoice<DailyLedger> dailyLedgerDropDownChoice;
    private DailyLedgerModel dailyLedgerModel;

    public BookkeepingPage() {
        super();

    }

    public BookkeepingPage(IModel<?> model) {
        super(model);
    }

    public BookkeepingPage(final PageParameters parameters) {
        super(parameters);

    }

    @Override
    protected void ensureUserInfo(PageParameters parameters) {
        super.ensureUserInfo(parameters);

    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(dailyLedgerDropDownChoice = new DropDownChoice<DailyLedger>("dailyLedgerList",
                dailyLedgerModel = getCurrentDailyLedger().getDailyLedgerModel(),
                financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()),
                new ChoiceRenderer<DailyLedger>("name", "id")));

        dailyLedgerDropDownChoice.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getPage());
            }
        }));
        dailyLedgerDropDownChoice.setOutputMarkupPlaceholderTag(true);
    }

    @Override
    protected void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);

        DropDownChoice<DailyLedger> temp = new DropDownChoice<DailyLedger>("legalEntityList",
                dailyLedgerModel = getCurrentDailyLedger().getDailyLedgerModel(),
                financeAccountService.findDailyLedgerByLegalEntity(getSelectedLegalEntity().getLegalEntityModel().getObject()),
                new ChoiceRenderer<DailyLedger>("name", "id"));
        temp.add((new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(getPage());
            }
        }));
        temp.setOutputMarkupPlaceholderTag(true);
        addOrReplace(dailyLedgerDropDownChoice, temp);
        temp.setParent(this);
        dailyLedgerDropDownChoice.setParent(this);
        dailyLedgerDropDownChoice = temp;
    }
}
