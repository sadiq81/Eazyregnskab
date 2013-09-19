package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.*;
import dk.eazyit.eazyregnskab.domain.*;
import dk.eazyit.eazyregnskab.util.CalendarUtil;
import org.apache.wicket.model.ResourceModel;
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

    private static final String I25 = "I25";
    private static final String U25 = "U25";
    private static final String REPR = "REPR";
    private static final String EU_I25 = "EU_I25";
    private static final String EU_U25 = "EU_U25";

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
    private void createStandardBaseData(LegalEntity legalEntity) {

        Map<Integer, FinanceAccount> accountMap = new HashMap<>();

        accountMap.put(25000, financeAccountDAO.create(createFinanceAccount("vat", 25000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(25100, financeAccountDAO.create(createFinanceAccount("vat.dk.incoming", 25100, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25200, financeAccountDAO.create(createFinanceAccount("vat.dk.outgoing", 25200, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25300, financeAccountDAO.create(createFinanceAccount("vat.dk.representation", 25300, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25400, financeAccountDAO.create(createFinanceAccount("vat.eur.incoming", 25400, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25500, financeAccountDAO.create(createFinanceAccount("vat.eur.outgoing", 25500, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25600, financeAccountDAO.create(createFinanceAccount("vat.reconciliation", 25600, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25999, financeAccountDAO.create(createFinanceAccount("vat.total", 25999, FinanceAccountType.SUM, null, accountMap.get(25000), legalEntity)));

        Map<String, VatType> vatTypeMap = new HashMap<>();

        vatTypeMap.put(I25, vatTypeDAO.create(new VatType(getString("vat.dk.incoming"), 25D, legalEntity, accountMap.get(25100))));
        vatTypeMap.put(U25, vatTypeDAO.create(new VatType(getString("vat.dk.outgoing"), 25D, legalEntity, accountMap.get(25200))));
        vatTypeMap.put(REPR, vatTypeDAO.create(new VatType(getString("vat.dk.outgoing"), 6.25D, legalEntity, accountMap.get(25300))));
        vatTypeMap.put(EU_I25, vatTypeDAO.create(new VatType(getString("vat.eur.incoming"), 25D, legalEntity, accountMap.get(25100), accountMap.get(25400))));
        vatTypeMap.put(EU_U25, vatTypeDAO.create(new VatType(getString("vat.eur.outgoing"), 25D, legalEntity, accountMap.get(25200), accountMap.get(25500))));

        accountMap.put(1000, financeAccountDAO.create(createFinanceAccount("income.statement", 1000, FinanceAccountType.CATEGORY, null, null, legalEntity)));

        accountMap.put(1100, financeAccountDAO.create(createFinanceAccount("sales", 1100, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(1200, financeAccountDAO.create(createFinanceAccount("sales.dk", 1200, FinanceAccountType.PROFIT, vatTypeMap.get(U25), null, legalEntity)));
        accountMap.put(1300, financeAccountDAO.create(createFinanceAccount("sales.dk.without.vat", 1300, FinanceAccountType.PROFIT, null, null, legalEntity)));
        accountMap.put(1400, financeAccountDAO.create(createFinanceAccount("sales.eur", 1400, FinanceAccountType.PROFIT, vatTypeMap.get(EU_U25), null, legalEntity)));
        accountMap.put(1500, financeAccountDAO.create(createFinanceAccount("sales.world", 1500, FinanceAccountType.PROFIT, null, null, legalEntity)));
        accountMap.put(1999, financeAccountDAO.create(createFinanceAccount("sales.total", 1999, FinanceAccountType.SUM, null, accountMap.get(1100), legalEntity)));

        accountMap.put(2000, financeAccountDAO.create(createFinanceAccount("purchases", 2000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(2100, financeAccountDAO.create(createFinanceAccount("purchases.dk", 2100, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(2200, financeAccountDAO.create(createFinanceAccount("purchases.dk.without.vat", 2200, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(2300, financeAccountDAO.create(createFinanceAccount("purchases.eur", 2300, FinanceAccountType.EXPENSE, vatTypeMap.get(EU_I25), null, legalEntity)));
        accountMap.put(2400, financeAccountDAO.create(createFinanceAccount("purchases.world", 2400, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(2999, financeAccountDAO.create(createFinanceAccount("purchases.total", 2999, FinanceAccountType.SUM, null, accountMap.get(2000), legalEntity)));

        accountMap.put(3000, financeAccountDAO.create(createFinanceAccount("wages", 3000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(3100, financeAccountDAO.create(createFinanceAccount("wages.wages", 3100, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3200, financeAccountDAO.create(createFinanceAccount("wages.pension", 3200, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3300, financeAccountDAO.create(createFinanceAccount("wages.social.cost", 3300, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3400, financeAccountDAO.create(createFinanceAccount("wages.holiday.pay", 3400, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3500, financeAccountDAO.create(createFinanceAccount("wages.driving.reimbursement", 3500, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3600, financeAccountDAO.create(createFinanceAccount("wages.insurance", 3600, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3700, financeAccountDAO.create(createFinanceAccount("wages.lunch", 3700, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3800, financeAccountDAO.create(createFinanceAccount("wages.other.cost", 3800, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(3999, financeAccountDAO.create(createFinanceAccount("wages.total", 3999, FinanceAccountType.SUM, null, accountMap.get(3000), legalEntity)));

        accountMap.put(4000, financeAccountDAO.create(createFinanceAccount("premises", 4000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(4100, financeAccountDAO.create(createFinanceAccount("premises.rent", 4100, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(4200, financeAccountDAO.create(createFinanceAccount("premises.supplies", 4200, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(4300, financeAccountDAO.create(createFinanceAccount("premises.maintenance", 4300, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(4999, financeAccountDAO.create(createFinanceAccount("premises.total", 4999, FinanceAccountType.SUM, null, accountMap.get(4000), legalEntity)));

        accountMap.put(5000, financeAccountDAO.create(createFinanceAccount("administration", 5000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(5050, financeAccountDAO.create(createFinanceAccount("administration.office.supplies", 5050, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5100, financeAccountDAO.create(createFinanceAccount("administration.insurance", 5100, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(5150, financeAccountDAO.create(createFinanceAccount("administration.subscriptions", 5150, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5200, financeAccountDAO.create(createFinanceAccount("administration.subscriptions.without.vat", 5200, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(5250, financeAccountDAO.create(createFinanceAccount("administration.telephone", 5250, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5300, financeAccountDAO.create(createFinanceAccount("administration.internet", 5300, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5350, financeAccountDAO.create(createFinanceAccount("administration.magazines", 5350, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5400, financeAccountDAO.create(createFinanceAccount("administration.finance.assistance", 5400, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5450, financeAccountDAO.create(createFinanceAccount("administration.audit.assistance", 5450, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5500, financeAccountDAO.create(createFinanceAccount("administration.legal.assistance", 5500, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5550, financeAccountDAO.create(createFinanceAccount("administration.bad.debt", 5550, FinanceAccountType.EXPENSE, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(5999, financeAccountDAO.create(createFinanceAccount("administration.total", 5999, FinanceAccountType.SUM, null, accountMap.get(5000), legalEntity)));

        accountMap.put(6000, financeAccountDAO.create(createFinanceAccount("depreciation", 6000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(6100, financeAccountDAO.create(createFinanceAccount("depreciation.inventory", 6100, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(6200, financeAccountDAO.create(createFinanceAccount("depreciation.goodwill", 6200, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(6300, financeAccountDAO.create(createFinanceAccount("depreciation.rights", 6300, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(6400, financeAccountDAO.create(createFinanceAccount("depreciation.operation", 6400, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(6500, financeAccountDAO.create(createFinanceAccount("depreciation.leasehold", 6500, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(6999, financeAccountDAO.create(createFinanceAccount("depreciation.total", 6999, FinanceAccountType.SUM, null, accountMap.get(6000), legalEntity)));

        accountMap.put(7000, financeAccountDAO.create(createFinanceAccount("received.interest", 7000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(7100, financeAccountDAO.create(createFinanceAccount("received.interest.bank", 7100, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(7200, financeAccountDAO.create(createFinanceAccount("received.interest.debtor", 7200, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(7499, financeAccountDAO.create(createFinanceAccount("received.interest.total", 7499, FinanceAccountType.SUM, null, accountMap.get(7000), legalEntity)));

        accountMap.put(7500, financeAccountDAO.create(createFinanceAccount("paid.interest", 7500, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(7600, financeAccountDAO.create(createFinanceAccount("paid.interest.bank", 7600, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(7700, financeAccountDAO.create(createFinanceAccount("paid.interest.creditor", 7700, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(7800, financeAccountDAO.create(createFinanceAccount("paid.interest.public", 7800, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(7999, financeAccountDAO.create(createFinanceAccount("paid.interest.total", 7999, FinanceAccountType.SUM, null, accountMap.get(7500), legalEntity)));

        accountMap.put(8000, financeAccountDAO.create(createFinanceAccount("tax", 8000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(8100, financeAccountDAO.create(createFinanceAccount("tax.company", 8100, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(8200, financeAccountDAO.create(createFinanceAccount("tax.company.previous.year", 8200, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(8300, financeAccountDAO.create(createFinanceAccount("tax.deferred.tax", 8300, FinanceAccountType.EXPENSE, null, null, legalEntity)));
        accountMap.put(8999, financeAccountDAO.create(createFinanceAccount("tax.total", 8999, FinanceAccountType.SUM, null, accountMap.get(8000), legalEntity)));

        accountMap.put(9999, financeAccountDAO.create(createFinanceAccount("income.statement.total", 9999, FinanceAccountType.CATEGORY_SUM, null, accountMap.get(1000), legalEntity)));

        accountMap.put(10000, financeAccountDAO.create(createFinanceAccount("assets", 10000, FinanceAccountType.CATEGORY, null, null, legalEntity)));

        accountMap.put(11000, financeAccountDAO.create(createFinanceAccount("inventory", 11000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(11100, financeAccountDAO.create(createFinanceAccount("inventory.acquisition.cost.start", 11100, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(11200, financeAccountDAO.create(createFinanceAccount("inventory.acquisition.cost", 11200, FinanceAccountType.ASSET, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(11300, financeAccountDAO.create(createFinanceAccount("inventory.disposals", 11300, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(11400, financeAccountDAO.create(createFinanceAccount("inventory.depreciation.start", 11400, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(11500, financeAccountDAO.create(createFinanceAccount("inventory.depreciation", 11500, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(11600, financeAccountDAO.create(createFinanceAccount("inventory.depreciation.on.disposals", 11600, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(11999, financeAccountDAO.create(createFinanceAccount("inventory.total", 11999, FinanceAccountType.SUM, null, accountMap.get(11000), legalEntity)));

        accountMap.put(12000, financeAccountDAO.create(createFinanceAccount("goodwill", 12000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(12100, financeAccountDAO.create(createFinanceAccount("goodwill.acquisition.cost.start", 12100, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(12200, financeAccountDAO.create(createFinanceAccount("goodwill.acquisition.cost", 12200, FinanceAccountType.ASSET, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(12300, financeAccountDAO.create(createFinanceAccount("goodwill.disposals", 12300, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(12400, financeAccountDAO.create(createFinanceAccount("goodwill.depreciation.start", 12400, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(12500, financeAccountDAO.create(createFinanceAccount("goodwill.depreciation", 12500, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(12600, financeAccountDAO.create(createFinanceAccount("goodwill.depreciation.on.disposals", 12600, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(12999, financeAccountDAO.create(createFinanceAccount("goodwill.total", 12999, FinanceAccountType.SUM, null, accountMap.get(12000), legalEntity)));

        accountMap.put(13000, financeAccountDAO.create(createFinanceAccount("rights", 13000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(13100, financeAccountDAO.create(createFinanceAccount("rights.acquisition.cost.start", 13100, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(13200, financeAccountDAO.create(createFinanceAccount("rights.acquisition.cost", 13200, FinanceAccountType.ASSET, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(13300, financeAccountDAO.create(createFinanceAccount("rights.disposals", 13300, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(13400, financeAccountDAO.create(createFinanceAccount("rights.depreciation.start", 13400, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(13500, financeAccountDAO.create(createFinanceAccount("rights.depreciation", 13500, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(13600, financeAccountDAO.create(createFinanceAccount("rights.depreciation.on.disposals", 13600, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(13999, financeAccountDAO.create(createFinanceAccount("rights.total", 13999, FinanceAccountType.SUM, null, accountMap.get(13000), legalEntity)));

        accountMap.put(14000, financeAccountDAO.create(createFinanceAccount("operation", 14000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(14100, financeAccountDAO.create(createFinanceAccount("operation.acquisition.cost.start", 14100, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(14200, financeAccountDAO.create(createFinanceAccount("operation.acquisition.cost", 14200, FinanceAccountType.ASSET, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(14300, financeAccountDAO.create(createFinanceAccount("operation.disposals", 14300, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(14400, financeAccountDAO.create(createFinanceAccount("operation.depreciation.start", 14400, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(14500, financeAccountDAO.create(createFinanceAccount("operation.depreciation", 14500, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(14600, financeAccountDAO.create(createFinanceAccount("operation.depreciation.on.disposals", 14600, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(14999, financeAccountDAO.create(createFinanceAccount("operation.total", 14999, FinanceAccountType.SUM, null, accountMap.get(14000), legalEntity)));

        accountMap.put(15000, financeAccountDAO.create(createFinanceAccount("leasehold", 15000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(15100, financeAccountDAO.create(createFinanceAccount("leasehold.acquisition.cost.start", 15100, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(15200, financeAccountDAO.create(createFinanceAccount("leasehold.acquisition.cost", 15200, FinanceAccountType.ASSET, vatTypeMap.get(I25), null, legalEntity)));
        accountMap.put(15300, financeAccountDAO.create(createFinanceAccount("leasehold.disposals", 15300, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(15400, financeAccountDAO.create(createFinanceAccount("leasehold.depreciation.start", 15400, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(15500, financeAccountDAO.create(createFinanceAccount("leasehold.depreciation", 15500, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(15600, financeAccountDAO.create(createFinanceAccount("leasehold.depreciation.on.disposals", 15600, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(15999, financeAccountDAO.create(createFinanceAccount("leasehold.total", 15999, FinanceAccountType.SUM, null, accountMap.get(15000), legalEntity)));

        accountMap.put(16000, financeAccountDAO.create(createFinanceAccount("other.assets", 16000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(16100, financeAccountDAO.create(createFinanceAccount("other.assets.debtors", 16100, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(16200, financeAccountDAO.create(createFinanceAccount("other.assets.receivables", 16200, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(16999, financeAccountDAO.create(createFinanceAccount("other.assets.total", 16999, FinanceAccountType.SUM, null, accountMap.get(16000), legalEntity)));

        accountMap.put(17000, financeAccountDAO.create(createFinanceAccount("liquid.assets", 17000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(17100, financeAccountDAO.create(createFinanceAccount("liquid.assets.cash", 17100, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(17200, financeAccountDAO.create(createFinanceAccount("liquid.assets.bank.account", 17200, FinanceAccountType.ASSET, null, null, legalEntity)));
        accountMap.put(17999, financeAccountDAO.create(createFinanceAccount("liquid.assets.total", 17999, FinanceAccountType.SUM, null, accountMap.get(17000), legalEntity)));

        accountMap.put(19999, financeAccountDAO.create(createFinanceAccount("assets.total", 19999, FinanceAccountType.CATEGORY_SUM, null, accountMap.get(10000), legalEntity)));

        accountMap.put(20000, financeAccountDAO.create(createFinanceAccount("liabilities", 20000, FinanceAccountType.CATEGORY, null, null, legalEntity)));

        accountMap.put(21000, financeAccountDAO.create(createFinanceAccount("equity", 21000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(21100, financeAccountDAO.create(createFinanceAccount("equity.share.capital", 21100, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(21200, financeAccountDAO.create(createFinanceAccount("equity.retained.earnings", 21200, FinanceAccountType.YEAR_END, null, null, legalEntity)));
        accountMap.put(21300, financeAccountDAO.create(createFinanceAccount("equity.current.result", 21300, FinanceAccountType.CURRENT_RESULT, null, null, legalEntity)));
        accountMap.put(21999, financeAccountDAO.create(createFinanceAccount("equity.total", 21999, FinanceAccountType.SUM, null, accountMap.get(21000), legalEntity)));

        accountMap.put(22000, financeAccountDAO.create(createFinanceAccount("provision", 22000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(22100, financeAccountDAO.create(createFinanceAccount("provision.deferred.tax", 22100, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(22999, financeAccountDAO.create(createFinanceAccount("provision.total", 22999, FinanceAccountType.SUM, null, accountMap.get(22000), legalEntity)));

        accountMap.put(23000, financeAccountDAO.create(createFinanceAccount("long.term.debt", 23000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(23100, financeAccountDAO.create(createFinanceAccount("long.term.debt.loan", 23100, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(23999, financeAccountDAO.create(createFinanceAccount("long.term.debt.total", 23999, FinanceAccountType.SUM, null, accountMap.get(23000), legalEntity)));

        accountMap.put(24000, financeAccountDAO.create(createFinanceAccount("short.term.debt", 24000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(24100, financeAccountDAO.create(createFinanceAccount("short.term.debt.bank", 24100, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(24200, financeAccountDAO.create(createFinanceAccount("short.term.debt.trade.creditors", 24200, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(24999, financeAccountDAO.create(createFinanceAccount("short.term.debt.total", 24999, FinanceAccountType.SUM, null, accountMap.get(24000), legalEntity)));

        accountMap.put(25000, financeAccountDAO.create(createFinanceAccount("vat", 25000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(25100, financeAccountDAO.create(createFinanceAccount("vat.dk.incoming", 25100, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25200, financeAccountDAO.create(createFinanceAccount("vat.dk.outgoing", 25200, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25300, financeAccountDAO.create(createFinanceAccount("vat.dk.representation", 25300, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25400, financeAccountDAO.create(createFinanceAccount("vat.eur.incoming", 25400, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25500, financeAccountDAO.create(createFinanceAccount("vat.eur.outgoing", 25500, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25600, financeAccountDAO.create(createFinanceAccount("vat.reconciliation", 25600, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(25999, financeAccountDAO.create(createFinanceAccount("vat.total", 25999, FinanceAccountType.SUM, null, accountMap.get(25000), legalEntity)));

        accountMap.put(26000, financeAccountDAO.create(createFinanceAccount("other.debt", 26000, FinanceAccountType.HEADLINE, null, null, legalEntity)));
        accountMap.put(26100, financeAccountDAO.create(createFinanceAccount("other.debt.employee.tax.a-skat", 26100, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(26200, financeAccountDAO.create(createFinanceAccount("other.debt.employee.tax.am", 26200, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(26300, financeAccountDAO.create(createFinanceAccount("other.debt.employee.atp", 26300, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(26400, financeAccountDAO.create(createFinanceAccount("other.debt.employee.pension", 26400, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(26500, financeAccountDAO.create(createFinanceAccount("other.debt.employee.wages", 26500, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(26600, financeAccountDAO.create(createFinanceAccount("other.debt.employee.holiday.pay", 26600, FinanceAccountType.LIABILITY, null, null, legalEntity)));
        accountMap.put(26999, financeAccountDAO.create(createFinanceAccount("other.debt.total", 26999, FinanceAccountType.SUM, null, accountMap.get(26000), legalEntity)));

        accountMap.put(29999, financeAccountDAO.create(createFinanceAccount("liabilities.total", 29999, FinanceAccountType.CATEGORY_SUM, null, accountMap.get(20000), legalEntity)));

        accountMap.put(30000, financeAccountDAO.create(createFinanceAccount("check", 30000, FinanceAccountType.BALANCE_CHECK, null, null, legalEntity)));

        legalEntity.getDailyLedgers().add(dailyLedgerDAO.create(new DailyLedger(getString("start.ledger.name"), legalEntity)));

        fiscalYearService.save(new FiscalYear(CalendarUtil.getFirstDayInYear(), CalendarUtil.getLastDayInYear(), legalEntity));

    }


    public FinanceAccount createFinanceAccount(String name, Integer accountNumber, FinanceAccountType type, VatType vatType, FinanceAccount sumFrom, LegalEntity legalEntity) {
        FinanceAccount financeAccount = new FinanceAccount(getString(name), accountNumber, type, vatType, sumFrom, legalEntity);
        if (FinanceAccountType.SUM == type || FinanceAccountType.CATEGORY_SUM == type) {
            financeAccount.setSumTo(financeAccount);
        }
        return financeAccount;
    }


    @Transactional
    public LegalEntity createLegalEntity(AppUser appUser) {
        log.debug("Creating new Legal Entity for user " + appUser.getUsername());
        LegalEntity legalEntity = createBaseDataForNewLegalEntity(new LegalEntity());
        legalEntityAccessDAO.create(new LegalEntityAccess(appUser, legalEntity));
        return legalEntity;
    }

    @Transactional
    public LegalEntity createBaseDataForNewLegalEntity(LegalEntity dummy) {
        LegalEntity legalEntity = new LegalEntity(getString("new.entity.name"), getString("new.entity.legalIdentification"), getString("new.entity.address"), getString("new.entity.postalCode"), Country.DNK, MoneyCurrency.DKK);
        legalEntityDAO.create(legalEntity);

        createStandardBaseData(legalEntity);

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

    private String getString(String resourceText) {
        return new ResourceModel(resourceText).getObject();
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
