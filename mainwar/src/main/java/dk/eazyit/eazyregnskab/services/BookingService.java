package dk.eazyit.eazyregnskab.services;

import dk.eazyit.eazyregnskab.dao.interfaces.BookedFinancePostingDAO;
import dk.eazyit.eazyregnskab.dao.interfaces.DraftFinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.DraftFinancePosting;
import dk.eazyit.eazyregnskab.domain.DraftFinancePostingBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Service
public class BookingService {


    @Autowired
    BookedFinancePostingDAO bookedFinancePostingDAO;
    @Autowired
    DraftFinancePostingDAO draftFinancePostingDAO;
    @Autowired
    FinanceAccountService financeAccountService;

    @Transactional
    public void BookChosen(DailyLedger dailyLedger) {
        List<DraftFinancePosting> draftFinancePostingList = financeAccountService.findPostingsFromDailyLedger(dailyLedger);
        List<DraftFinancePosting> markedForSave = new ArrayList<DraftFinancePosting>();
        for (DraftFinancePosting draftFinancePosting : draftFinancePostingList) {
            if (draftFinancePosting.isChosen()) {
                markedForSave.add(draftFinancePosting);
            }
        }
        processDraftFinancePostings(markedForSave);
    }

    @Transactional
    public void BookAll(DailyLedger dailyLedger) {
        List<DraftFinancePosting> draftFinancePostingList = financeAccountService.findPostingsFromDailyLedger(dailyLedger);
        processDraftFinancePostings(draftFinancePostingList);
    }

    @Transactional
    private void processDraftFinancePostings(List<DraftFinancePosting> draftFinancePostings) {

        Map<Integer, DraftFinancePostingBatch> map = new HashMap<Integer, DraftFinancePostingBatch>();

        for (DraftFinancePosting draftFinancePosting : draftFinancePostings) {

            DraftFinancePostingBatch batch = map.get(draftFinancePosting.getBookingNumber());
            if (batch == null) {
                batch = new DraftFinancePostingBatch(draftFinancePosting.getBookingNumber());
            }
            batch.getList().add(draftFinancePosting);
            map.put(draftFinancePosting.getBookingNumber(), batch);
        }
        seperateSingleFromMultiple(map);
    }

    @Transactional
    private void seperateSingleFromMultiple(Map<Integer, DraftFinancePostingBatch> map) {

        for (Map.Entry<Integer, DraftFinancePostingBatch> entry : map.entrySet()) {

            if (entry.getValue().getList().size() == 1) {
                singleDraftFinancePosting(entry.getValue());
            } else {
                multipleDraftFinancePosting(entry.getValue());
            }
        }
    }

    private void singleDraftFinancePosting(DraftFinancePostingBatch draftFinancePostingBatch) {

        DraftFinancePosting draftFinancePosting = draftFinancePostingBatch.getList().get(0);

        if (draftFinancePosting.getFinanceAccount() == null && draftFinancePosting.getReverseFinanceAccount() == null) {
            //Error
        } else {

            BookedFinancePosting posting = setupBaseData(draftFinancePosting);
            BookedFinancePosting reverse = setupBaseData(draftFinancePosting);
            BookedFinancePosting vat = null;

            posting.setAmount(draftFinancePosting.getAmount());
            posting.setFinanceAccount(draftFinancePosting.getFinanceAccount());

            reverse.setAmount(0 - draftFinancePosting.getAmount());
            reverse.setFinanceAccount(draftFinancePosting.getReverseFinanceAccount());

            if (draftFinancePosting.getVatType() != null) {
                vat = setupBaseData(draftFinancePosting);
                vat.setAmount(draftFinancePosting.getAmount() * (draftFinancePosting.getVatType().getPercentage() / 100));
                vat.setFinanceAccount(draftFinancePosting.getVatType().getFinanceAccount());
                posting.setAmount(posting.getAmount() - vat.getAmount());
            }
            bookedFinancePostingDAO.save(posting);
            bookedFinancePostingDAO.save(reverse);
            if (vat != null) bookedFinancePostingDAO.save(vat);
        }
        draftFinancePostingDAO.delete(draftFinancePosting);
    }

    private void multipleDraftFinancePosting(DraftFinancePostingBatch draftFinancePostingBatch) {

    }

    private BookedFinancePosting setupBaseData(DraftFinancePosting draftFinancePosting) {
        BookedFinancePosting bookedFinancePosting = new BookedFinancePosting();
        bookedFinancePosting.setDate(draftFinancePosting.getDate());
        bookedFinancePosting.setBookingNumber(draftFinancePosting.getBookingNumber());
        bookedFinancePosting.setText(draftFinancePosting.getText());
        return bookedFinancePosting;
    }
}
