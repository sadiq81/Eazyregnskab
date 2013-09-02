package dk.eazyit.eazyregnskab.web.components.tables.toolbar.export;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.session.EazyregnskabSesssion;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.AbstractDataExporter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Classes;

/**
 * @author
 */
public abstract class SessionAwareDataExporter extends AbstractDataExporter {


    protected Page page;

    /**
     * Creates a new instance with the data format name model, content type and file name extensions provided.
     *
     * @param dataFormatNameModel The model of the exported data format name.
     * @param contentType         The MIME content type of the exported data type.
     * @param fileNameExtension   The file name extensions to use in the file name for the exported data.
     */
    public SessionAwareDataExporter(IModel<String> dataFormatNameModel, String contentType, String fileNameExtension, Page page) {
        super(dataFormatNameModel, contentType, fileNameExtension);
        this.page = page;
    }

    protected String resourceKey(Enum value) {
        return Classes.simpleName(value.getDeclaringClass()) + '.' + value.name();
    }

    public AppUser getCurrentUser() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentUser();
    }

    public LegalEntity getCurrentLegalEntity() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentLegalEntity();
    }

    public void setCurrentLegalEntity(LegalEntity legalEntity) {
        ((EazyregnskabSesssion) Session.get()).setCurrentLegalEntity(legalEntity);
    }

    public DailyLedger getCurrentDailyLedger() {
        return ((EazyregnskabSesssion) Session.get()).getCurrentDailyLedger();
    }

    public void setCurrentDailyLedger(DailyLedger dailyLedger) {
        ((EazyregnskabSesssion) Session.get()).setCurrentDailyLedger(dailyLedger);
    }
}
