package dk.eazyit.eazyregnskab.web.components.resource;

/**
 * @author
 */

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;


public abstract class JasperXlsReportsResource extends ByteArrayResource {

    private String reportName;

    @SpringBean
    DataSource dateSource;

    static final Logger LOG = LoggerFactory.getLogger(JasperXlsReportsResource.class);

    public JasperXlsReportsResource(String reportName, String fileName) {
        super("application/download", null, fileName);
        this.reportName = reportName;
        Injector.get().inject(this);
    }

    @Override
    protected byte[] getData(Attributes attributes) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {

            ServletContext context = WebApplication.get().getServletContext();
            InputStream inputStream2 = context.getResourceAsStream("/WEB-INF/classes/" + reportName);

            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream2, getParameters(), dateSource.getConnection());
            JRXlsExporter exporterXLS = new JRXlsExporter();

            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER, false);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
            exporterXLS.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            exporterXLS.exportReport();

        } catch (JRException e) {
            LOG.error("Something went wrong in export of file " + reportName + e.toString());
        } catch (SQLException e) {
            LOG.error("Something went wrong in export of file " + reportName + e.toString());
        }

        return os.toByteArray();
    }

    protected abstract HashMap<String, Object> getParameters();

}
