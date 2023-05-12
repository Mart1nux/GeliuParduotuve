package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.dao.SellerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.entity.Seller;
import com.itizwhatitiz.geliuparduotuve.rest.dto.SellerDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/sellers")
public class SellerController {
    @Inject
    CustomerDao customerDao;

    @Inject
    SellerDao sellerDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(SellerDto sellerDto){
        Customer customer = customerDao.findOne(sellerDto.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Seller seller = new Seller();
        seller.setSellerCode(sellerDto.getSellerCode());
        seller.setCustomer(customer);
        sellerDao.persist(seller);
        return Response.ok(seller.getId()).build();
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id){
        Seller seller = sellerDao.findOne(id);
        if (seller == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        SellerDto sellerDto = new SellerDto();
        sellerDto.setSellerCode(seller.getSellerCode());
        sellerDto.setCustomerId(seller.getCustomer().getId());
        return Response.ok(sellerDto).build();
    }

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(){
        List<Seller> sellers = sellerDao.findAll();
        List<SellerDto> sellerDtos = new ArrayList<>();
        for(Seller seller:sellers){
            SellerDto sellerDto = new SellerDto();
            sellerDto.setSellerCode(seller.getSellerCode());
            sellerDto.setCustomerId(seller.getCustomer().getId());
            sellerDtos.add(sellerDto);
        }
        return Response.ok(sellerDtos).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Integer id, SellerDto sellerDto){
        Seller seller = sellerDao.findOne(id);
        if (seller == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Customer customer = customerDao.findOne(sellerDto.getCustomerId());
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        seller.setSellerCode(sellerDto.getSellerCode());
        seller.setCustomer(customer);
        sellerDao.merge(seller);
        return Response.ok(sellerDto).build();
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response delete(@PathParam("id") Integer id){
        Seller seller = sellerDao.findOne(id);
        if (seller == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        SellerDto sellerDto = new SellerDto();
        sellerDto.setSellerCode(seller.getSellerCode());
        sellerDto.setCustomerId(seller.getCustomer().getId());
        sellerDao.remove(seller);
        return Response.ok(sellerDto).build();
    }
}
