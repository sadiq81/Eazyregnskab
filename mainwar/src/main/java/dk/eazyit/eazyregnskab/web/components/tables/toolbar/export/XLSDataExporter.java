package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export;

import org.apache.wicket.Page;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public abstract class XLSDataExporter extends SessionAwareDataExporter {


    private static final Logger LOG = LoggerFactory.getLogger(XLSDataExporter.class);

    /**
     * Creates a new instance with the data format name model, content type and file name extensions provided.
     *
     * @param dataFormatNameModel The model of the exported data format name.
     * @param contentType         The MIME content type of the exported data type.
     * @param fileNameExtension   The file name extensions to use in the file name for the exported data.
     */
    public XLSDataExporter(Page page) {
        super(Model.of("XLS "), "application/vnd.ms-excel", "xls", page);
    }

    protected abstract String getTitle();

    protected String resourceKey(Enum value) {
        return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
    }
}
