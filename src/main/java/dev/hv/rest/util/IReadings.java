package dev.hv.rest.util;

import java.util.List;

import dev.hv.db.model.IDReading;
import dev.hv.rest.model.IRReading;

public interface IReadings {

   void close();

   void delete(final Integer id);

   List<IRReading> getAll();

   List<IRReading> getAllfromCustomerID(Integer id);

   IDReading getWithID(final Integer id);

   int insert(final IRReading user);

   void update(final IRReading user);
}
