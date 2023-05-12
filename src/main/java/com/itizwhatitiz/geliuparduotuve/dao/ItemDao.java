package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Item;

import java.util.List;

public interface ItemDao {
    Item findOne(Integer id);
    List<Item> findAll();

    void persist(Item item);
    void merge(Item item);
    void remove(Item item);
}
