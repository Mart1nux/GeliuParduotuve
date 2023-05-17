package com.itizwhatitiz.geliuparduotuve.businesslogic;

import com.itizwhatitiz.geliuparduotuve.dao.ItemDao;
import com.itizwhatitiz.geliuparduotuve.entity.Item;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@RequestScoped
public class Search implements Serializable {

    @Inject
    ItemDao itemDao;

    public List<Item> searchItems(String name){
        return itemDao.findByName(name);
    }

    public List<Item> recommendItems(){
        List<Item> recommendedItems = new ArrayList<>();
        List<Integer> items = itemDao.findIds();
        for (int i = 0; i < 2; i++){
            Random random = new Random();
            Item item = itemDao.findOne(random.nextInt(Collections.max(items)) + Collections.min(items));
            recommendedItems.add(item);
        }
        return recommendedItems;
    }
}
