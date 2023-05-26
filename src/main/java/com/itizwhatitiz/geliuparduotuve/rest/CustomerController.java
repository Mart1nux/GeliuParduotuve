package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
import com.itizwhatitiz.geliuparduotuve.rest.dto.CustomerDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.util.Nonbinding;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/customers")
@Logger
public class CustomerController {
    @Inject
    CustomerDao customerDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(CustomerDto customerDto){
        Customer customer = new Customer();
        customer.setFirstname(customerDto.getFirstname());
        customer.setLastname(customerDto.getLastname());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setRole(customerDto.getRole());
        customerDao.persist(customer);
        return Response.ok(customer).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id, GenericDto dto){
        Customer customer = customerDao.findOne(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstname(customer.getFirstname());
        customerDto.setLastname(customer.getLastname());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        customerDto.setRole(customer.getRole());
        return Response.ok(customerDto).build();
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(GenericDto dto){
        List<Customer> customers = customerDao.findAll();
        List<CustomerDto> customerDtos = new ArrayList<>();
        for(Customer customer:customers){
            CustomerDto customerDto = new CustomerDto();
            customerDto.setFirstname(customer.getFirstname());
            customerDto.setLastname(customer.getLastname());
            customerDto.setUsername(customer.getUsername());
            customerDto.setPassword(customer.getPassword());
            customerDto.setRole(customer.getRole());
            customerDtos.add(customerDto);
        }
        //System.out.println(search.recommendItems());
        return Response.ok(customerDtos).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Integer id, CustomerDto customerDto){
        Customer customer = customerDao.findOne(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        customer.setFirstname(customerDto.getFirstname());
        customer.setLastname(customerDto.getLastname());
        customer.setUsername(customerDto.getUsername());
        customer.setPassword(customerDto.getPassword());
        customer.setRole(customerDto.getRole());
        customerDao.merge(customer);
        return Response.ok(customerDto).build();
    }

    @Path("/{id}")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response patch(@PathParam("id") Integer id, CustomerDto customerDto){
        Customer customer = customerDao.findOne(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (customerDto.getFirstname() != null) {
            customer.setFirstname(customerDto.getFirstname());
        }

        if (customerDto.getLastname() != null) {
            customer.setLastname(customerDto.getLastname());
        }

        if (customerDto.getUsername() != null) {
            customer.setUsername(customerDto.getUsername());
        }

        if (customerDto.getPassword() != null) {
            customer.setPassword(customerDto.getPassword());
        }

        if (customerDto.getRole() != null) {
            customer.setRole(customerDto.getRole());
        }
        customerDao.merge(customer);
        return Response.ok(customerDto).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id, GenericDto dto){
        Customer customer = customerDao.findOne(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstname(customer.getFirstname());
        customerDto.setLastname(customer.getLastname());
        customerDto.setUsername(customer.getUsername());
        customerDto.setPassword(customer.getPassword());
        customerDto.setRole(customer.getRole());
        customerDao.remove(customer);
        return Response.ok(customerDto).build();
    }
}
