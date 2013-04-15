package dk.eazyit.eazyregnskab.dao.impl;

import dk.eazyit.eazyregnskab.dao.interfaces.DailyLedgerDAO;
import dk.eazyit.eazyregnskab.domain.DailyLedger;
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
public class DailyLedgerDAOImpl extends GenericDAOImpl<DailyLedger, Long> implements DailyLedgerDAO {

    @Override
    public List<DailyLedger> findByLegalEntity(LegalEntity legalEntity, int first, int count, String orderProperty, Boolean ascending) {
        Session session = (Session) getEntityManager().getDelegate();
        final Example example = Example.create(new DailyLedger().setLegalEntity(legalEntity)).excludeZeroes();
        Criteria c = session.createCriteria(getEntityClass()).add(example);
        c = ascending == true ? c.addOrder(Order.asc(orderProperty)) : c.addOrder(Order.desc(orderProperty));
        c.setFirstResult(first);
        c.setMaxResults(count);
        return c.list();
    }

    @Override
    public List<DailyLedger> findByLegalEntityAndSortOrder(LegalEntity legalEntity, int first, int count, String orderProperty, Boolean ascending) {

        Session session = (Session) getEntityManager().getDelegate();
        final Example example = Example.create(new DailyLedger().setLegalEntity(legalEntity)).excludeZeroes();
        Criteria c = session.createCriteria(getEntityClass()).add(example);
        c = ascending == true ? c.addOrder(Order.asc(orderProperty)) : c.addOrder(Order.desc(orderProperty));
        c.setFirstResult(first);
        c.setMaxResults(count);
        return c.list();
    }


}
