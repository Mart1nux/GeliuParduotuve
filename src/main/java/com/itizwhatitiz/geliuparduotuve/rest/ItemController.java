package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.ItemDao;
import com.itizwhatitiz.geliuparduotuve.dao.SellerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Item;
import com.itizwhatitiz.geliuparduotuve.entity.Seller;
import com.itizwhatitiz.geliuparduotuve.rest.dto.ItemDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/items")
public class ItemController {
    @Inject
    SellerDao sellerDao;

    @Inject
    ItemDao itemDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
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
        itemDao.persist(item);
        return Response.ok(item.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id){
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
        return Response.ok(itemDto).build();
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        List<Item> items = itemDao.findAll();
        List<ItemDto> itemDtos = new ArrayList<>();
        for(Item item:items){
            ItemDto itemDto = new ItemDto();
            itemDto.setName(item.getName());
            itemDto.setAmount(item.getAmount());
            itemDto.setPrice(item.getPrice());
            itemDto.setDescription(item.getDescription());
            itemDto.setSellerId(item.getSeller().getId());
            itemDtos.add(itemDto);
        }
        return Response.ok(itemDtos).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
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
        itemDao.merge(item);
        return Response.ok(itemDto).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") Integer id){
        Item item = itemDao.findOne(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setAmount(item.getAmount());
        itemDto.setPrice(item.getPrice());
        itemDto.setSellerId(item.getSeller().getId());
        itemDao.remove(item);
        return Response.ok(itemDto).build();
    }
}
