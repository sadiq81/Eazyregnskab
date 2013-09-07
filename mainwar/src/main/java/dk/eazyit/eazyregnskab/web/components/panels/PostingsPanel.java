package dk.eazyit.eazyregnskab.web.components.panels;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.web.components.link.AjaxToolTipLink;
import dk.eazyit.eazyregnskab.web.components.modal.PostingsModal;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class PostingsPanel extends GenericPanel<BookedFinancePosting> {
    public PostingsPanel(String id, final IModel<BookedFinancePosting> model, final PostingsModal postingsModal) {
        super(id, model);
        add(new AjaxToolTipLink(id, "tooltip.postings") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                postingsModal.setItem(model.getObject()).show(target);
            }
        });
    }
}
