package dev.hv.db.dao;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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
	@SqlQuery("SELECT id, lastname, firstname FROM customer;")
	@RegisterBeanMapper(DCustomer.class)
	public List<DCustomer> getAll();

	@Override
	//SQLUPDAte liefert die reihennummer zurück (oder nichts). Deshalb kann man hier nicht long als return-type verwenden
	@SqlUpdate("""
			INSERT INTO customer (lastname, firstname)
			VALUES (:o.lastname, :o.firstname);
			""")
	public void insert(@BindBean("o") DCustomer o);

	@Override
	@SqlUpdate("""
			update customer set lastname = :o.firstname, firstname = :o.firstname where id = :id;
			""")
	public void update(@Bind("id") Long id, @BindBean("o") DCustomer o);

	@Override
	@SqlUpdate("update customer set lastname = :o.lastname, firstname = :o.firstname where id = :o.id;")
	public void update(@BindBean("o") DCustomer o);	
	
}







