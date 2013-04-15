package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author EazyIT
 */
public class DailyLedgerModel extends AbstractEntityModel<DailyLedger> {

    @SpringBean
    DailyLedgerDAO dailyLedgerDAO;

    public DailyLedgerModel(DailyLedger entity) {
        super(entity);
        Injector.get().inject(this);
    }

    public DailyLedgerModel(Class<? extends DailyLedger> clazz, Long id) {
        super(DailyLedger.class, id);
        Injector.get().inject(this);
    }

    @Override
    protected DailyLedger load(Long id) {
        if (id == 0) {
            return new DailyLedger();
        } else {
            return dailyLedgerDAO.findById(id);
        }
    }

    @Override
    public void setObject(DailyLedger object) {
        if (object.getId() == 0) {
            dailyLedgerDAO.create(object);
        } else {
            entity = dailyLedgerDAO.save(object);
        }
    }
}
