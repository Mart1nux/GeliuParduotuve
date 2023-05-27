package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class CustomerDaoImpl implements CustomerDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;


    @Override
    public Customer findOne(Integer id) {
        return em.find(Customer.class, id, LockModeType.OPTIMISTIC);
    }

    @Override
    public List<Customer> findAll() {
        return em.createQuery("Select p from Customer as p", Customer.class).getResultList();
    }

    @Override
    public void persist(Customer customer) {
        em.persist(customer);
    }

    @Override
    public void merge(Customer customer) {
        em.merge(customer);
    }

    @Override
    public void remove(Customer customer) {
        em.remove(em.contains(customer) ? customer : em.merge(customer));
    }


}
