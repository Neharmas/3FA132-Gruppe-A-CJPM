package dev.hv.rest.util;

import java.util.List;

import dev.hv.rest.model.IRCustomer;

public interface ICustomers {

   void close();

   void delete(final Integer id);

   List<IRCustomer> getAll();

   IRCustomer getWithID(final Integer id);

   int insert(final IRCustomer customer);

   void update(final IRCustomer customer);
}
