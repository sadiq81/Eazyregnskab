package dk.eazyit.eazyregnskab.web.components.panels.report;

import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.lists.FinanceAccountListView;
import dk.eazyit.eazyregnskab.web.components.models.lists.FinanceAccountListModelWithSum;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

/**
 * @author
 */
public class BalancePanel extends ReportPanel {

    public BalancePanel(String id) {
        super(id);
    }

    public BalancePanel(String id, IModel<ReportObject> model) {
        super(id, model);
    }

    @Override
    protected void addToPage() {

        setOutputMarkupPlaceholderTag(true);

        add(new FinanceAccountListView("BalancePage.financeAccounts", new FinanceAccountListModelWithSum(new CompoundPropertyModel(getDefaultModel()))));
        add(new Label("BalancePage.dates", new PropertyModel<ReportObject>(getModelObject(), "dates")));
        add(new Label("BalancePage.datesCompare", new PropertyModel<ReportObject>(getModelObject(), "datesCompare")));

        add(new Label("BalancePage.compareType", new StringResourceModel("ReportCompareType.${reportCompareType}", this, getDefaultModel())));

        super.addToPage();

    }


}
