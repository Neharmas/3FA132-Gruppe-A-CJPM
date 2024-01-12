package dev.bsinfo.ressource;

import dev.hv.db.model.DCustomer;
import dev.hv.db.model.DReading;
import dev.hv.db.model.IDCustomer;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadingMapper implements RowMapper<DReading> {

    @Override
    public DReading map(ResultSet rs, StatementContext ctx) throws SQLException
    {
        DReading dReading = new DReading();

        dReading.setId(rs.getLong("id"));

        dReading.setComment(rs.getString("comment"));

        DCustomer customer = mapIDCustomer(rs);
        dReading.setCustomer(customer);
        System.out.println(customer);

        dReading.setKindofmeter(rs.getString("kindofmeter"));
        dReading.setMetercount(rs.getDouble("metercount"));
        dReading.setMeterid(rs.getString("meterid"));
        dReading.setSubstitute(rs.getBoolean("substitute"));
        dReading.setDateofreading(rs.getLong("dateofreading"));
        return dReading;
    }

    private DCustomer mapIDCustomer(ResultSet rs) throws SQLException {
        long customerId = rs.getLong("customer");
        String lastname = rs.getString("c_lastname");
        String firstname = rs.getString("c_firstname");
        return new DCustomer(customerId, lastname, firstname);
    }

}
