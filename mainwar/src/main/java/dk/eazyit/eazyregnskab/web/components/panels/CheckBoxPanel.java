package dk.eazyit.eazyregnskab.web.components.panels;

import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.services.PostingService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author
 */
@Transactional
public class CheckBoxPanel extends Panel {

    @SpringBean
    PostingService postingService;

    public CheckBoxPanel(String componentId, IModel dataModel) {
        super(componentId, dataModel);
        Injector.get().inject(this);

        add(new AjaxCheckBox("checkbox", dataModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                processInput();
                DraftFinancePosting draftFinancePosting = (DraftFinancePosting) ((PropertyModel) getModel()).getInnermostModelOrObject();
                postingService.saveDraftFinancePosting(draftFinancePosting);

            }
        });
    }
}



