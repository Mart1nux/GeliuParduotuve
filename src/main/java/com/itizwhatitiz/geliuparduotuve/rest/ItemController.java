package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.ItemDao;
import com.itizwhatitiz.geliuparduotuve.dao.SellerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Item;
import com.itizwhatitiz.geliuparduotuve.entity.Seller;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
import com.itizwhatitiz.geliuparduotuve.mybatis.dao.ItemMapper;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.ItemDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
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
public class ItemController extends GenericController {
    @Inject
    SellerDao sellerDao;

    @Inject
    ItemDao itemDao;

    @Inject
    ItemMapper itemMapper;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ItemDto itemDto){
        Response response;
        try {
            response = _create(itemDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _create(ItemDto itemDto){
        if (!VerifyIfCallerExists(itemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(itemDto).equals("manager") && !GetCallerRole(itemDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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

    @Path("/get/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id, GenericDto dto){
        com.itizwhatitiz.geliuparduotuve.mybatis.model.Item item = itemMapper.selectByPrimaryKey(id);
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        ItemDto itemDto = new ItemDto();
        itemDto.setName(item.getName());
        itemDto.setAmount(item.getAmount());
        itemDto.setPrice(item.getPrice());
        itemDto.setDescription(item.getDescription());
        itemDto.setSellerId(item.getSellerId());
        itemDto.setDescription(item.getDescription());
        itemDto.setImage(item.getImage());
        return Response.ok(itemDto).build();
    }

    @Path("/")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(GenericDto dto){
        List<com.itizwhatitiz.geliuparduotuve.mybatis.model.Item> items = itemMapper.selectAll();
        List<ItemDto> itemDtos = new ArrayList<>();
        for(com.itizwhatitiz.geliuparduotuve.mybatis.model.Item item:items){
            ItemDto itemDto = new ItemDto();
            itemDto.setId(item.getId());
            itemDto.setName(item.getName());
            itemDto.setAmount(item.getAmount());
            itemDto.setPrice(item.getPrice());
            itemDto.setDescription(item.getDescription());
            itemDto.setSellerId(item.getSellerId());
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
        Response response;
        try {
            response = _update(id, itemDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _update(Integer id, ItemDto itemDto){
        if (!VerifyIfCallerExists(itemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(itemDto).equals("manager") && !GetCallerRole(itemDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
        Response response;
        try {
            response = _patch(id, itemDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _patch(Integer id, ItemDto itemDto){
        if (!VerifyIfCallerExists(itemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(itemDto).equals("manager") && !GetCallerRole(itemDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
        Response response;
        try {
            response = _delete(id, dto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _delete(Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(dto).equals("manager") && !GetCallerRole(dto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
    @PUT
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
    @PUT
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
