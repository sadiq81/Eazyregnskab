package dk.eazyit.eazyregnskab.web.components.tables.tables;

import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.web.components.tables.columns.ColumnsForBalancePage;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.export.report.ReportExportToolbar;
import org.apache.wicket.Page;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.ResourceModel;

/**
 * @author
 */
public class ReportDataTable extends ExportableDataTable<FinanceAccount, String> {


    public ReportDataTable(String id, IDataProvider dataProvider, String filenameResourceText, Page page) {
        super(id, new ColumnsForBalancePage(), dataProvider, filenameResourceText, page);
    }

    @Override
    protected void addFirstToolBar(String filenameResourceText, Page page) {
        addTopToolbar(new ReportExportToolbar(this, new ResourceModel("datatable.export-to"), new ResourceModel(filenameResourceText), page));
    }
}