package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.*;
import com.itizwhatitiz.geliuparduotuve.entity.*;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
import com.itizwhatitiz.geliuparduotuve.rest.dto.CartItemDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.OrderedItemDto;

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
@Logger
public class CartController extends GenericController {
    @Inject
    ItemDao itemDao;
    @Inject
    OrderDao orderDao;

    @Inject
    CustomerDao customerDao;

    @Inject
    CartItemDao cartItemDao;

    @Inject
    OrderedItemDao orderedItemDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(CartItemDto cartItemDto){
        if (!VerifyIfCallerExists(cartItemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!VerifyIfCallerIs(cartItemDto, cartItemDto.getCustomerId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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

    @Path("/customer/{user_id}/toOrder/{order_id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(GenericDto dto, @PathParam("user_id") Integer userId, @PathParam("order_id") Integer orderId){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Order order = orderDao.findOne(orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Customer customer = customerDao.findOne(userId);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(dto, order.getCustomer().getId()) && (!VerifyIfCallerIs(dto, customer.getId()) && !GetCallerRole(dto).equals("Seller"))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<CartItem> cartItems = cartItemDao.findByCustomer(userId);
        List<OrderedItemDto> orderedItemDtos = new ArrayList<>();
        for(CartItem cartItem:cartItems) {
            Item item = itemDao.findOne(cartItem.getItem().getId());
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            OrderedItem orderedItem = new OrderedItem();
            orderedItem.setAmount(cartItem.getAmount());
            orderedItem.setItem(item);
            orderedItem.setOrder(order);
            orderedItemDao.persist(orderedItem);
            cartItemDao.remove(cartItem);

            OrderedItemDto orderedItemDto = new OrderedItemDto();
            orderedItemDto.setAmount(orderedItem.getAmount());
            orderedItemDto.setItemId(orderedItem.getItem().getId());
            orderedItemDto.setOrderId(orderedItem.getOrder().getId());
            orderedItemDtos.add(orderedItemDto);
        }

        return Response.ok(orderedItemDtos).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(dto, cartItem.getCustomer().getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setAmount(cartItem.getAmount());
        cartItemDto.setItemId(cartItem.getItem().getId());
        cartItemDto.setCustomerId(cartItem.getCustomer().getId());
        return Response.ok(cartItemDto).build();
    }

    @Path("/customer/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCustomer(@PathParam("id") Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!VerifyIfCallerIs(dto, id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
        if (!VerifyIfCallerExists(cartItemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(cartItemDto, cartItem.getCustomer().getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
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
        if (!VerifyIfCallerExists(cartItemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(cartItemDto, cartItem.getCustomer().getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
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
    public Response delete(@PathParam("id") Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }


        CartItem cartItem = cartItemDao.findOne(id);
        if (cartItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!VerifyIfCallerIs(dto, cartItem.getCustomer().getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setAmount(cartItem.getAmount());
        cartItemDto.setItemId(cartItem.getItem().getId());
        cartItemDto.setCustomerId(cartItem.getCustomer().getId());
        cartItemDao.remove(cartItem);
        return Response.ok(cartItemDto).build();
    }
}
