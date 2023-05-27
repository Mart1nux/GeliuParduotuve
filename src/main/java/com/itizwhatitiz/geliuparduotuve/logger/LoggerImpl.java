package com.itizwhatitiz.geliuparduotuve.logger;

import com.itizwhatitiz.geliuparduotuve.dao.CustomerDao;
import com.itizwhatitiz.geliuparduotuve.entity.Customer;
import com.itizwhatitiz.geliuparduotuve.rest.dto.GenericDto;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Logger
@Interceptor
public class LoggerImpl {
    @Inject
    CustomerDao customerDao;
    String filename = "/home/nemo/mif/6/tp/GeliuParduotuve/src/main/java/com/itizwhatitiz/geliuparduotuve/logger/log.txt";
    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        CompletableFuture.runAsync(() -> {
            Integer userId = null;
            Object[] params = ctx.getParameters();

            for(Object object : params) {
                if (object instanceof GenericDto) {
                    userId = ((GenericDto) object).getCallerId();
                    break;
                }
            }

            Customer customer = null;
            if (userId != null) {
                customer = customerDao.findOne(userId);
            }

            try (FileWriter w = new FileWriter(filename, true)) {
                w.write("[" + LocalDateTime.now() + "]\n");
                if (customer == null) {
                    w.write("    User invalid or not logged in\n");
                } else {
                    w.write("    User " + customer.getUsername() + " (role: " + customer.getRole() + ")\n");
                }
                w.write("    called " + ctx.getMethod().getDeclaringClass().getSimpleName()
                        + "." + ctx.getMethod().getName() + "\n");
            }
            catch (Exception e) {
                System.out.println("ERROR: Exception occurred");
                System.out.println(e.getMessage());
            }
        });

        return ctx.proceed();
    }
}
