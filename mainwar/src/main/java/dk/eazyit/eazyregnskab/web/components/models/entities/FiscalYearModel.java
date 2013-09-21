package dk.eazyit.eazyregnskab.web.components.models.entities;

import dk.eazyit.eazyregnskab.dao.interfaces.FiscalYearDAO;
import dk.eazyit.eazyregnskab.domain.FiscalYear;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
public class FiscalYearModel extends AbstractEntityModel<FiscalYear> {

    @SpringBean
    FiscalYearDAO fiscalYearDAO;

    static final Logger LOG = LoggerFactory.getLogger(FiscalYearModel.class);

    public FiscalYearModel(FiscalYear entity) {
        super(entity);
        Injector.get().inject(this);
        LOG.trace("Created FiscalYearModel with id " + entity.getId());
    }

    public FiscalYearModel(Class<? extends FiscalYear> clazz, Long id) {
        super(FiscalYear.class, id);
        Injector.get().inject(this);
        LOG.trace("Created FiscalYearModel with id " + entity.getId());
    }

    @Override
    protected FiscalYear load(Long id) {
        if (id == 0) {
            if (entity != null) LOG.trace("loading empty FiscalYear entity");
            return new FiscalYear();
        } else {
            if (entity != null) LOG.trace("loading FiscalYear entity " + entity.getId());
            return fiscalYearDAO.findById(id);
        }
    }

    @Override
    public void setObject(FiscalYear object) {
        LOG.trace("setting VatType entity " + object.getId());
        if (object == null || object.getId() == null) {
            return;
        }
        if (object.getId() == 0) {
            entity = object;
        } else {
            entity = fiscalYearDAO.save(object);
        }
    }
}
