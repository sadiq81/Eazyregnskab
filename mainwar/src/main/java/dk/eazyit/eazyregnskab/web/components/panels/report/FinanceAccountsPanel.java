package dk.eazyit.eazyregnskab.web.components.panels.report;

import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.lists.BookedFinancePostingListView;
import dk.eazyit.eazyregnskab.web.components.models.lists.BookedFinancePostingListModelWithSum;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class FinanceAccountsPanel extends ReportPanel {

    public FinanceAccountsPanel(String id, IModel<ReportObject> model) {
        super(id, model);
    }

    @Override
    protected void addToPage() {

        setOutputMarkupPlaceholderTag(true);

        add(new BookedFinancePostingListView("FinanceAccountsPage.postings", new BookedFinancePostingListModelWithSum(new CompoundPropertyModel(getDefaultModel()))));

        super.addToPage();
    }


}
