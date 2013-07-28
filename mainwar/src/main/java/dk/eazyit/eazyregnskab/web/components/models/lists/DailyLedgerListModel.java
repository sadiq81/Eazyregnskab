package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.DailyLedgerService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author EazyIT
 */
public class DailyLedgerListModel extends AbstractEntityListModel<DailyLedger, LegalEntity> {

    @SpringBean
    DailyLedgerService dailyLedgerService;

    static final Logger LOG = LoggerFactory.getLogger(DailyLedgerListModel.class);

    public DailyLedgerListModel() {
        Injector.get().inject(this);
    }

    public DailyLedgerListModel(LegalEntity parent) {
        super(parent);
        Injector.get().inject(this);
    }

    public DailyLedgerListModel(LegalEntity parent, List<DailyLedger> list) {
        super(parent, list);
        Injector.get().inject(this);
    }

    @Override
    protected List<DailyLedger> load(LegalEntity id) {
        return dailyLedgerService.findDailyLedgerByLegalEntity(id);
    }

    @Override
    protected LegalEntity fetchParent() {
        return getCurrentLegalEntity();
    }

    @Override
    public void setObject(List<DailyLedger> object) {
        for (DailyLedger dailyLedger : object) {
            dailyLedgerService.saveDailyLedger(dailyLedger, fetchParent());
        }
    }


}
