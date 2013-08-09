package dk.eazyit.eazyregnskab.web.components.panels;

import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.lists.BookedFinancePostingListView;
import dk.eazyit.eazyregnskab.web.components.models.lists.BookedFinancePostingListModelWithSum;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class FinanceAccountsPanel extends SessionAwarePanel<ReportObject> {

    public FinanceAccountsPanel(String id) {
        super(id);
    }

    public FinanceAccountsPanel(String id, IModel<ReportObject> model) {
        super(id, model);
    }

    @Override
    protected void addToPage() {
        setOutputMarkupPlaceholderTag(true);

        add(new BookedFinancePostingListView("postings", new BookedFinancePostingListModelWithSum(new CompoundPropertyModel(getDefaultModel()))));

    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisibilityAllowed(getModelObject().isSubmitHasBeenPressed());
    }


}