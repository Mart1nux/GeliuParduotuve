package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Item;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class ItemDaoImpl implements ItemDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;

    @Override
    public Item findOne(Integer id) {
        return em.find(Item.class, id);
    }

    @Override
    public List<Item> findAll() {
        return em.createQuery("Select p from Item as p", Item.class).getResultList();
    }

    @Override
    public void persist(Item item) {
        em.persist(item);
    }

    @Override
    public void merge(Item item) {
        em.merge(item);
    }

    @Override
    public void remove(Item item) {
        em.remove(item);
    }
}
