package dk.eazyit.eazyregnskab.web.components.panels.action;

import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.form.BaseCreateEditForm;
import dk.eazyit.eazyregnskab.web.components.models.VatTypeModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class VatTypeActionPanel extends ActionPanel<VatType> {

    BaseCreateEditForm<VatType> form;

    private static final Logger LOG = LoggerFactory.getLogger(VatTypeActionPanel.class);

    /**
     * @param id    component id
     * @param model model for contact
     */
    public VatTypeActionPanel(String id, IModel<VatType> model, BaseCreateEditForm<VatType> form) {
        super(id, model);
        this.form = form;
    }

    @Override
    protected BaseCreateEditForm<VatType> selectItem(VatType vatType) {
        LOG.debug("Selected item " + vatType.toString());
        form.setDefaultModel(new CompoundPropertyModel<VatType>(new VatTypeModel(vatType)));
        return form;
    }

    @Override
    protected void deleteItem(VatType vatType) {
        form.deleteEntity(vatType);
    }
}