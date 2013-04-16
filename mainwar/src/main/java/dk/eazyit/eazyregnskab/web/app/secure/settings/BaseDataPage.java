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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.util.Arrays;

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
                updateLegalEntitySelections();
                getSession().success(new NotificationMessage(new ResourceModel("legal.entity.was.deleted")).hideAfter(Duration.seconds(DURATION)));
            } else {
                getSession().error(new NotificationMessage(new ResourceModel("must.be.one.legal.entity")).hideAfter(Duration.seconds(DURATION)));
            }
        }

        @Override
        public void newEntity() {
            LegalEntity newLegalEntity = legalEntityService.createLegalEntity(getCurrentUser().getAppUserModel().getObject(),
                    new LegalEntity(getString("new.legal.entity"), null, null, null, Country.DK, MoneyCurrency.DKK));
            getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(newLegalEntity));
            updateLegalEntitySelections();
            getSession().success(new NotificationMessage(new ResourceModel("created.and.saved.new.entity")).hideAfter(Duration.seconds(DURATION)));

        }

        @Override
        public void saveForm() {
            legalEntityService.saveLegalEntity(getCurrentUser().getAppUserModel().getObject(), getModelObject());
            updateLegalEntitySelections();
            getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));

        }
    }

    @Override
    protected void updateLegalEntitySelections() {
        super.updateLegalEntitySelections();

        LegalEntityForm temp = new LegalEntityForm("legalEntityEdit", getSelectedLegalEntity().getLegalEntityModel());
        temp.setOutputMarkupPlaceholderTag(true);
        addOrReplace(form, temp);
        temp.setParent(this);
        form.setParent(this);
        form = temp;

    }
}
