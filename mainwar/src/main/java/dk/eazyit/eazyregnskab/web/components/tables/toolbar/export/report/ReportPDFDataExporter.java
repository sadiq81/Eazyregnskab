package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export.report;

import dk.eazyit.eazyregnskab.util.ReportObject;
import dk.eazyit.eazyregnskab.web.components.tables.toolbar.export.PDFDataExporter;
import org.apache.wicket.Session;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class ReportPDFDataExporter extends PDFDataExporter {

    private static final Logger LOG = LoggerFactory.getLogger(ReportPDFDataExporter.class);

    public ReportPDFDataExporter(org.apache.wicket.Page page) {
        super(page);
    }

    @Override
    protected String getTitle() {
        ReportObject reportObject = (ReportObject) Session.get().getAttribute(ReportObject.ATTRIBUTE_NAME);
        return getCurrentLegalEntity().getName() + " " + new StringResourceModel(page.getClass().getSimpleName() + ".report.title", new Model(reportObject)).getObject();
    }

}
