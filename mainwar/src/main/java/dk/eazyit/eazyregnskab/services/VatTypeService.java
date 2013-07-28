package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.VatType;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author
 */
@Service("vatTypeService")
public class VatTypeService {

    @SpringBean
    FinanceAccountService financeAccountService;
    @SpringBean
    PostingService postingService;

    private Logger LOG = LoggerFactory.getLogger(VatTypeService.class);

    @Autowired
    private VatTypeDAO vatTypeDAO;

    @Transactional(readOnly = true)
    public List<VatType> findAllVatTypesForLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Finding all VatType from legal entity " + legalEntity.toString());
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity);
    }

    @Transactional
    public List<VatType> findVatTypeByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        LOG.debug("Finding all VatType from legalEntity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<VatType> list = vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<VatType> findVatTypeByLegalEntitySubListSortBy(LegalEntity legalEntity, int first, int count, String sortProperty, boolean Ascending) {
        LOG.debug("Finding all VatType from legalEntity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<VatType> list = vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), sortProperty, Ascending, legalEntity);
        return list;
    }

    @Transactional
    public int countVatTypesOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Couting all VatType from legalEntity " + legalEntity.toString());
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional(readOnly = true)
    public boolean isDeletingVatTypeAllowed(VatType vatType) {
        return postingService.findDraftFinancePostingsByVatType(vatType).size() == 0 &&
                postingService.findBookedFinancePostingsByVatType(vatType).size() == 0;

    }

    @Transactional
    public boolean deleteVatType(VatType vatType) {
        if (isDeletingVatTypeAllowed(vatType)) {
            vatTypeDAO.delete(vatType);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void saveVatType(VatType vatType, LegalEntity legalEntity) {
        if (vatType.getId() == 0) {
            vatType.setLegalEntity(legalEntity);
            vatTypeDAO.create(vatType);
        }
        vatTypeDAO.save(vatType);
    }

    public VatType findVatTypeByNameAndLegalEntity(LegalEntity legalEntity, String name) {
        return vatTypeDAO.findByNamedQueryUnique(VatType.QUERY_FIND_VATTYPE_BY_NAME_AND_LEGAL_ENTITY, name, legalEntity);
    }
}
