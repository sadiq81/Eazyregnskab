package dk.eazyit.eazyregnskab.web.components.panels;

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
public class BalancePanel extends SessionAwarePanel<ReportObject> {

    public BalancePanel(String id) {
        super(id);
    }

    public BalancePanel(String id, IModel<ReportObject> model) {
        super(id, model);
    }

    @Override
    protected void addToPage() {
        setOutputMarkupPlaceholderTag(true);

        add(new FinanceAccountListView("financeAccounts", new FinanceAccountListModelWithSum(new CompoundPropertyModel(getDefaultModel()))));
        add(new Label("dates", new PropertyModel<ReportObject>(getDefaultModel(), "dates")));
        add(new Label("datesCompare", new PropertyModel<ReportObject>(getDefaultModel(), "datesCompare")));

        add(new Label("compareType", new StringResourceModel("ReportCompareType.${reportCompareType}", this, getDefaultModel())));

    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(getModelObject().isSubmitHasBeenPressed());
    }
}
