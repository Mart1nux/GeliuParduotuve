package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class CustomerDaoImpl implements CustomerDao{
    @PersistenceContext(unitName = "GeliuParduotuvePersistenceUnit")
    private EntityManager em;


    @Override
    public Customer findOne(Integer id) {
        return em.find(Customer.class, id);
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
        em.remove(customer);
    }


}
