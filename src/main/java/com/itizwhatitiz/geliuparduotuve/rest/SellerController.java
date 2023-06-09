package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.dao.SellerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.entity.Seller;
import com.itizwhatitiz.geliuparduotuve.logger.Logger;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;
import com.itizwhatitiz.geliuparduotuve.rest.dto.SellerDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/sellers")
@Logger
public class SellerController extends GenericController {
    @Inject
    CustomerDao customerDao;

    @Inject
    SellerDao sellerDao;

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(SellerDto sellerDto){
        Response response;
        try {
            response = _create(sellerDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _create(SellerDto sellerDto){
        if (!VerifyIfCallerExists(sellerDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(sellerDto).equals("manager")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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

    @Path("/get/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOne(@PathParam("id") Integer id, GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(dto).equals("manager")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(GenericDto dto){
        if (!VerifyIfCallerExists(dto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(dto).equals("manager")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
    public Response update(@PathParam("id") Integer id, SellerDto sellerDto){
        Response response;
        try {
            response = _update(id, sellerDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _update(Integer id, SellerDto sellerDto){
        if (!VerifyIfCallerExists(sellerDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(sellerDto).equals("manager")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response patch(@PathParam("id") Integer id, SellerDto sellerDto){
        Response response;
        try {
            response = _patch(id, sellerDto);
        }
        catch (OptimisticLockException e) {
            response = Response.status(Response.Status.CONFLICT).build();
        }

        return response;
    }

    public Response _patch(Integer id, SellerDto sellerDto){
        if (!VerifyIfCallerExists(sellerDto)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        else if (!GetCallerRole(sellerDto).equals("manager")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Seller seller = sellerDao.findOne(id);
        if (seller == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (sellerDto.getCustomerId() != null) {
            Customer customer = customerDao.findOne(sellerDto.getCustomerId());
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            seller.setCustomer(customer);
        }

        if (sellerDto.getSellerCode() != null) {
            seller.setSellerCode(sellerDto.getSellerCode());
        }

        sellerDao.merge(seller);
        return Response.ok(sellerDto).build();
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
        else if (!GetCallerRole(dto).equals("manager")) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
