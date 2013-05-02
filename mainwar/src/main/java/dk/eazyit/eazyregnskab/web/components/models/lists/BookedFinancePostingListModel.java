package dk.eazyit.eazyregnskab.web.components.models.lists;

import dk.eazyit.eazyregnskab.domain.BookedFinancePosting;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.services.FinanceAccountService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author EazyIT
 */
public class BookedFinancePostingListModel extends AbstractEntityListModel<BookedFinancePosting, FinanceAccount> {

    @SpringBean
    FinanceAccountService financeAccountService;

    static final Logger LOG = LoggerFactory.getLogger(BookedFinancePostingListModel.class);

    public BookedFinancePostingListModel() {
        Injector.get().inject(this);
    }

    public BookedFinancePostingListModel(FinanceAccount parent) {
        super(parent);
        Injector.get().inject(this);
    }

    public BookedFinancePostingListModel(FinanceAccount parent, List<BookedFinancePosting> list) {
        super(parent, list);
        Injector.get().inject(this);
    }

    @Override
    protected List<BookedFinancePosting> load(FinanceAccount id) {

        List<BookedFinancePosting> list1 = financeAccountService.findPostingsFromFinanceAccount(id);
        List<BookedFinancePosting> sortedAndSummed = new ArrayList<BookedFinancePosting>();
        Collections.sort(list1, new Comparator<BookedFinancePosting>() {
            @Override
            public int compare(BookedFinancePosting bookedFinancePosting, BookedFinancePosting bookedFinancePosting2) {
                return bookedFinancePosting.getDate().compareTo(bookedFinancePosting2.getDate());
            }
        });
        Double sum = new Double(0);
        for (BookedFinancePosting bookedFinancePosting : list1) {
            sum += sum + bookedFinancePosting.getAmount();
            bookedFinancePosting.setSum(sum);
        }
        return sortedAndSummed;
    }

    @Override
    protected FinanceAccount fetchParent() {
        return parent;
    }

    @Override
    public void setObject(List<BookedFinancePosting> object) {
        for (BookedFinancePosting bookedFinancePosting : object) {
            //TODO
        }
    }

    @Override
    public void detach() {
        if (list != null) {
            list = null;
        }

    }
}
