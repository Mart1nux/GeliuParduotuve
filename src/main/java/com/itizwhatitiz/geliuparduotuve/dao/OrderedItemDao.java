package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.OrderedItem;

import java.util.List;

public interface OrderedItemDao {
    OrderedItem findOne(Integer id);

    List<OrderedItem> findAll();

    List<OrderedItem> findByOrder(Integer id);

    void persist(OrderedItem orderedItem);

    void merge(OrderedItem orderedItem);

    void remove(OrderedItem orderedItem);
}
