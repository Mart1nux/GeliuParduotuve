package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.ItemDao;
import com.itizwhatitiz.geliuparduotuve.dao.SellerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Item;
import com.itizwhatitiz.geliuparduotuve.entity.Seller;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.ItemDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@ApplicationScoped
@Path("/items")
@Logger
public class ItemController {
    @Inject
    SellerDao sellerDao;

    @Inject
    ItemDao itemDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ItemDto itemDto){
        Seller seller = sellerDao.findOne(itemDto.getSellerId());
        if (seller == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setAmount(itemDto.getAmount());
        item.setPrice(itemDto.getPrice());
        item.setDescription(itemDto.getDescription());
        item.setSeller(seller);
        item.setDescription(itemDto.getDescription());
        item.setImage(itemDto.getImage());
        itemDao.persist(item);
        return Response.ok(item.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id, GenericDto dto){
        Item item = itemDao.findOne(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setAmount(item.getAmount());
        itemDto.setPrice(item.getPrice());
        itemDto.setDescription(item.getDescription());
        itemDto.setSellerId(item.getSeller().getId());
        itemDto.setDescription(item.getDescription());
        itemDto.setImage(item.getImage());
        return Response.ok(itemDto).build();
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(GenericDto dto){
        List<Item> items = itemDao.findAll();
        List<ItemDto> itemDtos = new ArrayList<>();
        for(Item item:items){
            ItemDto itemDto = new ItemDto();
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setAmount(item.getAmount());
            itemDto.setPrice(item.getPrice());
            itemDto.setDescription(item.getDescription());
            itemDto.setSellerId(item.getSeller().getId());
            itemDto.setDescription(item.getDescription());
            itemDto.setImage(item.getImage());
            itemDtos.add(itemDto);
        }
        return Response.ok(itemDtos).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, ItemDto itemDto){
        Item item = itemDao.findOne(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Seller seller = sellerDao.findOne(itemDto.getSellerId());
        if (seller == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        item.setName(itemDto.getName());
        item.setAmount(itemDto.getAmount());
        item.setPrice(itemDto.getPrice());
        item.setDescription(itemDto.getDescription());
        item.setSeller(seller);
        item.setDescription(itemDto.getDescription());
        itemDao.merge(item);
        return Response.ok(itemDto).build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@PathParam("id") Integer id, ItemDto itemDto){
        Item item = itemDao.findOne(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (itemDto.getSellerId() != null) {
            Seller seller = sellerDao.findOne(itemDto.getSellerId());
            if (seller == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            item.setSeller(seller);
        }

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getAmount() != null) {
            item.setAmount(itemDto.getAmount());
        }

        if (itemDto.getPrice() != null) {
            item.setPrice(itemDto.getPrice());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        item.setImage(itemDto.getImage());
        itemDao.merge(item);
        return Response.ok(itemDto).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id, GenericDto dto){
        Item item = itemDao.findOne(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setAmount(item.getAmount());
        itemDto.setPrice(item.getPrice());
        itemDto.setSellerId(item.getSeller().getId());
        itemDto.setImage(item.getImage());
        itemDao.remove(item);
        return Response.ok(itemDto).build();
    }
    @Path("/recommendItems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response recommendItems(GenericDto dto){
        List<Item> recommendedItems = new ArrayList<>();
        List<Integer> items = itemDao.findIds();
        for (int i = 0; i < 2; i++){
            Random random = new Random();
            Item item = itemDao.findOne(random.nextInt(Collections.max(items)) + Collections.min(items));
            recommendedItems.add(item);
        }
        List<ItemDto> itemDtos = new ArrayList<>();
        for(Item item:recommendedItems){
            ItemDto itemDto = new ItemDto();
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setAmount(item.getAmount());
            itemDto.setPrice(item.getPrice());
            itemDto.setDescription(item.getDescription());
            itemDto.setSellerId(item.getSeller().getId());
            itemDto.setImage(item.getImage());
            itemDtos.add(itemDto);
        }
        return Response.ok(itemDtos).build();
    }

    @Path("/search")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchItems(ItemDto name){
        List<Item> searchedItems = itemDao.findByName(name.getName());
        List<ItemDto> itemDtos = new ArrayList<>();
        for(Item item:searchedItems){
            ItemDto itemDto = new ItemDto();
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setAmount(item.getAmount());
            itemDto.setPrice(item.getPrice());
            itemDto.setDescription(item.getDescription());
            itemDto.setSellerId(item.getSeller().getId());
            itemDto.setImage(item.getImage());
            itemDtos.add(itemDto);
        }
        return Response.ok(itemDtos).build();
    }
}
