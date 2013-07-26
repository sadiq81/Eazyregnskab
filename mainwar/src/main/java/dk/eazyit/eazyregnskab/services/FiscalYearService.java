package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.FiscalYearDAO;
import dk.eazyit.eazyregnskab.domain.FiscalYear;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author
 */
@Service("fiscalYearService")
public class FiscalYearService {

    private Logger log = LoggerFactory.getLogger(LegalEntityService.class);

    @Autowired
    FiscalYearDAO fiscalYearDAO;

    @Transactional
    public boolean save(FiscalYear fiscalYear) {

        //TODO on change check if year contains postings beyond changed dates

        //TODO Checks for date interval
        fiscalYearDAO.save(fiscalYear);
        return true;
    }

    @Transactional
    public boolean deleteFiscalYear(FiscalYear fiscalYear) {

        //TODO checks for postings
        fiscalYearDAO.delete(fiscalYear);
        return true;
    }

    @Transactional
    public List<FiscalYear> findFiscalYearByLegalEntitySubList(LegalEntity legalEntity, int first, int count) {
        log.debug("Finding all FiscalYear from legal entity starting with " + first + " to  " + count + " from " + legalEntity.toString());
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_BY_LEGAL_ENTITY, new Integer(first), new Integer(count), legalEntity);
        return list;
    }

    @Transactional
    public List<FiscalYear> findFiscalYearByLegalEntity(LegalEntity legalEntity) {
        log.debug("Finding all FiscalYear from legal entity from " + legalEntity.toString());
        List<FiscalYear> list = fiscalYearDAO.findByNamedQuery(FiscalYear.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity);
        return list;
    }
}