package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CartItemDao;
import com.itizwhatitiz.geliuparduotuve.dao.ItemDao;
import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.entity.CartItem;
import com.itizwhatitiz.geliuparduotuve.entity.Item;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.rest.dto.CartItemDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/cart")
public class CartController {
    @Inject
    ItemDao itemDao;

    @Inject
    CustomerDao customerDao;

    @Inject
    CartItemDao cartItemDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CartItemDto cartItemDto){
        Item item = itemDao.findOne(cartItemDto.getItemId());
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Customer customer = customerDao.findOne(cartItemDto.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        CartItem cartItem = new CartItem();
        cartItem.setAmount(cartItemDto.getAmount());
        cartItem.setItem(item);
        cartItem.setCustomer(customer);
        cartItemDao.persist(cartItem);
        return Response.ok(cartItem.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id){
        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setAmount(cartItem.getAmount());
        cartItemDto.setItemId(cartItem.getItem().getId());
        cartItemDto.setCustomerId(cartItem.getCustomer().getId());
        return Response.ok(cartItemDto).build();
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        List<CartItem> cartItems = cartItemDao.findAll();
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem cartItem:cartItems){
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setAmount(cartItem.getAmount());
            cartItemDto.setItemId(cartItem.getItem().getId());
            cartItemDto.setCustomerId(cartItem.getCustomer().getId());
            cartItemDtos.add(cartItemDto);
        }
        return Response.ok(cartItemDtos).build();
    }

    @Path("/customer/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCustomer(@PathParam("id") Integer id){
        List<CartItem> cartItems = cartItemDao.findByCustomer(id);
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        for(CartItem cartItem:cartItems){
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setAmount(cartItem.getAmount());
            cartItemDto.setItemId(cartItem.getItem().getId());
            cartItemDto.setCustomerId(cartItem.getCustomer().getId());
            cartItemDtos.add(cartItemDto);
        }
        return Response.ok(cartItemDtos).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Integer id, CartItemDto cartItemDto){
        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Item item = itemDao.findOne(cartItemDto.getItemId());
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Customer customer = customerDao.findOne(cartItemDto.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        cartItem.setAmount(cartItemDto.getAmount());
        cartItem.setItem(item);
        cartItem.setCustomer(customer);

        return Response.ok(cartItemDto).build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response patch(@PathParam("id") Integer id, CartItemDto cartItemDto){
        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (cartItemDto.getItemId() != null) {
            Item item = itemDao.findOne(cartItemDto.getItemId());
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            cartItem.setItem(item);
        }

        if (cartItemDto.getCustomerId() != null) {
            Customer customer = customerDao.findOne(cartItemDto.getCustomerId());
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            cartItem.setCustomer(customer);
        }

        if (cartItemDto.getAmount() != null) {
            cartItem.setAmount(cartItemDto.getAmount());
        }

        return Response.ok(cartItemDto).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") Integer id){
        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setAmount(cartItem.getAmount());
        cartItemDto.setItemId(cartItem.getItem().getId());
        cartItemDto.setCustomerId(cartItem.getCustomer().getId());
        cartItemDao.remove(cartItem);
        return Response.ok(cartItemDto).build();
    }
}
