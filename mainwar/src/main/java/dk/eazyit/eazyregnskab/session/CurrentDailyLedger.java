package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.web.components.models.entities.DailyLedgerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class CurrentDailyLedger implements Serializable {

    private Logger LOG = LoggerFactory.getLogger(CurrentDailyLedger.class);

    public static final String ATTRIBUTE_NAME = CurrentDailyLedger.class.getName();

    private DailyLedgerModel dailyLedgerModel;

    public DailyLedgerModel getDailyLedgerModel() {
        if (dailyLedgerModel != null) LOG.trace("Getting DailyLedgerModel " +dailyLedgerModel.getObject().toString());
        return dailyLedgerModel;
    }

    public void setDailyLedgerModel(DailyLedgerModel dailyLedgerModel) {
        LOG.trace("Setting DailyLedgerModel " +dailyLedgerModel.getObject().toString());
        this.dailyLedgerModel = dailyLedgerModel;
    }
}