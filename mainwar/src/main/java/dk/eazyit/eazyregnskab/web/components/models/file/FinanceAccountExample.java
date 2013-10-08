package dk.eazyit.eazyregnskab.web.components.models.file;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * @author
 */
public class FinanceAccountExample extends AbstractReadOnlyModel<File> {

    static final Logger LOG = LoggerFactory.getLogger(FinanceAccountExample.class);

    @Override
    public File getObject() {
        ServletContext servletContext = WebApplication.get().getServletContext();

        File file = new File(servletContext.getRealPath("/WEB-INF/classes/files/examples/import_finance_account_example.xls"));
        if (!file.exists()) LOG.error("import_finance_account_example.xls could not be retrieved from container");
        return file;
    }
}
