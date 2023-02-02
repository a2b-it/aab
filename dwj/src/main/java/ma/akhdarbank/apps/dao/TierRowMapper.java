package ma.akhdarbank.apps.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ma.akhdarbank.apps.model.Tiers;

public class TierRowMapper implements RowMapper<Tiers> {
	
	@Override
	public Tiers mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		return new Tiers(rs.getLong("IDENTIFIANT"), 
				rs.getString("TYPEPERSONNE"),
				rs.getString("NOM"),
				rs.getString("PRENOM"),
				rs.getString("RAISONSOCIALE"),
				rs.getString("ETX_RIM"),
				null,
				null,
				rs.getString("ANNEENAISSANCE"),
				rs.getString("NATIONALITE"),
				rs.getString("REFERENCEPIECE")				
				);
	}

}

