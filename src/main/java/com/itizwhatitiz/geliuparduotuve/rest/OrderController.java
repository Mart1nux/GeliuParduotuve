package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.dao.OrderDao;
import com.itizwhatitiz.geliuparduotuve.dao.OrderedItemDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.entity.Order;
import com.itizwhatitiz.geliuparduotuve.entity.OrderedItem;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.OrderDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/orders")
@Logger
@Transactional
public class OrderController extends GenericController {
    @Inject
    CustomerDao customerDao;

    @Inject
    OrderDao orderDao;

    @Inject
    OrderedItemDao orderedItemDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(OrderDto orderDto){
        if (!VerifyIfCallerExists(orderDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!VerifyIfCallerIs(orderDto, orderDto.getCustomerId()) && !GetCallerRole(orderDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerDao.findOne(orderDto.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Order order = new Order();
        order.setOrderCreateDate(orderDto.getOrderCreateDate());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setCustomer(customer);
        orderDao.persist(order);
        return Response.ok(order.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id, GenericDto dto){
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
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderCreateDate(order.getOrderCreateDate());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setCustomerId(order.getCustomer().getId());
        return Response.ok(orderDto).build();
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(dto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        List<Order> orders = orderDao.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Order order:orders){
            OrderDto orderDto = new OrderDto();
            orderDto.setOrderCreateDate(order.getOrderCreateDate());
            orderDto.setOrderStatus(order.getOrderStatus());
            orderDto.setCustomerId(order.getCustomer().getId());
            orderDtos.add(orderDto);
        }
        return Response.ok(orderDtos).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, OrderDto orderDto){
        if (!VerifyIfCallerExists(orderDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Order order = orderDao.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(orderDto, order.getCustomer().getId()) && !GetCallerRole(orderDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Customer customer = customerDao.findOne(orderDto.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        order.setOrderCreateDate(orderDto.getOrderCreateDate());
        order.setOrderStatus(orderDto.getOrderStatus());
        order.setCustomer(customer);
        orderDao.merge(order);
        return Response.ok(orderDto).build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response patch(@PathParam("id") Integer id, OrderDto orderDto){
        if (!VerifyIfCallerExists(orderDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Order order = orderDao.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!VerifyIfCallerIs(orderDto, order.getCustomer().getId()) && !GetCallerRole(orderDto).equals("seller")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (orderDto.getCustomerId() != null) {
            Customer customer = customerDao.findOne(orderDto.getCustomerId());

            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            order.setCustomer(customer);
        }

        if (orderDto.getOrderCreateDate() != null) {
            order.setOrderCreateDate(orderDto.getOrderCreateDate());
        }

        if (orderDto.getOrderStatus() != null) {
            order.setOrderStatus(orderDto.getOrderStatus());
        }


        orderDao.merge(order);
        return Response.ok(orderDto).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id, GenericDto dto){
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
        for (OrderedItem orderedItem : orderedItems) {
            orderedItemDao.remove(orderedItem);
        }

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderCreateDate(order.getOrderCreateDate());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDao.remove(order);
        return Response.ok(orderDto).build();
    }
}
