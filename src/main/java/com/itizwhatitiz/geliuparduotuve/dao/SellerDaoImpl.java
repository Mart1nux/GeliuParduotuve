package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Seller;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class SellerDaoImpl implements SellerDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;


    @Override
    public Seller findOne(Integer id) {
        Seller seller = em.find(Seller.class, id);
        return seller;
    }

    @Override
    public List<Seller> findAll() {
        return em.createQuery("Select p from Seller as p", Seller.class).getResultList();
    }
}
