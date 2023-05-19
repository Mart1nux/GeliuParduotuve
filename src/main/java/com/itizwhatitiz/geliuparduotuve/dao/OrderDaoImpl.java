package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Order;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class OrderDaoImpl implements OrderDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;

    @Override
    public Order findOne(Integer id) {
        return em.find(Order.class, id);
    }

    @Override
    public List<Order> findAll() {
        return em.createQuery("Select p from Order as p", Order.class).getResultList();
    }

    @Override
    public void persist(Order order) {
        em.persist(order);
    }

    @Override
    public void merge(Order order) {
        em.merge(order);
    }

    @Override
    public void remove(Order order) {
        em.remove(order);
    }
}
