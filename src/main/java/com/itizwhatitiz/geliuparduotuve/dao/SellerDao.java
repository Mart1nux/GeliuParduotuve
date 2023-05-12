package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Seller;

import java.util.List;

public interface SellerDao {
    Seller findOne(Integer id);

    List<Seller> findAll();

    void persist(Seller seller);

    void merge(Seller seller);

    void remove(Seller seller);
}
