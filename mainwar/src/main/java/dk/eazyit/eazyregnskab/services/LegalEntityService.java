package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.*;
import dk.eazyit.eazyregnskab.domain.*;
import org.apache.wicket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private DailyLedgerDAO dailyLedgerDAO;
    @Autowired
    private DraftFinancePostingDAO draftFinancePostingDAO;
    @Autowired
    private FinanceAccountDAO financeAccountDAO;
    @Autowired
    private VatTypeDAO vatTypeDAO;


    @Transactional
    public LegalEntity createLegalEntity(AppUser appUser) {
        log.debug("Creating new Legal Entity for user " + appUser.getUsername());
        LegalEntity legalEntity = createBaseDataForNewLegalEntity(new LegalEntity());
        legalEntityAccessDAO.create(new LegalEntityAccess(appUser, legalEntity));
        return legalEntity;
    }

    @Transactional
    private LegalEntity createBaseDataForNewLegalEntity(LegalEntity legalEntity) {

        ResourceBundle bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntity", Session.get().getLocale());
        legalEntity.setName(bundle.getString("new.entity.name"));
        legalEntity.setLegalIdentification(bundle.getString("new.entity.legalIdentification"));
        legalEntity.setAddress(bundle.getString("new.entity.address"));
        legalEntity.setPostalCode(bundle.getString("new.entity.postalCode"));
        legalEntity.setMoneyCurrency(MoneyCurrency.DKK);
        legalEntity.setCountry(Country.DK);
        legalEntityDAO.create(legalEntity);

        bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntityVatTypes", Session.get().getLocale());

        VatType incoming = new VatType(bundle.getString("new.entity.vat.types.incoming"), new BigDecimal(bundle.getString("new.entity.vat.types.incoming.percentage")), legalEntity);
        VatType outgoing = new VatType(bundle.getString("new.entity.vat.types.outgoing"), new BigDecimal(bundle.getString("new.entity.vat.types.outgoing.percentage")), legalEntity);
        VatType representation = new VatType(bundle.getString("new.entity.vat.types.representation"), new BigDecimal(bundle.getString("new.entity.vat.types.representation.percentage")), legalEntity);
        vatTypeDAO.create(incoming);
        vatTypeDAO.create(outgoing);
        vatTypeDAO.create(representation);

        bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntityFinanceAccounts", Session.get().getLocale());
        Map<String, String> map = convertResourceBundleToMap(bundle);
        map = convertResourceBundleToMap(bundle);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            FinanceAccount financeAccount = null;
            if (entry.getKey().contains("PROFIT") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {
                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, accountNumber, FinanceAccountType.PROFIT, legalEntity);
            } else if (entry.getKey().contains("EXPENSE") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {
                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, accountNumber, FinanceAccountType.EXPENSE, legalEntity);
            } else if (entry.getKey().contains("ASSET") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {
                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, accountNumber, FinanceAccountType.ASSET, legalEntity);
            } else if (entry.getKey().contains("LIABILITY") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {
                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, accountNumber, FinanceAccountType.LIABILITY, legalEntity);
            }

            if (financeAccount != null && map.get(entry.getKey() + ".vat.type") != null) {
                String vatType = map.get(entry.getKey() + ".vat.type");
                if (vatType.contains("I25")) {
                    financeAccount.setVatType(incoming);
                } else if (vatType.contains("U25")) {
                    financeAccount.setVatType(outgoing);
                } else if (vatType.contains("REPR")) {
                    financeAccount.setVatType(representation);
                }
            }
            if (financeAccount != null) {
                financeAccountDAO.create(financeAccount);
            }
        }
        bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntityDailyLedger", Session.get().getLocale());
        dailyLedgerDAO.create(new DailyLedger(bundle.getString("start.ledger.name"), legalEntity));

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

    //TODO check for financepostings and ask for confirm dialog
    @Transactional
    public void deleteLegalEntity(AppUser appUser, LegalEntity legalEntity) {
        if (isDeletingAllowed(appUser, legalEntity)) {
            log.debug("Deleting legal entity " + legalEntity.toString());
            for (LegalEntityAccess legalEntityAccess : findLegalEntityAccessByLegalEntity(legalEntity)) {
                legalEntityAccessDAO.delete(legalEntityAccess);
            }
            for (DailyLedger dailyLedger : dailyLedgerDAO.findByNamedQuery(DailyLedger.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity)) {
                for (DraftFinancePosting draftFinancePosting : draftFinancePostingDAO.findByNamedQuery(DraftFinancePosting.QUERY_FIND_FINANCE_POSTING_BY_DAILY_LEDGER, dailyLedger)) {
                    draftFinancePostingDAO.delete(draftFinancePosting);
                }
                dailyLedgerDAO.delete(dailyLedger);

            }
            for (FinanceAccount financeAccount : financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity)) {
                //TODO delete booked finance postings
                financeAccountDAO.delete(financeAccount);
            }
            for (VatType vatType : vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity)) {
                vatTypeDAO.delete(vatType);
            }

            legalEntityDAO.delete(legalEntity);
        } else {
            log.warn("Not able to delete legal entity " + legalEntity.toString() + " because it was the last");
        }
    }

    //TODO check for finance postings
    @Transactional(readOnly = true)
    public boolean isDeletingAllowed(AppUser appUser, LegalEntity legalEntity) {
        if (findLegalEntityByUser(appUser).size() <= 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Convert ResourceBundle into a Map object.
     *
     * @param resource a resource bundle to convert.
     * @return Map a map version of the resource bundle.
     *         Found http://www.kodejava.org/examples/340.html
     */
    private static Map<String, String> convertResourceBundleToMap(ResourceBundle resource) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            map.put(key, resource.getString(key));
        }
        return map;
    }
}
