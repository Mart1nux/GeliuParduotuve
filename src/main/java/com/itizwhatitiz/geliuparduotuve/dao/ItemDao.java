package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Item;

import java.util.List;

public interface ItemDao {
    Item findOne(Integer id);
    List<Item> findAll();
    List<Integer> findIds();
    List<Item> findByName(String name);

    void persist(Item item);
    void merge(Item item);
    void remove(Item item);
}
