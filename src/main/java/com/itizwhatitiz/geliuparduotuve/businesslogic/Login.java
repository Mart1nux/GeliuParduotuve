package com.itizwhatitiz.geliuparduotuve.businesslogic;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class Login implements Serializable {

    @Inject
    CustomerDao customerDao;

    public void login(){
        String username = "username";
        String password = "password";
        List<Customer> customers = customerDao.findAll();
        if(username != null && password != null && !username.equals("") && !password.equals("")){
            for(Customer customer:customers){
                if(username.equals(customer.getUsername()) && password.equals(customer.getPassword())){
                    if(customer.getRole() == "Seller"){
                        System.out.println("You have logged in as seller.");
                        return; // token
                    }
                    System.out.println("You have logged in as customer.");
                    return;
                }
            }
            System.out.println("Wrong username or password");
        }
    }

}
