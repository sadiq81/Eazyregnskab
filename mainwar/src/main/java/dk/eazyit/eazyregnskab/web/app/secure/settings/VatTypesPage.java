package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.VatType;
import dk.eazyit.eazyregnskab.web.components.dataprovider.VatTypeDataProvider;
import dk.eazyit.eazyregnskab.web.components.form.VatTypeForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.VatTypeModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.columns.ColumnsForVatTypesPage;
import dk.eazyit.eazyregnskab.web.components.tables.tables.ExportableDataTable;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.vat.type", parentPage = BaseDataPage.class, subLevel = 3, topLevelPage = false)
public class VatTypesPage extends LoggedInPage {

    VatTypeForm form;
    ExportableDataTable dataTable;

    private static final Logger LOG = LoggerFactory.getLogger(VatTypesPage.class);

    public VatTypesPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public VatTypesPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public VatTypesPage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new VatTypeForm("vatTypeEdit", new CompoundPropertyModel<VatType>(new VatTypeModel(new VatType()))));

        add(dataTable = new ExportableDataTable("chartOfVatTypes", new ColumnsForVatTypesPage(form), new VatTypeDataProvider(), "VatTypesPage.datatable.export-file-name", this));
    }

    @Override
    protected void configureComponents() {

    }

}
