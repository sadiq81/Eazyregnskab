package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.FinanceAccountDAO;
import dk.eazyit.eazyregnskab.domain.FinanceAccount;
import dk.eazyit.eazyregnskab.domain.LegalEntity;
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
public class FinanceAccountDAOImpl extends GenericDAOImpl<FinanceAccount, Long> implements FinanceAccountDAO {

    @Override
    public List<FinanceAccount> findByLegalEntity(LegalEntity legalEntity, int first, int count) {
        Session session = (Session) getEntityManager().getDelegate();
        final Example example = Example.create(new FinanceAccount().setLegalEntity(legalEntity)).excludeZeroes();
        Criteria c = session.createCriteria(getEntityClass()).add(example);
        c.setFirstResult(first);
        c.setMaxResults(count);
        return c.list();
    }

    @Override
    public List<FinanceAccount> findByLegalEntityAndSortOrder(LegalEntity legalEntity, int first, int count, String orderProperty, Boolean ascending) {

        if (!orderProperty.equals("vatType.percentage")) {
            Session session = (Session) getEntityManager().getDelegate();
            final Example example = Example.create(new FinanceAccount().setLegalEntity(legalEntity)).excludeZeroes();
            Criteria c = session.createCriteria(getEntityClass()).add(example);
            c = ascending == true ? c.addOrder(Order.asc(orderProperty)) : c.addOrder(Order.desc(orderProperty));
            c.setFirstResult(first);
            c.setMaxResults(count);
            return c.list();
        } else if (orderProperty.equals("vatType.percentage")) {
            Session session = (Session) getEntityManager().getDelegate();
            final Example example = Example.create(new FinanceAccount().setLegalEntity(legalEntity)).excludeZeroes();
            Criteria c = session.createCriteria(getEntityClass()).add(example)
                    .createAlias("vatType", "vatType")
                    .addOrder(ascending == true ? (Order.asc("vatType.percentage")) : (Order.desc("vatType.percentage")));
            c.setFirstResult(first);
            c.setMaxResults(count);
            return c.list();
        } else throw new NullPointerException("vatType sortorder must be specified");
    }
}
