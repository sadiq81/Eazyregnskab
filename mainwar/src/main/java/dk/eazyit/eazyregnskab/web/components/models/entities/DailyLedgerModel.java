package dk.eazyit.eazyregnskab.web.components.models.entities;

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
public class DailyLedgerModel extends AbstractEntityModel<DailyLedger> {

    @SpringBean
    DailyLedgerDAO dailyLedgerDAO;

    static final Logger LOG = LoggerFactory.getLogger(DailyLedgerModel.class);

    public DailyLedgerModel(DailyLedger entity) {
        super(entity);
        Injector.get().inject(this);
        LOG.trace("Created DailyLedgerModel with id " + entity.getId());
    }

    public DailyLedgerModel(Class<? extends DailyLedger> clazz, Long id) {
        super(DailyLedger.class, id);
        Injector.get().inject(this);
        LOG.trace("Created DailyLedgerModel with id " + entity.getId());
    }

    @Override
    protected DailyLedger load(Long id) {
        if (id == 0) {
            if (entity != null) LOG.trace("Loading empty DailyLedgerModel ");
            return new DailyLedger();
        } else {
            if (entity != null) LOG.trace("Loading DailyLedgerModel with id " + entity.getId());
            return dailyLedgerDAO.findById(id);
        }
    }

    @Override
    public void setObject(DailyLedger object) {

        if (object == null || object.getId() == null) {
            return;
        }
        LOG.trace("setting DailyLedger entity " + object.getId());
        if (object.getId() == 0) {
            entity = object;
        } else {
            entity = dailyLedgerDAO.save(object);
        }
    }
}
