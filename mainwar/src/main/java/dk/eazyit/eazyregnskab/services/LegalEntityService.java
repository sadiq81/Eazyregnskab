package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityAccessDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.LegalEntityAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Trifork
 */
@Service("legalEntityService")
public class LegalEntityService {

    private Logger log = LoggerFactory.getLogger(LegalEntityService.class);

    @Autowired
    private LegalEntityDAO legalEntityDAO;
    @Autowired
    private LegalEntityAccessDAO legalEntityAccessDAO;

    @Transactional
    public LegalEntity createLegalEntity(AppUser appUser, LegalEntity legalEntity) {
        log.debug("Creating new Legal Entity " + legalEntity);
        legalEntityDAO.create(legalEntity);
        legalEntityAccessDAO.create(new LegalEntityAccess(appUser, legalEntity));
        return legalEntity;
    }

    @Transactional
    public LegalEntity saveLegalEntity(AppUser appUser, LegalEntity legalEntity) {
        log.debug("Saving Legal Entity " + legalEntity);
        legalEntityDAO.save(legalEntity);
        return legalEntity;
    }

    @Transactional(readOnly = true)
    public LegalEntity findLegalEntityById(Long id) {
        log.debug("Finding Legal Entity with id " + id);
        return legalEntityDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<LegalEntity> findLegalEntityByUser(AppUser appUser) {
        log.debug("Finding all Legal Entity accessible by " + appUser.getUsername());
        List<LegalEntity> list = legalEntityDAO.findByNamedQuery(LegalEntity.QUERY_FIND_LEGAL_ENTITY_BY_USER, appUser);
        return list;
    }

    @Transactional()
    public List<LegalEntityAccess> findLegalEntityAccessByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding all Legal Entity access to " + legalEntity.toString());
        return legalEntityAccessDAO.findByNamedQuery(LegalEntityAccess.QUERY_FIND_LEGAL_ENTITY_ACCESS_BY_LEGAL_ENTITY, legalEntity);
    }

    @Transactional
    public void deleteLegalEntity(AppUser appUser, LegalEntity legalEntity) {
        if (isDeletingAllowed(appUser, legalEntity)) {
            log.debug("Deleting legal entity " + legalEntity.toString());
            for (LegalEntityAccess legalEntityAccess : findLegalEntityAccessByLegalEntity(legalEntity)) {
                legalEntityAccessDAO.delete(legalEntityAccess);
            }
            legalEntityDAO.delete(legalEntity);
        } else {
            log.warn("Not able to delete legal entity " + legalEntity.toString() + " because it was the last");
        }
    }

    @Transactional(readOnly = true)
    public boolean isDeletingAllowed(AppUser appUser, LegalEntity legalEntity) {
        if (findLegalEntityByUser(appUser).size() <= 1) {
            return false;
        } else {
            return true;
        }
    }


}
