package dk.eazyit.eazyregnskab.web.components.form;

import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationMessage;
import dk.eazyit.eazyregnskab.domain.Country;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.MoneyCurrency;
import dk.eazyit.eazyregnskab.web.app.secure.settings.BaseDataPage;
import dk.eazyit.eazyregnskab.web.components.choice.EnumDropDownChoice;
import dk.eazyit.eazyregnskab.web.components.input.PlaceholderTextField;
import dk.eazyit.eazyregnskab.web.components.models.entities.LegalEntityModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import java.util.Arrays;

/**
 * @author
 */
public class LegalEntityForm extends BaseCreateEditForm<LegalEntity> {

    private BaseDataPage parent;

    public LegalEntityForm(String id, IModel<LegalEntity> model, BaseDataPage parent) {
        super(id, model);
        this.parent = parent;
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
    public void deleteEntity(LegalEntity legalEntity) {
        if (legalEntityService.deleteLegalEntity(getCurrentUser().getAppUserModel().getObject(), legalEntity)) {
            getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(legalEntityService.findLegalEntityByUser(getCurrentUser().getAppUserModel().getObject()).get(0)));
            setDefaultModel(new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject())));
            parent.updateLegalEntitySelections();
            getSession().success(new NotificationMessage(new ResourceModel("legal.entity.was.deleted")).hideAfter(Duration.seconds(DURATION)));
        } else {
            getSession().error(new NotificationMessage(new ResourceModel("must.be.one.legal.entity")).hideAfter(Duration.seconds(DURATION)));
        }
    }

    @Override
    public CompoundPropertyModel<LegalEntity> newEntity() {
        LegalEntity newLegalEntity = legalEntityService.createLegalEntity(getCurrentUser().getAppUserModel().getObject());
        getSelectedLegalEntity().setLegalEntityModel(new LegalEntityModel(newLegalEntity));
        parent.updateLegalEntitySelections();
        getSession().success(new NotificationMessage(new ResourceModel("created.and.saved.new.entity")).hideAfter(Duration.seconds(DURATION)));
        return new CompoundPropertyModel<LegalEntity>(new LegalEntityModel(getSelectedLegalEntity().getLegalEntityModel().getObject()));
    }

    @Override
    public void saveForm(LegalEntity legalEntity) {
        legalEntityService.saveLegalEntity(getCurrentUser().getAppUserModel().getObject(), legalEntity);
        parent.updateLegalEntitySelections();
        getSession().success(new NotificationMessage(new ResourceModel("changes.has.been.saved")).hideAfter(Duration.seconds(DURATION)));
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