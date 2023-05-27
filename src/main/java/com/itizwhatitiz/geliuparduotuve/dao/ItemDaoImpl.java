package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Item;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class ItemDaoImpl implements ItemDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;

    @Override
    public Item findOne(Integer id) {
        return em.find(Item.class, id, LockModeType.OPTIMISTIC);
    }

    @Override
    public List<Item> findAll() {
        return em.createQuery("Select p from Item as p", Item.class).getResultList();
    }

    @Override
    public List<Integer> findIds() {
        return em.createQuery("Select p.id from Item as p").getResultList();
    }

    @Override
    public List<Item> findByName(String name) {
        return em.createQuery("Select p from Item as p where p.name like :itemName", Item.class).setParameter("itemName", name).getResultList();
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
        em.remove(em.contains(item) ? item : em.merge(item));
    }
}
