package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.web.components.form.LegalEntityForm;
import dk.eazyit.eazyregnskab.web.components.models.LegalEntityModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.base.data", parentPage = BaseDataPage.class, subLevel = 0, topLevelPage = true, topLevel = 0)
public class BaseDataPage extends LoggedInPage {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDataPage.class);

    LegalEntityForm form;

    public BaseDataPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public BaseDataPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public BaseDataPage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new LegalEntityForm("legalEntityEdit", new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject())),this));
    }

    @Override
    public void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        addOrReplace(form, new LegalEntityForm("legalEntityEdit", new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject())),this));
        LOG.debug("Changed LegalEntity to " + getSelectedLegalEntity().getLegalEntityModel().getObject().toString());
    }
}
