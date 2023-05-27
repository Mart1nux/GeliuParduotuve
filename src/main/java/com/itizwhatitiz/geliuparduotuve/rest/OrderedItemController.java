package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.*;
import com.itizwhatitiz.geliuparduotuve.entity.*;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
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
@Path("/orderedItem")
@Logger
public class OrderedItemController extends GenericController {
    @Inject
    ItemDao itemDao;

    @Inject
    OrderDao orderDao;

    @Inject
    OrderedItemDao orderedItemDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(OrderedItemDto orderedItemDto){
        if (!VerifyIfCallerExists(orderedItemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Order order = orderDao.findOne(orderedItemDto.getOrderId());
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!VerifyIfCallerIs(orderedItemDto, order.getCustomer().getId()) && !GetCallerRole(orderedItemDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Item item = itemDao.findOne(orderedItemDto.getItemId());
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setAmount(orderedItemDto.getAmount());
        orderedItem.setItem(item);
        orderedItem.setOrder(order);
        orderedItemDao.persist(orderedItem);
        return Response.ok(orderedItem.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        OrderedItem orderedItem = orderedItemDao.findOne(id);
        if (orderedItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(dto, orderedItem.getOrder().getCustomer().getId()) && !GetCallerRole(dto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        OrderedItemDto orderedItemDto = new OrderedItemDto();
        orderedItemDto.setAmount(orderedItem.getAmount());
        orderedItemDto.setItemId(orderedItem.getItem().getId());
        orderedItemDto.setOrderId(orderedItem.getOrder().getId());
        return Response.ok(orderedItemDto).build();
    }

    @Path("/order/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByOrder(@PathParam("id") Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Order order = orderDao.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!VerifyIfCallerIs(dto, order.getCustomer().getId()) && !GetCallerRole(dto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<OrderedItem> orderedItems = orderedItemDao.findByOrder(id);
        List<OrderedItemDto> orderedItemDtos = new ArrayList<>();
        for(OrderedItem orderedItem:orderedItems){
            OrderedItemDto orderedItemDto = new OrderedItemDto();
            orderedItemDto.setAmount(orderedItem.getAmount());
            orderedItemDto.setItemId(orderedItem.getItem().getId());
            orderedItemDto.setOrderId(orderedItem.getOrder().getId());
            orderedItemDtos.add(orderedItemDto);
        }
        return Response.ok(orderedItemDtos).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Integer id, OrderedItemDto orderedItemDto){
        if (!VerifyIfCallerExists(orderedItemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        OrderedItem orderedItem = orderedItemDao.findOne(id);
        if (orderedItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(orderedItemDto, orderedItem.getOrder().getCustomer().getId()) && !GetCallerRole(orderedItemDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Item item = itemDao.findOne(orderedItemDto.getItemId());
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Order order = orderDao.findOne(orderedItemDto.getOrderId());
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        orderedItem.setAmount(orderedItemDto.getAmount());
        orderedItem.setItem(item);
        orderedItem.setOrder(order);

        return Response.ok(orderedItemDto).build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response patch(@PathParam("id") Integer id, OrderedItemDto orderedItemDto){
        if (!VerifyIfCallerExists(orderedItemDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        OrderedItem orderedItem = orderedItemDao.findOne(id);
        if (orderedItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(orderedItemDto, orderedItem.getOrder().getCustomer().getId()) && !GetCallerRole(orderedItemDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (orderedItemDto.getItemId() != null) {
            Item item = itemDao.findOne(orderedItemDto.getItemId());
            if (item == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            orderedItem.setItem(item);
        }

        if (orderedItemDto.getOrderId() != null) {
            Order order = orderDao.findOne(orderedItemDto.getOrderId());
            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            orderedItem.setOrder(order);
        }

        if (orderedItemDto.getAmount() != null) {
            orderedItem.setAmount(orderedItemDto.getAmount());
        }

        return Response.ok(orderedItemDto).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        OrderedItem orderedItem = orderedItemDao.findOne(id);
        if (orderedItem == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (!VerifyIfCallerIs(dto, orderedItem.getOrder().getCustomer().getId()) && !GetCallerRole(dto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        OrderedItemDto orderedItemDto = new OrderedItemDto();
        orderedItemDto.setAmount(orderedItem.getAmount());
        orderedItemDto.setItemId(orderedItem.getItem().getId());
        orderedItemDto.setOrderId(orderedItem.getOrder().getId());
        orderedItemDao.remove(orderedItem);
        return Response.ok(orderedItemDto).build();
    }
}
