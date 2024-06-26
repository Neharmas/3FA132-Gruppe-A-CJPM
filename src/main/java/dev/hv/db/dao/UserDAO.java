package dev.hv.db.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import dev.hv.db.model.DUser;

public interface UserDAO extends IDAO<DUser> {

	@Override
	@SqlUpdate("delete from user where id = :id;")
	public boolean delete(@Bind("id") Long id);

	@Override
	@SqlUpdate("delete from user where id = :o.ID;")
	public boolean delete(@BindBean("o") DUser o);

	@Override
	@SqlQuery("SELECT * FROM user WHERE id=:id;")
	@RegisterBeanMapper(DUser.class)
	public DUser findById(@Bind("id") Long ID);

	@Override
	@SqlQuery("SELECT * FROM user;")
	@RegisterBeanMapper(DUser.class)
	public List<DUser> getAll();

	@Override
	@SqlUpdate("""
			INSERT INTO user (lastname, firstname, token, password)
			VALUES (:o.lastName, :o.firstName, :o.token, :o.password);
			""")
	public void insert(@BindBean("o") DUser o);

	@SqlQuery("SELECT MAX(id) AS id FROM User")
	public Integer getLastInsertedId();

	@Override
	@SqlUpdate("""
			update user set lastname = :o.firstName, firstname = :o.firstName, token = :o.token, password = :o.password where id = :id;
			""")
	public void update(@Bind("id") Long id, @BindBean("o") DUser o);

	@Override
	@SqlUpdate("update user set lastname = :o.lastName, firstname = :o.firstName, token = :o.token, password = :o.password where id = :o.ID;")
	public void update(@BindBean("o") DUser o);
	
}
