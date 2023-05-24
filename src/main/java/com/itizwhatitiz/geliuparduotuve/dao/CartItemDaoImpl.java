package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.CartItem;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class CartItemDaoImpl implements CartItemDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;


    @Override
    public CartItem findOne(Integer id) {
        return em.find(CartItem.class, id);
    }

    @Override
    public List<CartItem> findAll() {
        return em.createQuery("Select p from CartItem as p", CartItem.class).getResultList();
    }

    public List<CartItem> findByCustomer(Integer id) {
        return em.createQuery("Select p from CartItem as p where p.customer.id=:customerId", CartItem.class)
                .setParameter("customerId", id)
                .getResultList();
    }

    @Override
    public void persist(CartItem cartItem) {
        em.persist(cartItem);
    }

    @Override
    public void merge(CartItem cartItem) {
        em.merge(cartItem);
    }

    @Override
    public void remove(CartItem cartItem) {
        em.remove(cartItem);
    }


}
