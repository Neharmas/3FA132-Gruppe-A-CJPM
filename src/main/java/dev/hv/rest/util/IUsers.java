package dev.hv.rest.util;

import java.util.List;

import dev.hv.db.model.IDUser;
import dev.hv.rest.model.IRUser;

public interface IUsers {

   void close();

   void delete(final Integer id);

   List<IRUser> getAll();

   IDUser getWithID(final Integer id);

   int insert(final IRUser user);

   void update(final IRUser user);
}
