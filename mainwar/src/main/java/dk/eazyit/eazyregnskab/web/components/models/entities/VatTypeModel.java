package dk.eazyit.eazyregnskab.web.components.models.entities;

import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.domain.VatType;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EazyIT
 */
public class VatTypeModel extends AbstractEntityModel<VatType> {

    @SpringBean
    VatTypeDAO vatTypeDAO;

    static final Logger LOG = LoggerFactory.getLogger(VatTypeModel.class);

    public VatTypeModel(VatType entity) {
        super(entity);
        Injector.get().inject(this);
        LOG.trace("Created VatTypeModel with id " + entity.getId());
    }

    public VatTypeModel(Class<? extends VatType> clazz, Long id) {
        super(VatType.class, id);
        Injector.get().inject(this);
        LOG.trace("Created VatTypeModel with id " + entity.getId());
    }

    @Override
    protected VatType load(Long id) {
        if (id == 0) {
            if (entity != null) LOG.trace("loading empty VatType entity");
            return new VatType();
        } else {
            if (entity != null) LOG.trace("loading VatType entity " + entity.getId());
            return vatTypeDAO.findById(id);
        }
    }

    @Override
    public void setObject(VatType object) {

        if (object == null || object.getId() == null) {
            return;
        }
        LOG.trace("setting VatType entity " + object.getId());
        if (object.getId() == 0) {
            entity = object;
        } else {
            entity = vatTypeDAO.save(object);
        }
    }
}
