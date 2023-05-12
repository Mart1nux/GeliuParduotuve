package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.dao.OrderDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.entity.Order;
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
public class OrderController {
    @Inject
    CustomerDao customerDao;

    @Inject
    OrderDao orderDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(OrderDto orderDto){
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
    public Response findOne(@PathParam("id") Integer id){
        Order order = orderDao.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
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
    public Response findAll(){
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
    @Transactional
    public Response update(@PathParam("id") Integer id, OrderDto orderDto){
        Order order = orderDao.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
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
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") Integer id){
        Order order = orderDao.findOne(id);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderCreateDate(order.getOrderCreateDate());
        orderDto.setOrderStatus(order.getOrderStatus());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDao.remove(order);
        return Response.ok(orderDto).build();
    }
}
