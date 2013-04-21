package dk.eazyit.eazyregnskab.web.components.models;

import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.domain.VatType;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * @author EazyIT
 */
public class VatTypeModel extends AbstractEntityModel<VatType> {

    @SpringBean
    VatTypeDAO vatTypeDAO;

    public VatTypeModel(VatType entity) {
        super(entity);
        Injector.get().inject(this);
    }

    public VatTypeModel(Class<? extends VatType> clazz, Long id) {
        super(VatType.class, id);
        Injector.get().inject(this);
    }

    @Override
    protected VatType load(Long id) {
        if (id == 0) {
            return new VatType();
        } else {
            return vatTypeDAO.findById(id);
        }
    }

    @Override
    public void setObject(VatType object) {
        if (object.getId() == 0) {
            vatTypeDAO.create(object);
        } else {
            entity = vatTypeDAO.save(object);
        }
    }
}
