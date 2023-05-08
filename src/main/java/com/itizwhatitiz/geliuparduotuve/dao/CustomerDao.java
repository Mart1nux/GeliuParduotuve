package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Customer;

import java.util.List;

public interface CustomerDao {
    Customer findOne(Integer id);

    List<Customer> findAll();
}
