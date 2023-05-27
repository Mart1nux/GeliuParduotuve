package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.OrderedItem;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class OrderedItemDaoImpl implements OrderedItemDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;


    @Override
    public OrderedItem findOne(Integer id) {
        return em.find(OrderedItem.class, id, LockModeType.OPTIMISTIC);
    }

    @Override
    public List<OrderedItem> findAll() {
        return em.createQuery("Select p from OrderedItem as p", OrderedItem.class).getResultList();
    }

    public List<OrderedItem> findByOrder(Integer id) {
        return em.createQuery("Select p from OrderedItem as p where p.order.id=:orderId", OrderedItem.class)
                .setParameter("orderId", id)
                .getResultList();
    }

    @Override
    public void persist(OrderedItem orderedItem) {
        em.persist(orderedItem);
    }

    @Override
    public void merge(OrderedItem orderedItem) {
        em.merge(orderedItem);
    }

    @Override
    public void remove(OrderedItem orderedItem) {
        em.remove(em.contains(orderedItem) ? orderedItem : em.merge(orderedItem));
    }


}
