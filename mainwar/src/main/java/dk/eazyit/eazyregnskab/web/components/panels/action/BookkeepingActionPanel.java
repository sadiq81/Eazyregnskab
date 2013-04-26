package dk.eazyit.eazyregnskab.web.components.panels.action;

import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.models.DraftFinancePostingModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class BookkeepingActionPanel extends ActionPanel<DraftFinancePosting> {

    BaseCreateEditForm<DraftFinancePosting> form;

    private static final Logger LOG = LoggerFactory.getLogger(BookkeepingActionPanel.class);

    public BookkeepingActionPanel(String id, IModel<DraftFinancePosting> model, BaseCreateEditForm<DraftFinancePosting> form) {
        super(id, model);
    }

    @Override
    protected BaseCreateEditForm<DraftFinancePosting> selectItem(DraftFinancePosting draftFinancePosting) {
        LOG.debug("Selected item " + draftFinancePosting.toString());
        form.setDefaultModel(new CompoundPropertyModel<DraftFinancePosting>(new DraftFinancePostingModel(draftFinancePosting)));
        return form;
    }

    @Override
    protected void deleteItem(DraftFinancePosting draftFinancePosting) {
        form.deleteEntity(draftFinancePosting);
    }
}
