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
	public void delete(@Bind("id") Long id);

	@Override
	@SqlUpdate("delete from user where id = :o.id;")
	public void delete(@BindBean("o") DUser o);

	@Override
	@SqlQuery("SELECT * FROM user WHERE id=:id;")
	@RegisterBeanMapper(DUser.class)
	public DUser findById(@Bind("id") Long id);

	@Override
	@SqlQuery("SELECT * FROM user;")
	@RegisterBeanMapper(DUser.class)
	public List<DUser> getAll();

	@Override
	@SqlUpdate("""
			INSERT INTO user (lastname, firstname, token, password)
			VALUES (:o.lastname, :o.firstname, :o.token, :o.password);
			""")
	public void insert(@BindBean("o") DUser o);

	@Override
	@SqlUpdate("""
			update user set lastname = :o.firstname, firstname = :o.firstname, token = :o.token, password = :o.password where id = :id;
			""")
	public void update(@Bind("id") Long id, @BindBean("o") DUser o);

	@Override
	@SqlUpdate("update user set lastname = :o.lastname, firstname = :o.firstname, token = :o.token, password = :o.password where id = :o.id;")
	public void update(@BindBean("o") DUser o);
	
}
