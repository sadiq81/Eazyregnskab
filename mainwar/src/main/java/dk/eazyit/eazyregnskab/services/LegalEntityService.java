package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.LegalEntityDAO;
import dk.eazyit.eazyregnskab.domain.AppUser;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Trifork
 */
@Service
public class LegalEntityService {

    private Logger log = LoggerFactory.getLogger(LegalEntityService.class);

    @Autowired
    private LegalEntityDAO legalEntityDAO;

    @Transactional(readOnly = true)
    public LegalEntity findLegalEntityById(Long id) {
        log.debug("Finding Legal Entity with id " + id);
        return legalEntityDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public List<LegalEntity> findLegalEntityByUser(AppUser appUser) {
        log.debug("Finding all Legal Entity accessible by " + appUser.getUsername());
        return legalEntityDAO.findByNamedQuery(LegalEntity.QUERY_FIND_LEGAL_ENTITY_BY_USER, appUser);
    }

}
