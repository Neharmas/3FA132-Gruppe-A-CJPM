package dev.hv.db.dao;

import java.util.List;

import dev.bsinfo.ressource.ReadingMapper;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import dev.hv.db.model.DReading;

public interface ReadingDAO extends IDAO<DReading> {
	@Override
	@SqlUpdate("delete from reading where id = :id;")
	public void delete(@Bind("id") Long id);

	@Override
	@SqlUpdate("delete from reading where id = :o.id;")
	public void delete(@BindBean("o") DReading o);

	@Override
	@SqlQuery("SELECT r.*, c.lastname as c_lastname, c.firstname as c_firstname " +
			"FROM reading r JOIN customer c ON r.customer = c.id " +
			"WHERE r.id = :id;")
	@RegisterRowMapper(ReadingMapper.class)
	public DReading findById(@Bind("id") Long id);


	@SqlQuery("SELECT r.*, " +
			"c.lastname as c_lastname, c.firstname as c_firstname " +
			"FROM reading r " +
			"LEFT JOIN customer c ON r.customer = c.id;")
	@RegisterRowMapper(ReadingMapper.class)
	public List<DReading> getAll();

	@Override
	@SqlUpdate("INSERT INTO reading (comment, customer, kindofmeter, metercount, meterid, substitute, dateofreading) " +
			"VALUES (:comment, :customer.id, :kindofmeter, :metercount, :meterid, :substitute, :dateofreading); SELECT last_insert_rowid();")
	public Integer insert(@BindBean DReading reading);

	@Override
	@SqlUpdate("UPDATE reading SET comment = :comment, customer = :customer.id, dateofreading = :dateofreading, " +
			"kindofmeter = :kindofmeter, meterid = :meterid, metercount = :metercount, substitute = :substitute " +
			"WHERE id = :id; ")
	public void update(@Bind("id") Long id, @BindBean DReading reading);
	@Override
	@SqlUpdate("UPDATE reading SET comment = :comment, customer = :customer.id, dateofreading = :dateofreading, " +
			"kindofmeter = :kindofmeter, meterid = :meterid, metercount = :metercount, substitute = :substitute " +
			"WHERE id = :id; ")
	public void update(@BindBean DReading reading);
}
