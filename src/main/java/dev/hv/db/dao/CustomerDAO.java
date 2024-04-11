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
	public boolean delete(@Bind("id") Long ID);

	@Override
	@SqlUpdate("delete from customer where id = :o.ID;")
	public boolean delete(@BindBean("o") DCustomer o);

	@Override
	@SqlQuery("SELECT * FROM customer WHERE id=:id;")
	@RegisterBeanMapper(DCustomer.class)
	public DCustomer findById(@Bind("id") Long ID);

	@Override
	@SqlQuery("SELECT * FROM customer;")
	@RegisterBeanMapper(DCustomer.class)
	public List<DCustomer> getAll();

	@Override
	//SQLUPDAte liefert die reihennummer zurück (oder nichts). Deshalb kann man hier nicht long als return-type verwenden
	@SqlUpdate("""
			INSERT INTO customer (lastname, firstname)
			VALUES (:o.lastName, :o.firstName);
			""")
	public void insert(@BindBean("o") DCustomer o);

	@SqlQuery("SELECT MAX(id) AS id FROM customer")
	public Integer getLastInsertedId();

	@Override
	@SqlUpdate("""
			update customer set lastname = :o.lastName, firstname = :o.firstName where id = :id;
			""")
	public void update(@Bind("id") Long ID, @BindBean("o") DCustomer o);

	@Override
	@SqlUpdate("update customer set lastname = :o.lastName, firstname = :o.firstName where id = :o.ID;")
	public void update(@BindBean("o") DCustomer o);	
	
}







