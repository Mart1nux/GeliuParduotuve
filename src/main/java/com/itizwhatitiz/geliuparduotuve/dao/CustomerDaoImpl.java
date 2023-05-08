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
        Customer customer = em.find(Customer.class, id);
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return em.createQuery("Select p from Customer as p", Customer.class).getResultList();
    }

}
