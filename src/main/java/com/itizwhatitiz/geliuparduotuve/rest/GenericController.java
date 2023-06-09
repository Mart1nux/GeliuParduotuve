package com.itizwhatitiz.geliuparduotuve.rest;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;

import javax.inject.Inject;


public class GenericController {
    @Inject
    CustomerDao customerDao;

    private Customer getCaller(GenericDto dto) {
        if (dto.getCallerId() == null) {
            return null;
        }

        return customerDao.findOne(dto.getCallerId());
    }
    public String GetCallerRole(GenericDto dto) {
        Customer caller = getCaller(dto);

        if (caller == null) {
            return null;
        }
        return caller.getRole();
    }

    public Boolean VerifyIfCallerExists(GenericDto dto) {
        Customer caller = getCaller(dto);

        return !(caller == null);
    }

    public Boolean VerifyIfCallerIs(GenericDto dto, Integer id) {
        Customer caller = getCaller(dto);

        if (caller == null) {
            return null;
        }
        return caller.getId().equals(id);
    }
}
