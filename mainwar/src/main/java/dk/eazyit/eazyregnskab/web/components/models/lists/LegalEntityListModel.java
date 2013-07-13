package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.services.LegalEntityService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author EazyIT
 */
public class LegalEntityListModel extends AbstractEntityListModel<LegalEntity, AppUser> {

    @SpringBean
    LegalEntityService legalEntityService;

    static final Logger LOG = LoggerFactory.getLogger(LegalEntityListModel.class);

    public LegalEntityListModel() {
        Injector.get().inject(this);
    }

    public LegalEntityListModel(AppUser parent) {
        super(parent);
        Injector.get().inject(this);
    }

    public LegalEntityListModel(AppUser parent, List<LegalEntity> list) {
        super(parent, list);
        Injector.get().inject(this);
    }

    @Override
    protected List<LegalEntity> load(AppUser id) {
        return legalEntityService.findLegalEntityByUser(id);
    }

    @Override
    protected AppUser fetchParent() {
        return getCurrentUser();
    }

    @Override
    public void setObject(List<LegalEntity> object) {
        for (LegalEntity legalEntity : object) {
            legalEntityService.saveLegalEntity(fetchParent(), legalEntity);
        }
    }
}
