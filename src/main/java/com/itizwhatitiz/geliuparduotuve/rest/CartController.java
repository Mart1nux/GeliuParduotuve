package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.*;
import com.itizwhatitiz.geliuparduotuve.entity.*;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
import com.itizwhatitiz.geliuparduotuve.rest.dto.CartItemDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
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
    public Response create(CartItemDto cartItemDto){
        Response response;
        try {
            response = _create(cartItemDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _create(CartItemDto cartItemDto){
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

    @Path("/customer/{user_id}/toOrder")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response toOrder(GenericDto dto, @PathParam("user_id") Integer userId){
        Response response;
        try {
            response = _toOrder(dto, userId);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _toOrder(GenericDto dto, Integer userId){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }


        //create order

        Customer customer = customerDao.findOne(userId);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(dto, customer.getId()) && !GetCallerRole(dto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Order order = new Order();
        order.setOrderStatus("created");
        order.setOrderCreateDate(LocalDateTime.now().toString());
        order.setCustomer(customer);
        orderDao.persist(order);

        List<CartItem> cartItems = cartItemDao.findByCustomer(userId);
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
        }

        return Response.ok(order.getId()).build();
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
    public Response update(@PathParam("id") Integer id, CartItemDto cartItemDto){
        Response response;
        try {
            response = _update(id, cartItemDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _update(Integer id, CartItemDto cartItemDto){
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

        cartItemDao.merge(cartItem);

        return Response.ok(cartItemDto).build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@PathParam("id") Integer id, CartItemDto cartItemDto){
        Response response;
        try {
            response = _patch(id, cartItemDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _patch(Integer id, CartItemDto cartItemDto){
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

        cartItemDao.merge(cartItem);

        return Response.ok(cartItemDto).build();
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
