package com.itizwhatitiz.geliuparduotuve.dao;

import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.entity.Seller;

import java.util.List;

public interface SellerDao {
    Seller findOne(Integer id);

    List<Seller> findAll();
}
