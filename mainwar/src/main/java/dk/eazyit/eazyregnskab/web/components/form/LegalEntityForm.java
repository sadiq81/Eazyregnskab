package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.Country;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.MoneyCurrency;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.entities.LegalEntityModel;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import java.util.Arrays;

/**
 * @author
 */
public class LegalEntityForm extends BaseCreateEditForm<LegalEntity> {

    EnumDropDownChoice<Country> country;
    EnumDropDownChoice<MoneyCurrency> moneyCurrency;

    public LegalEntityForm(String id, IModel<LegalEntity> model) {
        super(id, model);
    }

    @Override
    public void addToForm() {
        super.addToForm();
        add(new PlaceholderTextField<String>("name").setRequired(true));
        add(new PlaceholderTextField<String>("legalIdentification"));
        add(new PlaceholderTextField<String>("address"));
        add(new PlaceholderTextField<String>("postalCode"));
        add(country = (EnumDropDownChoice<Country>) new EnumDropDownChoice<Country>("country", Arrays.asList(Country.values())).setRequired(true));
        add(moneyCurrency = (EnumDropDownChoice<MoneyCurrency>) new EnumDropDownChoice<MoneyCurrency>("moneyCurrency", Arrays.asList(MoneyCurrency.values())).setRequired(true).setOutputMarkupId(true));
    }

    @Override
    protected void configureComponents() {
        configureCountry();
    }


    @Override
    public void deleteEntity(LegalEntity legalEntity) {
        if (legalEntityService.deleteLegalEntity(getCurrentUser(), legalEntity)) {
            setCurrentLegalEntity(legalEntityService.findLegalEntityByUser(getCurrentUser()).get(0));
            setDefaultModel(new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getCurrentLegalEntity())));

            //TODO bad design, find another solution without using a reference.
            ((LoggedInPage) getPage()).changeLegalEntity();

            getSession().success(new NotificationMessage(new ResourceModel("legal.entity.was.deleted")).hideAfter(Duration.seconds(DURATION)));
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("must.be.one.legal.entity")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public LegalEntity buildNewEntity() {
        LegalEntity newLegalEntity = legalEntityService.createLegalEntity(getCurrentUser());

        setCurrentLegalEntity(newLegalEntity);

        //TODO bad design, find another solution without using a reference.
        ((LoggedInPage) getPage()).changeLegalEntity();

        getSession().success(new NotificationMessage(new ResourceModel("created.and.saved.new.entity")).hideAfter(Duration.seconds(DURATION)));
        return newLegalEntity;
    }

    @Override
    public void saveForm(LegalEntity legalEntity) {
        legalEntityService.saveLegalEntity(getCurrentUser(), legalEntity);
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
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

    @Override
    protected boolean isNewButtonVisible() {
        return true;
    }

    @Override
    protected boolean isDeleteButtonVisible() {
        return true;
    }
}