package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.VatTypeDAO;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import dk.eazyit.eazyregnskab.domain.VatType;
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

    @Autowired
    FinanceAccountService financeAccountService;
    @Autowired
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
        List<VatType> list = vatTypeDAO.findByNamedQuerySorted(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), sortProperty, Ascending, legalEntity);
        return list;
    }

    @Transactional
    public int countVatTypesOfLegalEntity(LegalEntity legalEntity) {
        LOG.debug("Couting all VatType from legalEntity " + legalEntity.toString());
        return vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity).size();
    }

    @Transactional
    public boolean isDeleteVatTypeAllowed(VatType vatType) {
        return !vatType.isInUse() && postingService.findDraftFinancePostingsByVatTypeSubList(vatType, 0, 1).size() == 0;
    }

    @Transactional
    public void deleteVatType(VatType vatType) {
        vatTypeDAO.delete(vatType);
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
        LOG.debug("Finding all VatType from legalEntity  " + legalEntity.toString() + " by name " + name);
        return vatTypeDAO.findByNamedQueryUnique(VatType.QUERY_FIND_VATTYPE_BY_NAME_AND_LEGAL_ENTITY, name, legalEntity);
    }

    public void setVatTypeInUse(VatType vatType) {
        vatType.setInUse(true);
        vatTypeDAO.save(vatType);
    }
}
