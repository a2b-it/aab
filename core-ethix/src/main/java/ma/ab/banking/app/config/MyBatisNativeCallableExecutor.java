/**
 * 
 */
package ma.ab.banking.app.config;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ma.ab.banking.tran.excep.TransactionException;

/**
 * @author a.bouabidi
 *
 */

@Component
@Slf4j
public class MyBatisNativeCallableExecutor {
	
	@Autowired
	SqlSessionFactory sessionFactory;
	
	/**
	 * 
	 * @param rparam
	 * @param catalog
	 * @param functionName
	 * @param params
	 * @return
	 * @throws TransactionException
	 */
	public Object executeFunction(SqlParam rparam, String catalog, String functionName, List<SqlParam> params) throws TransactionException{
		if(rparam==null) {
			throw new TransactionException("Return Parameter shouldn't be null with call to function");
		}
		 return execute(rparam,catalog, functionName, params);
	}
	

	/**
	 * 
	 * @param catalog
	 * @param procedureName
	 * @param params
	 * @throws TransactionException
	 */
	public void executeProcedure(String catalog, String procedureName, List<SqlParam> params) throws TransactionException{
		execute(null,catalog, procedureName, params);
	}
	
	/**
	 * 
	 * @param catalog
	 * @param functionName
	 * @param params       out param pass non null object to determine type
	 * @return
	 * @throws TransactionException
	 */
	private Object execute(SqlParam rparam,String catalog, String name, List<SqlParam> params)
			throws TransactionException {
		int index = 0;
		Object retour=null;
		boolean isFunction = false;
		// sql exemple {? = call getDob(?)}
		StringBuilder sql_params = new StringBuilder("");
		for (SqlParam p : params) {
			if (!sql_params.toString().equals("")) {
				sql_params.append(",");
			}
			sql_params.append("?");
		}
		if(rparam!=null) {
			isFunction=true;
			index=2;
		}else {
			index=1;
		}
		String sql =(isFunction)?"{? = call " + catalog+"."+name + "(" + sql_params.toString() + ")" + "}":"{call " + catalog+"."+name + "(" + sql_params.toString() + ")" + "}";
		// Preparing a CallableStatement to call a function
		CallableStatement cstmt = null;
		SqlSession session = null;
		try {
			session = sessionFactory.openSession();
			cstmt = session.
					getConnection ().prepareCall(sql);
			// Registering the out parameter of the function (return type)
			cstmt.registerOutParameter(1, Types.INTEGER);
			// Setting the input parameters of the function
			for (SqlParam p : params) {
				if (p.isIn()) {
					setSqlValue(cstmt, index++, p);
				} else if (p.isInOut()) {
					setSqlValue(cstmt, index, p);
					cstmt.registerOutParameter(index, p.getType());
					index++;
				} else {
					cstmt.registerOutParameter(index++, p.getType());
				}

			}
			//
			log.debug("CallableStatement :=>["+sql);
			// Executing the statement
			cstmt.execute();
			//
			if(isFunction) {
				getSqlValue(cstmt, 1, rparam) ;
				retour=rparam.getValue();
				log.debug("Function return :=>["+((retour!=null)?retour.toString()+"]":" null"));
				//
				int j=(isFunction)?1:0;
				for (SqlParam p : params) {
					j++;
					if(p.isOut() || p.isInOut()) {
						 getSqlValue(cstmt, j, p) ;
					}					
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new TransactionException(e);
		} finally {
			try {
				cstmt.close();
				session.getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new TransactionException(e);
			}
			session.close();
		}

		return retour;
	}

	private void setSqlValue(CallableStatement cstmt, int index, SqlParam p) throws SQLException {

		if (p.getType() == java.sql.Types.INTEGER) {
			if (p.isNull()) {
				cstmt.setNull(index, java.sql.Types.INTEGER);
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type INTEGER Is null");
			} else {
				cstmt.setInt(index, (Integer) p.getValue());
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type INTEGER value ="+(Integer) p.getValue());
			}

		} else if (p.getType() == java.sql.Types.BIGINT) {
			if (p.isNull()) {
				cstmt.setNull(index, java.sql.Types.BIGINT);
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type BIGINT Is null");
			} else {
				cstmt.setLong(index, (Long) p.getValue());
				log.debug("Parameter"+index+" :=>["+p.getName()+"] of type BIGINT value ="+(Long) p.getValue());
			}
		} else if (p.getType() == java.sql.Types.VARCHAR) {
			if (p.isNull()) {
				cstmt.setNull(index, java.sql.Types.VARCHAR);
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type VARCHAR Is null");
			} else {
				cstmt.setString(index, (String) p.getValue());
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type BIGINT value ="+(String) p.getValue());
			}
		}
		if (p.getType() == java.sql.Types.DATE) {
			if (p.isNull()) {
				cstmt.setNull(index, java.sql.Types.DATE);
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type DATE Is null");
			} else {
				java.sql.Date date = new java.sql.Date(((Date) p.getValue()).getTime());
				cstmt.setDate(index, date);
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type DATE value ="+date);
			}
		}
		if (p.getType() == java.sql.Types.DOUBLE) {
			if (p.isNull()) {
				cstmt.setNull(index, java.sql.Types.DOUBLE);
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type DOUBLE Is null");
			} else {
				cstmt.setDouble(index, (Double) p.getValue());
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type DATE value ="+(Double) p.getValue());
			}
		}
		if (p.getType() == java.sql.Types.DECIMAL) {
			if (p.isNull()) {
				cstmt.setNull(index, java.sql.Types.DECIMAL);
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type DECIMAL Is null");
			} else {
				cstmt.setBigDecimal(index, (BigDecimal) p.getValue());
				log.debug("Parameter <=="+index+" :=>["+p.getName()+"] of type DECIMAL value ="+(BigDecimal) p.getValue());
			}
		}

	}
	
	private void getSqlValue(CallableStatement cstmt, int index, SqlParam p) throws SQLException {

		if (p.getType() == java.sql.Types.INTEGER) {
			p.setValue(cstmt.getInt(index));
			log.debug("Parameter ==>"+index+" :=>["+p.getName()+"] of type INTEGER value ="+p.getValue().toString());
		} else if (p.getType() == java.sql.Types.BIGINT) {
			p.setValue(cstmt.getLong(index));
			log.debug("Parameter ==>"+index+" :=>["+p.getName()+"] of type BIGINT value ="+p.getValue().toString());
		} else if (p.getType() == java.sql.Types.VARCHAR) {
			p.setValue(cstmt.getString(index));
			log.debug("Parameter ==>"+index+" :=>["+p.getName()+"] of type VARCHAR value ="+p.getValue().toString());
		}
		if (p.getType() == java.sql.Types.DATE) {
			p.setValue(cstmt.getDate(index));
			log.debug("Parameter ==>"+index+" :=>["+p.getName()+"] of type DATE value ="+p.getValue().toString());
		}
		if (p.getType() == java.sql.Types.DOUBLE) {
			p.setValue(cstmt.getDouble(index));
			log.debug("Parameter ==>"+index+" :=>["+p.getName()+"] of type DOUBLE value ="+p.getValue().toString());
		}
		if (p.getType() == java.sql.Types.DECIMAL) {
			p.setValue(cstmt.getBigDecimal(index));
			log.debug("Parameter ==>"+index+" :=>["+p.getName()+"] of type DECIMAL value ="+p.getValue().toString());
		}

	}

}
