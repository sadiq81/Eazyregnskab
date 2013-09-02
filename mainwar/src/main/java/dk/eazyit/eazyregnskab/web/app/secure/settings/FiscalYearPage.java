package dk.eazyit.eazyregnskab.web.app.secure.settings;

import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.domain.FiscalYearStatus;
import dk.eazyit.eazyregnskab.web.components.form.FiscalYearForm;
import dk.eazyit.eazyregnskab.web.components.models.entities.FiscalYearModel;
import dk.eazyit.eazyregnskab.web.components.navigation.menu.MenuPosition;
import dk.eazyit.eazyregnskab.web.components.page.LoggedInPage;
import dk.eazyit.eazyregnskab.web.components.tables.tables.ExportableDataTable;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
@MenuPosition(name = "settings.fiscal.year", parentPage = BaseDataPage.class, subLevel = 4, topLevelPage = false)
public class FiscalYearPage extends LoggedInPage {

    FiscalYearForm form;
    ExportableDataTable dataTable;

    private static final Logger LOG = LoggerFactory.getLogger(FiscalYearPage.class);

    public FiscalYearPage() {
        super();
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId());
    }

    public FiscalYearPage(IModel<?> model) {
        super(model);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and model " + model.getObject().toString());
    }

    public FiscalYearPage(PageParameters parameters) {
        super(parameters);
        LOG.trace("creating " + this.getClass().getSimpleName() + " with id " + this.getId() + " and parameters " + parameters.toString());
    }

    @Override
    protected void addToPage(PageParameters parameters) {
        super.addToPage(parameters);

        add(form = new FiscalYearForm("fiscalYearEdit", new CompoundPropertyModel<FiscalYear>(new FiscalYearModel(new FiscalYear().setFiscalYearStatus(FiscalYearStatus.OPEN).setLegalEntity(getCurrentLegalEntity())))));

//        add(dataTable = new ExportableDataTable("chartOfFiscalYears", new ColumnsForFiscalYearsPage(form), new FiscalYearDataProvider(), 20, "FiscalYearPage.datatable.export-file-name", new float[]{135, 135, 135, 135}) {
//            @Override
//            public String getTitle() {
//                return new StringResourceModel("FiscalYearPage.report.title", this, new Model<>(getCurrentLegalEntity())).getString();
//            }
//        });
    }

    @Override
    protected void configureComponents() {

    }

}
