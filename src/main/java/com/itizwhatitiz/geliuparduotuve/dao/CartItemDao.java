package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.CartItem;

import java.util.List;

public interface CartItemDao {
    CartItem findOne(Integer id);

    List<CartItem> findAll();

    List<CartItem> findByCustomer(Integer id);

    void persist(CartItem cartItem);

    void merge(CartItem cartItem);

    void remove(CartItem cartItem);
}
