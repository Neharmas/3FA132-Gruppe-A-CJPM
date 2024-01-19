package dev.hv.db.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.*;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.*;

import dev.hv.db.model.DCustomer;

public interface CustomerDAO extends IDAO<DCustomer> {
	
	@Override
	@SqlUpdate("delete from customer where id = :id;")
	public void delete(@Bind("id") Long id);

	@Override
	@SqlUpdate("delete from customer where id = :o.id;")
	public void delete(@BindBean("o") DCustomer o);

	@Override
	@SqlQuery("SELECT * FROM customer WHERE id=:id;")
	@RegisterBeanMapper(DCustomer.class)
	public DCustomer findById(@Bind("id") Long id);

	@Override
	@SqlQuery("SELECT * FROM customer;")
	@RegisterBeanMapper(DCustomer.class)
	public List<DCustomer> getAll();

	@Override
	//SQLUPDAte liefert die reihennummer zurück (oder nichts). Deshalb kann man hier nicht long als return-type verwenden
	@SqlUpdate("""
			INSERT INTO customer (lastname, firstname)
			VALUES (:o.lastname, :o.firstname); SELECT MAX(id) AS id FROM Customer;
			""")
	public void insert(@BindBean("o") DCustomer o);

	@SqlQuery("SELECT MAX(id) AS id FROM customer")
	public Integer getLastInsertedId();

	@Override
	@SqlUpdate("""
			update customer set lastname = :o.lastname, firstname = :o.firstname where id = :id;
			""")
	public void update(@Bind("id") Long id, @BindBean("o") DCustomer o);

	@Override
	@SqlUpdate("update customer set lastname = :o.lastname, firstname = :o.firstname where id = :o.id;")
	public void update(@BindBean("o") DCustomer o);	
	
}







