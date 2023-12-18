package dev.hv.db.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.DUser;

public interface ReadingDAO extends IDAO<DReading> {
	@Override
	@SqlUpdate("delete from reading where id = :id;")
	public void delete(@Bind("id") Long id);

	@Override
	@SqlUpdate("delete from reading where id = :o.id;")
	public void delete(@BindBean("o") DReading o);

	@Override
	@SqlQuery("SELECT * FROM reading WHERE id=:id;")
	@RegisterBeanMapper(DUser.class)
	public DReading findById(@Bind("id") Long id);

	@Override
	@SqlQuery("SELECT r.id as r_id, r.comment as r_comment, r.kindofmeter as r_kindofmeter, r.metercount as r_metercount,"
			+ "r.meterid as r_meterid, r.substitute as r_substitute, r.dateofreading as r_dateofreading "
			+ "FROM reading r INNER JOIN Customer c ON (r.customer = c.id);")
	@RegisterBeanMapper(value=DReading.class, prefix="r")
	@RegisterBeanMapper(value=DCustomer.class, prefix="c")
	public List<DReading> getAll();

	@Override
	@SqlUpdate("""
			INSERT INTO READING (comment, customer, kindofmeter, metercount, meterid, substitute, dateofreading) 
			VALUES (:o.comment, :o.customer.id, :o.kindofmeter, :o.metercount, :o.meterid, :o.substitute, :o.dateofreading);
			""")
	public void insert(@BindBean("o") DReading o);

	@Override
	@SqlUpdate("""
			update reading set comment = :o.comment, customer = c.id, kindofmeter = o.kindofmeter, 
			metercount = o.metercount, meterid = o.meterid, substitute = o.substitute, dateofreading = o.dateofreading
			WHERE id = :id;
			""")
	//@RegisterBeanMapper(value=DReading.class, prefix = "o")
	@RegisterBeanMapper(value=DCustomer.class, prefix = "c")
	public void update(@Bind("id") Long id, @BindBean("o") DReading o);

	@Override
	@SqlUpdate("update reading set comment = :r.comment, c.id = c.id, c.kindofmeter = r.kindofmeter,"
			+ "c.metercount = r.metercount, c.meterid = r.meterid, c.substitute = r.substitute, c.dateofreading = r.dateofreading"
			+ "WHERE c.id = :r.id;")
	@RegisterBeanMapper(value=DCustomer.class, prefix = "c")
	public void update(@BindBean("r") DReading r);

}
