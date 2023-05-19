package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Seller;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class SellerDaoImpl implements SellerDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;


    @Override
    public Seller findOne(Integer id) {
        return em.find(Seller.class, id);
    }

    @Override
    public List<Seller> findAll() {
        return em.createQuery("Select p from Seller as p", Seller.class).getResultList();
    }

    @Override
    public void persist(Seller seller) {
        em.persist(seller);
    }

    @Override
    public void merge(Seller seller) {
        em.merge(seller);
    }

    @Override
    public void remove(Seller seller) {
        em.remove(seller);
    }

}
