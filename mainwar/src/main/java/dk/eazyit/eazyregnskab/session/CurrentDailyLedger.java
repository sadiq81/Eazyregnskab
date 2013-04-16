package dk.eazyit.eazyregnskab.session;

import dk.eazyit.eazyregnskab.web.components.models.DailyLedgerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author
 */
public class CurrentDailyLedger implements Serializable {

    private Logger log = LoggerFactory.getLogger(CurrentDailyLedger.class);

    public static final String ATTRIBUTE_NAME = CurrentDailyLedger.class.getName();

    private DailyLedgerModel dailyLedgerModel;

    public DailyLedgerModel getDailyLedgerModel() {
        return dailyLedgerModel;
    }

    public void setDailyLedgerModel(DailyLedgerModel dailyLedgerModel) {
        this.dailyLedgerModel = dailyLedgerModel;
    }
}