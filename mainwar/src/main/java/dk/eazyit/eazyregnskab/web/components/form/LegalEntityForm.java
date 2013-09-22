package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.core.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.Country;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.MoneyCurrency;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import java.util.Arrays;

/**
 * @author
 */
public class LegalEntityForm extends BaseCreateEditForm<LegalEntity> {

    PlaceholderTextField name;
    EnumDropDownChoice<Country> country;
    EnumDropDownChoice<MoneyCurrency> moneyCurrency;

    public LegalEntityForm(String id, IModel<LegalEntity> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(name = (PlaceholderTextField) new PlaceholderTextField<String>("name", "BaseDataPage").setRequired(true));
        add(new PlaceholderTextField<String>("legalIdentification", "BaseDataPage"));
        add(new PlaceholderTextField<String>("address", "BaseDataPage"));
        add(new PlaceholderTextField<String>("postalCode", "BaseDataPage"));
        add(country = (EnumDropDownChoice<Country>) new EnumDropDownChoice<>("country", Arrays.asList(Country.values())).setRequired(true));
        add(moneyCurrency = (EnumDropDownChoice<MoneyCurrency>) new EnumDropDownChoice<>("moneyCurrency", Arrays.asList(MoneyCurrency.values())).setRequired(true).setOutputMarkupId(true));
    }

    @Override
    protected void configureComponents() {
        getFormSettings().setNewVisible(true).setConfirmNew(true);
        configureCountry();
    }


    @Override
    public void deleteEntity(LegalEntity legalEntity) {
        throw new NullPointerException("ILLEGAL FUNCTION");
    }

    @Override
    public LegalEntity buildNewEntity(LegalEntity previous) {
        LegalEntity newLegalEntity = legalEntityService.createLegalEntity(getCurrentUser());

        setCurrentLegalEntity(newLegalEntity);

        //TODO bad design, find another solution without using a reference.
        ((LoggedInPage) getPage()).changeLegalEntity();

        getSession().success(new NotificationMessage(new ResourceModel("BaseDataPage.created.and.saved.new.entity")).hideAfter(Duration.seconds(DURATION)));
        return newLegalEntity;
    }

    @Override
    public void saveForm(LegalEntity legalEntity) {
        legalEntityService.saveLegalEntity(getCurrentUser(), legalEntity);
        getSession().success(new NotificationMessage(new ResourceModel("BaseDataPage.changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
    }

    @Override
    public FormComponent focusAfterSave() {
        return name;
    }

    private void configureCountry() {
        country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                moneyCurrency.setModelObject(country.getConvertedInput().getMoneyCurrency());
                target.add(moneyCurrency);
            }
        });

    }
}