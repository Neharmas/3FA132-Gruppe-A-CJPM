package dev.hv.db.dao;

import java.util.List;

import dev.hv.db.model.DUser;

public interface IDAO<T> {

   // DELETE
   void delete(Long id);

   // DELETE
   void delete(T o);

   // READ
   T findById(Long id);

   // READ
   List<T> getAll();

   // CREATE
   void insert(T o);

   // UPDATE
   void update(Long id, T o);

   // UPDATE
   void update(T o);
}
