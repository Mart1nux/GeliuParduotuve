package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Order;

import java.util.List;

public interface OrderDao {
    Order findOne(Integer id);
    List<Order> findAll();

    void persist(Order order);
    void merge(Order order);
    void remove(Order order);
}
