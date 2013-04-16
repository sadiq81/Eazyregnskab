package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.FinancePostingDAO;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
import dk.eazyit.eazyregnskab.domain.FinancePosting;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author EazyIT
 */
@Repository
public class FinancePostingDAOImpl extends GenericDAOImpl<FinancePosting, Long> implements FinancePostingDAO {

    @Override
    public List<FinancePosting> findByDailyLedger(DailyLedger dailyLedger, int first, int count) {
        Session session = (Session) getEntityManager().getDelegate();
        final Example example = Example.create(new FinancePosting().setDailyLedger(dailyLedger)).excludeZeroes();
        Criteria c = session.createCriteria(getEntityClass()).add(example);
        c.setFirstResult(first);
        c.setMaxResults(count);
        return c.list();
    }

    @Override
    public List<FinancePosting> findByDailyLedgerAndSortOrder(DailyLedger dailyLedger, int first, int count, String orderProperty, Boolean ascending) {

        if (!orderProperty.equals("vatType.percentage")) {
            Session session = (Session) getEntityManager().getDelegate();
            final Example example = Example.create(new FinancePosting().setDailyLedger(dailyLedger)).excludeZeroes();
            Criteria c = session.createCriteria(getEntityClass()).add(example);
            c = ascending == true ? c.addOrder(Order.asc(orderProperty)) : c.addOrder(Order.desc(orderProperty));
            c.setFirstResult(first);
            c.setMaxResults(count);
            return c.list();
        } else if (orderProperty.equals("vatType.percentage")) {
            Session session = (Session) getEntityManager().getDelegate();
            final Example example = Example.create(new FinancePosting().setDailyLedger(dailyLedger)).excludeZeroes();
            Criteria c = session.createCriteria(getEntityClass()).add(example)
                    .createAlias("vatType", "vatType")
                    .addOrder(ascending == true ? (Order.asc("vatType.percentage")) : (Order.desc("vatType.percentage")));
            c.setFirstResult(first);
            c.setMaxResults(count);
            return c.list();
        } else throw new NullPointerException("vatType sortorder must be specified");
    }

}
