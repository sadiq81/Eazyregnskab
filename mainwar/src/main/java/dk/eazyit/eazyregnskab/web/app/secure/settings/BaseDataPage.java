package dk.eazyit.eazyregnskab.web.app.secure.settings;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.base.data", parentPage = BaseDataPage.class, subLevel = 0)
public class BaseDataPage extends LoggedInPage {

    private static final Logger LOG = LoggerFactory.getLogger(BaseDataPage.class);

    @SpringBean
    LegalEntityService legalEntityService;

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

        add(form = new LegalEntityForm("legalEntityEdit", new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject()))));
    }

    class LegalEntityForm extends BaseCreateEditForm<LegalEntity> {

        LegalEntityForm(String id, IModel<LegalEntity> model) {
            super(id, model);
        }

        @Override
        public void addToForm() {
            super.addToForm();
            add(new PlaceholderTextField<String>("name").setRequired(true));
            add(new PlaceholderTextField<String>("legalIdentification"));
            add(new PlaceholderTextField<String>("address"));
            add(new PlaceholderTextField<String>("postalCode"));
            add(new EnumDropDownChoice<Country>("country", Arrays.asList(Country.values())).setRequired(true));
            add(new EnumDropDownChoice<MoneyCurrency>("moneyCurrency", Arrays.asList(MoneyCurrency.values())).setRequired(true));
        }

        @Override
        public void deleteEntity() {
            if (legalEntityService.isDeletingAllowed(getCurrentUser().getAppUserModel().getObject(), getModelObject())) {

                legalEntityService.deleteLegalEntity(getCurrentUser().getAppUserModel().getObject(), getModelObject());
                getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()).get(0)));
                setDefaultModel(new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject())));
                updateLegalEntitySelections();
                getSession().success(new NotificationMessage(new ResourceModel("legal.entity.was.deleted")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Deleting LegalEntity " + getModelObject().toString());
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("must.be.one.legal.entity")).hideAfter(Duration.seconds(DURATION)));
                LOG.info("Not able to delete LegalEntity because it was the last left" + getModelObject().toString());
            }
        }

        @Override
        public void newEntity() {

            LegalEntity newLegalEntity = legalEntityService.createLegalEntity(getCurrentUser().getAppUserModel().getObject());
            getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(newLegalEntity));
            setDefaultModel(new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject())));
            updateLegalEntitySelections();
            getSession().success(new NotificationMessage(new ResourceModel("created.and.saved.new.entity")).hideAfter(Duration.seconds(DURATION)));
            LOG.info("Creating LegalEntity " + getModelObject().toString());

        }

        @Override
        public void saveForm() {
            legalEntityService.saveLegalEntity(getCurrentUser().getAppUserModel().getObject(), getModelObject());
            LOG.info("Saving LegalEntity " + getModelObject().toString());
            updateLegalEntitySelections();
            getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));

        }
    }

    @Override
    protected void changedLegalEntity(AjaxRequestTarget target) {
        super.changedLegalEntity(target);
        LegalEntityForm temp = new LegalEntityForm("legalEntityEdit", new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject())));
        temp.setOutputMarkupPlaceholderTag(true);
        addOrReplace(form, temp);
        temp.setParent(this);
        form.setParent(this);
        form = temp;
        LOG.info("Changed LegalEntity to " + getSelectedLegalEntity().getLegalEntityModel().getObject().toString());
    }
}
