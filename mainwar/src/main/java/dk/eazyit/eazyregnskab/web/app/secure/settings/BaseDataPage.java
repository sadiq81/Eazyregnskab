package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.Country;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.MoneyCurrency;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.LegalEntityModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.base.data", parentPage = BaseDataPage.class, subLevel = 0)
public class BaseDataPage extends LoggedInPage {

    @SpringBean
    LegalEntityService legalEntityService;

    LegalEntityForm form;

    public BaseDataPage() {
        super();
    }

    public BaseDataPage(IModel<?> model) {
        super(model);
    }

    public BaseDataPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new LegalEntityForm("legalEntityEdit", getSelectedLegalEntity().getLegalEntityModel()));
    }

    class LegalEntityForm extends BaseCreateEditForm<LegalEntity> {

        LegalEntityForm(String id, IModel<LegalEntity> model) {
            super(id, model);
        }

        @Override
        protected void onBeforeRender() {
            LegalEntity legalEntity = getSelectedLegalEntity().getLegalEntityModel().getObject();
            super.onBeforeRender();
        }

        @Override
        public void addToForm() {
            super.addToForm();
            add(new PlaceholderTextField<String>("name"));
            add(new PlaceholderTextField<String>("legalIdentification"));
            add(new PlaceholderTextField<String>("address"));
            add(new PlaceholderTextField<String>("postalCode"));
            add(new EnumDropDownChoice<Country>("country"));
            add(new EnumDropDownChoice<MoneyCurrency>("moneyCurrency"));
        }

        @Override
        public void deleteEntity() {
            if (legalEntityService.isDeletingAllowed(getCurrentUser().getAppUserModel().getObject(), getModelObject())) {

                legalEntityService.deleteLegalEntity(getCurrentUser().getAppUserModel().getObject(), getModelObject());
                getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()).get(0)));
                updateSelections();
                getSession().info(getString("legal.entity.was.deleted"));
            } else {
                getSession().error(getString("must.be.one.legal.entity"));
            }
        }

        @Override
        public void newEntity() {
            LegalEntity newLegalEntity = legalEntityService.createLegalEntity(getCurrentUser().getAppUserModel().getObject(),
                    new LegalEntity(getString("new.legal.entity"), null, null, null, Country.DK, MoneyCurrency.DKK));
            getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(newLegalEntity));
            updateSelections();
            getSession().info(getString("created.and.saved.new.entity"));
        }

        @Override
        public void saveForm() {
            legalEntityService.saveLegalEntity(getCurrentUser().getAppUserModel().getObject(), getModelObject());
            updateSelections();
            getSession().info(getString("changes.has.been.saved"));

        }
    }

    @Override
    protected void updateSelections() {
        super.updateSelections();

        LegalEntityForm temp = new LegalEntityForm("legalEntityEdit", getSelectedLegalEntity().getLegalEntityModel());
        temp.setOutputMarkupPlaceholderTag(true);
        addOrReplace(form, temp);
        temp.setParent(this);
        form.setParent(this);
        form = temp;

    }
}
