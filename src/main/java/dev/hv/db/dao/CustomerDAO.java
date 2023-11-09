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
	@SqlUpdate("""
			CREATE TABLE IF NOT EXISTS Kunden (
			id INTEGER PRIMARY KEY AUTOINCREMENT,
			name TEXT,
			VORNAME TEXT);
			""")
	void createTable();
	
    //List<DCustomer> customers = new ArrayList<>();
	
	@Override
	@SqlUpdate("delete from kunden where id = :id;")
	public void delete(@Bind("id") Long id);

	@Override
	@SqlUpdate("delete from kunden where id = :o.id;")
	public void delete(@BindBean("o") DCustomer o);

	@Override
	@SqlQuery("SELECT * FROM kunden WHERE id=:id;")
	@RegisterBeanMapper(DCustomer.class)
	public DCustomer findById(@Bind("id") Long id);

	@Override
	@SqlQuery("SELECT id, name, vorname FROM kunden;")
	@RegisterBeanMapper(DCustomer.class)
	public List<DCustomer> getAll();

	@Override
	//SQLUPDAte liefert die reihennummer zurück (oder nichts). Deshalb kann man hier nicht long als return-type verwenden
	@SqlUpdate("""
			INSERT INTO KUNDEN (name, vorname)
			VALUES (:o.lastname, :o.firstname);
			""")
	public void insert(@BindBean("o") DCustomer o);

	@Override
	@SqlUpdate("""
			update kunden set name = :o.firstname AND vorname = :o.firstname AND id=:id where id = :o.id;
			""")
	public void update(@Bind("id") Long id, @BindBean("o") DCustomer o);

	@Override
	@SqlUpdate("update kunden set name = :o.lastname  AND vorname = :o.firstname where id = :o.id;")
	public void update(@BindBean("o") DCustomer o);	
}
