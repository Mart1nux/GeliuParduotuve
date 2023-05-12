package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Order;

import java.util.List;

public interface OrderDao {
    List<Order> findAll();
}
