package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.*;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
import org.apache.wicket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Eazy IT
 */
@Service("legalEntityService")
public class LegalEntityService {

    private Logger log = LoggerFactory.getLogger(LegalEntityService.class);

    @Autowired
    FiscalYearService fiscalYearService;
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
    public LegalEntity createBaseDataForNewLegalEntity(LegalEntity legalEntity) {

        ResourceBundle bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntity", Session.get().getLocale());
        legalEntity.setName(bundle.getString("new.entity.name"));
        legalEntity.setLegalIdentification(bundle.getString("new.entity.legalIdentification"));
        legalEntity.setAddress(bundle.getString("new.entity.address"));
        legalEntity.setPostalCode(bundle.getString("new.entity.postalCode"));
        legalEntity.setMoneyCurrency(MoneyCurrency.DKK);
        legalEntity.setCountry(Country.DNK);
        legalEntityDAO.create(legalEntity);

        bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntityVatTypes", Session.get().getLocale());

        FinanceAccount incomingVat = new FinanceAccount(bundle.getString("new.entity.finance.account.LIABILITY.vat.incoming"),
                Integer.parseInt(bundle.getString("new.entity.finance.account.LIABILITY.vat.incoming.account.number")),
                FinanceAccountType.LIABILITY, legalEntity);
        financeAccountDAO.create(incomingVat);
        FinanceAccount outgoingVat = new FinanceAccount(bundle.getString("new.entity.finance.account.LIABILITY.vat.outgoing"),
                Integer.parseInt(bundle.getString("new.entity.finance.account.LIABILITY.vat.outgoing.account.number")),
                FinanceAccountType.LIABILITY, legalEntity);
        financeAccountDAO.create(outgoingVat);
        FinanceAccount repr = new FinanceAccount(bundle.getString("new.entity.finance.account.LIABILITY.vat.representation"),
                Integer.parseInt(bundle.getString("new.entity.finance.account.LIABILITY.vat.representation.account.number")),
                FinanceAccountType.LIABILITY, legalEntity);
        financeAccountDAO.create(repr);

        FinanceAccount incomingVatEU = new FinanceAccount(bundle.getString("new.entity.finance.account.LIABILITY.vat.incoming.eu"),
                Integer.parseInt(bundle.getString("new.entity.finance.account.LIABILITY.vat.incoming.eu.account.number")),
                FinanceAccountType.LIABILITY, legalEntity);
        financeAccountDAO.create(incomingVatEU);

        FinanceAccount outgoingVatEU = new FinanceAccount(bundle.getString("new.entity.finance.account.LIABILITY.vat.outgoing.eu"),
                Integer.parseInt(bundle.getString("new.entity.finance.account.LIABILITY.vat.outgoing.eu.account.number")),
                FinanceAccountType.LIABILITY, legalEntity);
        financeAccountDAO.create(outgoingVatEU);

        VatType incoming = new VatType(bundle.getString("new.entity.vat.types.incoming"), new Double(bundle.getString("new.entity.vat.types.incoming.percentage")), legalEntity, incomingVat);
        VatType outgoing = new VatType(bundle.getString("new.entity.vat.types.outgoing"), new Double(bundle.getString("new.entity.vat.types.outgoing.percentage")), legalEntity, outgoingVat);
        VatType representation = new VatType(bundle.getString("new.entity.vat.types.representation"), new Double(bundle.getString("new.entity.vat.types.representation.percentage")), legalEntity, repr);
        VatType EUIncoming = new VatType(bundle.getString("new.entity.vat.types.incoming.eu"), new Double(bundle.getString("new.entity.vat.types.incoming.eu.percentage")), legalEntity, incomingVat, incomingVatEU);
        VatType EUOutgoing = new VatType(bundle.getString("new.entity.vat.types.outgoing.eu"), new Double(bundle.getString("new.entity.vat.types.outgoing.eu.percentage")), legalEntity, outgoingVat, outgoingVatEU);

        vatTypeDAO.create(incoming);
        vatTypeDAO.create(outgoing);
        vatTypeDAO.create(representation);
        vatTypeDAO.create(EUIncoming);
        vatTypeDAO.create(EUOutgoing);

        bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntityFinanceAccounts", Session.get().getLocale());
        Map<String, String> map = convertResourceBundleToMap(bundle);

        Map<Integer, FinanceAccount> financeAccounts = new HashMap<Integer, FinanceAccount>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            FinanceAccount financeAccount = null;
            if (entry.getKey().contains("PROFIT") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {

                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.PROFIT, legalEntity);

            } else if (entry.getKey().contains("EXPENSE") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {

                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.EXPENSE, legalEntity);

            } else if (entry.getKey().contains("ASSET") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {

                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.ASSET, legalEntity);

            } else if (entry.getKey().contains("LIABILITY") && !entry.getKey().contains("account.number") && !entry.getKey().contains("vat.type")) {

                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.LIABILITY, legalEntity);

            } else if (entry.getKey().contains("CATEGORY") && !entry.getKey().contains("account.number")) {

                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.CATEGORY, legalEntity);

            } else if (entry.getKey().contains("HEADLINE") && !entry.getKey().contains("account.number")) {

                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.HEADLINE, legalEntity);

            } else if (entry.getKey().contains("SYSTEM") && !entry.getKey().contains("account.number") && !entry.getKey().contains("account.type")) {

                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                String accountType = map.get(entry.getKey() + ".account.type");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.valueOf(accountType), legalEntity);
            }

            if (financeAccount != null && map.get(entry.getKey() + ".vat.type") != null) {
                String vatType = map.get(entry.getKey() + ".vat.type");
                if (vatType.contains("I25") && !vatType.contains("EU_I25")) {
                    financeAccount.setVatType(incoming);
                } else if (vatType.contains("U25") && !vatType.contains("EU_U25")) {
                    financeAccount.setVatType(outgoing);
                } else if (vatType.contains("REPR")) {
                    financeAccount.setVatType(representation);
                } else if (vatType.contains("EU_I25")) {
                    financeAccount.setVatType(EUIncoming);
                } else if (vatType.contains("EU_U25")) {
                    financeAccount.setVatType(EUOutgoing);
                }
            }
            if (financeAccount != null) {
                financeAccounts.put(financeAccount.getAccountNumber(), financeAccount);
                financeAccountDAO.create(financeAccount);
            }
        }

        //TODO create new bundle for sum accounts
        for (Map.Entry<String, String> entry : map.entrySet()) {
            FinanceAccount financeAccount = null;

            if (entry.getKey().contains("SUM") && !entry.getKey().contains("account.number") && !entry.getKey().contains("sum.from") && !entry.getKey().contains("sum.to")) {
                String name = entry.getValue();
                String accountNumber = map.get(entry.getKey() + ".account.number");
                financeAccount = new FinanceAccount(name, Integer.parseInt(accountNumber), FinanceAccountType.SUM, legalEntity);
                financeAccounts.put(financeAccount.getAccountNumber(), financeAccount);
                FinanceAccount from = financeAccounts.get(Integer.parseInt(map.get(entry.getKey() + ".sum.from")));
                FinanceAccount to = financeAccounts.get(Integer.parseInt(map.get(entry.getKey() + ".sum.to")));
                financeAccount.setSumFrom(from);
                financeAccount.setSumTo(to);
            }

            if (financeAccount != null) {

                financeAccountDAO.create(financeAccount);
            }

        }

        financeAccounts = null;

        bundle = PropertyResourceBundle.getBundle("dk.eazyit.eazyregnskab.services.newEntityDailyLedger", Session.get().getLocale());

        DailyLedger dailyLedger = new DailyLedger(bundle.getString("start.ledger.name"), legalEntity);
        dailyLedgerDAO.create(dailyLedger);
        legalEntity.getDailyLedgers().add(dailyLedger);

        FiscalYear fiscalYear = new FiscalYear(CalendarUtil.getFirstDayInYear(), CalendarUtil.getLastDayInYear(), legalEntity);
        fiscalYearService.save(fiscalYear);

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
    public boolean deleteLegalEntity(AppUser appUser, LegalEntity legalEntity) {
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
            for (VatType vatType : vatTypeDAO.findByNamedQuery(VatType.QUERY_FIND_VATTYPE_BY_LEGAL_ENTITY, legalEntity)) {
                vatTypeDAO.delete(vatType);
            }
            for (FinanceAccount financeAccount : financeAccountDAO.findByNamedQuery(FinanceAccount.QUERY_FIND_BY_LEGAL_ENTITY, legalEntity)) {
                //TODO delete booked finance postings
                financeAccountDAO.delete(financeAccount);
            }
            legalEntityDAO.delete(legalEntity);
            return true;
        } else {
            log.warn("Not able to delete legal entity " + legalEntity.toString() + " because it was the last");
            return false;
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
