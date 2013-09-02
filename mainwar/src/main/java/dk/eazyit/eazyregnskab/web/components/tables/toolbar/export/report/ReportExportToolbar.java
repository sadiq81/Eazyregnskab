package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export.report;

import dk.eazyit.eazyregnskab.domain.ExportTableRow;
import dk.eazyit.eazyregnskab.web.components.tables.tables.ExportableDataTable;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.export.CustomExportToolbar;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;

/**
 * @author
 */
public class ReportExportToolbar<E extends ExportTableRow> extends CustomExportToolbar {

    public ReportExportToolbar(ExportableDataTable<E, ?> table, IModel<String> messageModel, IModel<String> fileNameModel, Page page) {
        super(table, messageModel, fileNameModel, page);
    }

    @Override
    protected void addDataExporters(Page page) {
        addDataExporter(new ReportXLSDataExporter(page));
        addDataExporter(new ReportPDFDataExporter(page));
    }
}
