package dev.hv.db.dao;

import java.util.List;

public interface IDAO<T> {

   // DELETE
   boolean delete(Long ID);

   // DELETE
   boolean delete(T o);

   // READ
   T findById(Long ID);

   // READ
   List<T> getAll();

   // CREATE
   void insert(T o);

   // UPDATE
   void update(Long ID, T o);

   // UPDATE
   void update(T o);
}