package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.rest.dto.CustomerDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.LoginDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/login")
public class LoginController {
    @Inject
    CustomerDao customerDao;

    @Path("/")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDto login){
        List<Customer> customers = customerDao.findAll();
        if(login.getUsername() != null && login.getPassword() != null && !login.getUsername().equals("") && !login.getPassword().equals("")){
            for(Customer customer:customers){
                if(login.getUsername().equals(customer.getUsername()) && login.getPassword().equals(customer.getPassword())){
                    CustomerDto customerDto = new CustomerDto();
                    customerDto.setId(customer.getId());
                    customerDto.setFirstname(customer.getFirstname());
                    customerDto.setLastname(customer.getLastname());
                    customerDto.setUsername(customer.getUsername());
                    customerDto.setRole(customer.getRole());
                    return Response.ok(customerDto).build();
                }
            }
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}