package ma.cam.dao;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import ma.cam.commun.Constants;
import ma.cam.commun.Util;
import ma.cam.model.JDOMapping;
import ma.cam.model.MessageOracle;
import ma.cam.model.ParamPs;
import ma.cam.model.QueryResult;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.OracleConnection;

public class AbstractDAO {

	/**
	 * 
	 */
	public static final String COUNT = "COUNT";
	public static final String V_COUNT = "V_COUNT";

	@Autowired
	DataSource datasource;

	public QueryResult executeQueryProcedure(String namePackage, String namePs, Class<?> jdo, List<?> params)
			throws Exception {

		String spackage = (namePackage != null && !namePackage.equals("")) ? namePackage : null;
		String sprocedure = (namePs != null && !namePs.equals("")) ? namePs : null;
		String sCallName = "";
		QueryResult result = new QueryResult();
		Connection conn = null;
		CallableStatement statement = null;
		String inter = "?";

		if (spackage == null && sprocedure == null) {
			throw new Exception("Veuillez donner au moins le nom du package ou de la procédure!");
		} else if (spackage == null) {
			sCallName = sprocedure;
		} else {
			sCallName = spackage + "." + sprocedure;
		}
		if (params == null || (params != null && params.isEmpty())) {
			inter = "";
		} else {

			int size = 0;
			StringBuffer tab = new StringBuffer(",");
			for (int i = 0; i < params.size(); i++) {
				String j = String.valueOf(((ParamPs) params.get(i)).getOrder());
				if (tab.indexOf("," + j + ",") < 0) {
					size++;
					tab.append(j + ",");
				}
			}
			tab = null;
			for (int i = 1; i < size; i++) {
				inter = inter + ",?";
			}
		}
		String sql = "{call " + sCallName + "(" + inter + ")}";

		conn = datasource.getConnection();
//		OracleConnection oraCon = conn.unwrap(OracleConnection.class);
		
		OracleConnection oraCon = OracleNativeJdbcExtractor.getNativeOracleConnection(conn);

		statement = conn.prepareCall(sql);
		result = performeQueryProcedure(oraCon, statement, params, jdo);
		conn.close();
 
		return result;
	}

	@SuppressWarnings("unchecked")
	private QueryResult performeQueryProcedure(OracleConnection con, CallableStatement statement, List<?> params,
			Class<?> jdo) throws Exception {

		String retour[] = null;
		QueryResult result = new QueryResult();
		int i = 0;
		if (params != null && !params.isEmpty()) {
			retour = new String[params.size()];
			Iterator<?> it = params.iterator();
			while (it.hasNext()) {
				ParamPs param = (ParamPs) it.next();
				if (param.getOrder() == 0)
					throw new Exception("L'ordre des Parametres est obligatoire!!");
				if (param.getType() == null) {

					throw new Exception("Type du Parametre :" + param.getOrder() + "non renseigné!!");

				} else if (param.getType().equalsIgnoreCase(Constants.STRING)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						statement.setString(param.getOrder(), (String) param.getValue());
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.INT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.LONG)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), (Long) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.DATE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][] ro = null;
							ro = new Object[getSizeMap((Map<String, Object>) param.getValue())][2];
							ro = convertMapToArrayObject((Map<String, Object>) param.getValue());

							Array arr = con.createARRAY(Constants.T_ELEMENT, ro);

							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.ARRAY, Constants.T_ELEMENT);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {

						throw new Exception("Sens du Paramétre :" + Constants.OUT + " avec type Param "
								+ Constants.T_ELEMENT + " non pris en charge");

					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CSTREAM)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), ((InputStream) param.getValue()), 0);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						throw new Exception("Sens du Parametre :" + Constants.OUT + " avec type Param "
								+ Constants.CSTREAM + " non pris en charge");
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CURSOR)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), ((InputStream) param.getValue()), 0);
						} else {
							statement.setNull(param.getOrder(), OracleTypes.CURSOR);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), OracleTypes.CURSOR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else {
					throw new Exception(
							"Type du Parametre :" + param.getOrder() + "non pris en charge!! ( ," + Constants.STRING
									+ "," + Constants.INT + "," + Constants.LONG + "," + Constants.DOUBLE + ")");
				}
			}
		}
		statement.execute();
		if (i >= 2) {
			int fin = (retour[i - 1] != null) ? Integer.valueOf(retour[i - 1]).intValue() : 0;
			int avfin = (retour[i - 2] != null) ? Integer.valueOf(retour[i - 2]).intValue() : 0;
			String typeMsg = statement.getString(avfin);
			String msgTxt = statement.getString(fin);

			if (typeMsg != null && !"".equalsIgnoreCase(typeMsg))
				throw new Exception(msgTxt);

			result.setTypeMessage(typeMsg);
			result.setMessage(msgTxt);
			if (avfin >= 1) {
				for (int j = 0; j < i; j++) {
					int index = Integer.valueOf(retour[j]).intValue();
					ParamPs param = (ParamPs) params.get(index - 1);
					if (param != null
							&& (param.getName().equalsIgnoreCase(COUNT) || param.getName().equalsIgnoreCase(V_COUNT))) {
						int rtr = statement.getInt(index);
						result.setCount(rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.CURSOR)
							&& param.getName().equalsIgnoreCase(Constants.CURSOR_NAME)) {

						Object objet;
						try {
							objet = statement.getObject(index);
							if (objet != null) {
								ResultSet rtr = (ResultSet) objet;
								// Mapping des valeurs du CURSOR
								List<?> list = MappingUtil.resultSetToBeans(rtr, jdo);

								result.setList(list);
							}
						} catch (SQLException e) {
							if (e.getMessage().indexOf("Cursor is Closed") >= 0
									|| e.getMessage().toUpperCase().indexOf("CURSOR IS CLOSED") >= 0)
								;
							else
								throw e;
						}
					}
				}
			}
		}
		return result;

	}

	public static int getSizeMap(Map<String, Object> resource) {
		final Set<Entry<String, Object>> t = resource.entrySet();
		final int taille = t.size();

		return taille;
	}

	public Object[][] convertMapToArrayObject(Map<String, Object> resource) throws ParseException {
		Object[][] recObj = null;
		final Set<Entry<String, Object>> t = resource.entrySet();
		final int taille = getSizeMap(resource);
		recObj = new Object[taille][2];

		int j = 0;

		for (Entry<String, Object> e : t) {
			String tKey = e.getKey().toString();
			final String cle = tKey.toUpperCase();
			String valeur = null;
			//
			final Object oValeur = resource.get(tKey);
			if (oValeur != null) {
				if (oValeur instanceof Double || oValeur instanceof BigDecimal || oValeur instanceof Float) {
					valeur = (Constants.EMPTY_STRING + oValeur).replace('.', ',');
				} else {
					valeur = Constants.EMPTY_STRING + oValeur;
				}

				if (tKey.startsWith("date_")) {
					if (e.getValue() != null) {
						if (resource.get(tKey) instanceof Date) {
							valeur = Constants.format.format((Date) resource.get(tKey));
						} else if (resource.get(tKey) instanceof String) {
							valeur = (String) resource.get(tKey);
						} else {
							valeur = Constants.EMPTY_STRING + resource.get(tKey);
						}
					} else {
						resource.put(tKey, Constants.EMPTY_STRING);
					}
				}
				recObj[j][0] = cle;
				recObj[j][1] = valeur;
				j++;
			} else {
				recObj[j][0] = cle;
				recObj[j][1] = "";
				j++;
			}
		}
		return recObj;
	}

	public MessageOracle executeProcedure(String namePackage, String namePs, List<?> params) throws Exception {

		String spackage = (namePackage != null && !namePackage.equals("")) ? namePackage : null;
		String sprocedure = (namePs != null && !namePs.equals("")) ? namePs : null;
		String sCallName = "";
		MessageOracle msgOracle = new MessageOracle();
		Connection conn = null;
		CallableStatement statement = null;
		String inter = "?";
		if (spackage == null && sprocedure == null) {
			throw new Exception("Veuillez donner au moins le nom du package ou de la procédure!");
		} else if (spackage == null) {
			sCallName = sprocedure;
		} else {
			sCallName = spackage + "." + sprocedure;
		}
		if (params == null || (params != null && params.isEmpty())) {
			inter = "";
		} else {
			int size = 0;
			StringBuffer tab = new StringBuffer(",");
			for (int i = 0; i < params.size(); i++) {
				String j = String.valueOf(((ParamPs) params.get(i)).getOrder());
				if (tab.indexOf("," + j + ",") < 0) {
					size++;
					tab.append(j + ",");
				}
			}
			tab = null;
			for (int i = 1; i < size; i++) {
				inter = inter + ",?";
			}
		}
		String sql = "{call " + sCallName + "(" + inter + ")}";

		conn = datasource.getConnection();
//		OracleConnection oraCon = conn.unwrap(OracleConnection.class);
		OracleConnection oraCon = OracleNativeJdbcExtractor.getNativeOracleConnection(conn);
		statement = conn.prepareCall(sql);

		msgOracle = performeProcedure(oraCon, statement, params);
		conn.close();
		return msgOracle;
	}

	@SuppressWarnings("unchecked")
	private MessageOracle performeProcedure(OracleConnection conn, CallableStatement statement, List<?> params)
			throws SQLException, Exception, ParseException {
		String retour[] = null;
		MessageOracle msgOracle = new MessageOracle();
		int i = 0;
		if (params != null && !params.isEmpty()) {
			retour = new String[params.size()];
			Iterator<?> it = params.iterator();
			while (it.hasNext()) {
				ParamPs param = (ParamPs) it.next();
				if (param.getOrder() == 0)
					throw new Exception("L'ordre des Parametres est obligatoire!!");
				if (param.getType() == null) {

					throw new Exception("Type du Parametre :" + param.getOrder() + "non renseigné!!");

				} else if (param.getType().equalsIgnoreCase(Constants.STRING)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						statement.setString(param.getOrder(), (String) param.getValue());

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {
						statement.setString(param.getOrder(), (String) param.getValue());
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.INT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.LONG)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), Long.parseLong(param.getValue().toString()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), (Long) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.DATE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][] ro = null;
							ro = new Object[getSizeMap((Map<String, Object>) param.getValue())][2];
							ro = convertMapToArrayObject((Map<String, Object>) param.getValue());

							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.T_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.T_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][][] ro = null;
							final List<Map<String, Object>> lm = (List<Map<String, Object>>) param.getValue();
							if (lm.size() > 0) {
								final Map<String, Object> fElement = lm.get(0);
								ro = new Object[lm.size()][getSizeMap(fElement)][2];
								ro = convertListMapsToArrayObject(lm);
							}
							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.C_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						/*
						 * throw new Exception("Sens du Paramétre :" + SenseParam.OUT +
						 * " avec type Param " + Constants.CSTREAM + " non pris en charge");
						 */
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.C_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CSTREAM)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), param.getStream(), param.getSize());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						throw new Exception("Sens du Parametre :" + Constants.OUT + " avec type Param "
								+ Constants.CSTREAM + " non pris en charge");
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else {
					throw new Exception("Type du Parametre :" + param.getOrder() + "non pris en charge!! ( ,"
							+ Constants.STRING + "," + Constants.INT + "," + Constants.LONG + "," + Constants.DOUBLE
							+ "," + Constants.BIG_DECIMAL + ")");
				}
			}
		}
		// fin construction de l'appel avec parametres
		statement.execute();
		// recupérer les paramétres en sortie
		if (i >= 2) {

			int fin = (retour[i - 1] != null) ? Integer.valueOf(retour[i - 1]).intValue() : 0;
			int avfin = (retour[i - 2] != null) ? Integer.valueOf(retour[i - 2]).intValue() : 0;

			String typeMsg = statement.getString(avfin);
			String msgTxt = statement.getString(fin);
			msgOracle.setTypeMessage(typeMsg);
			msgOracle.setMessage(msgTxt);
			msgOracle.setRetours(new HashMap<String, Object>());
			if (avfin >= 2) {
				for (int j = 0; j < i; j++) {
					int index = Integer.valueOf(retour[j]).intValue();
					ParamPs param = (ParamPs) params.get(index - 1);
					if (param != null && param.getType().equalsIgnoreCase(Constants.STRING)) {
						String rtr = statement.getString(index);

						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.INT)) {
						int rtr = statement.getInt(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);

					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.LONG)) {
						long rtr = statement.getLong(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
						double rtr = statement.getDouble(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
						BigDecimal rtr = statement.getBigDecimal(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DATE)) {
						Date rtr = statement.getDate(index);
						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = convertArrayObjectToMap(statement, Constants.O_ELEMENT,
									rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = convertArrayObjectToMap(statement, Constants.C_ELEMENT,
									rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
				}
			}
		}
		return msgOracle;

	}

	public int executeCountProcedure(String namePackage, String namePs, List<?> params) throws Exception {

		String count = "0";
		try {
			MessageOracle msgOracle = executeProcedure(namePackage, namePs, params);

			if (msgOracle.getTypeMessage() != null)
				throw new Exception(msgOracle.getMessage());

			count = (String) msgOracle.getRetours().get(Constants.COUNT_NAME);

		} catch (SQLException e) {
			throw new Exception(e);
		}

		return Integer.valueOf(count).intValue();
	}

	public Object[][][] convertListMapsToArrayObject(List<Map<String, Object>> lMaps) {
		Object[][][] recObj = null;
		final int tailleL = lMaps.size();
		final int tailleR = getMaxSizeMaps(lMaps);
		recObj = new Object[tailleL][tailleR][2];

		for (int i = 0; i < tailleL; i++) {
			final Map<String, Object> resource = (Map<String, Object>) lMaps.get(i);
			Set<Entry<String, Object>> t = resource.entrySet();
			int j = 0;

			for (Entry<String, Object> e : t) {

				String tKey = e.getKey();
				final String cle = tKey.toUpperCase();
				String valeur = null;
				//
				final Object oValeur = resource.get(tKey);
				if (oValeur != null) {
					if (oValeur instanceof Double || oValeur instanceof BigDecimal || oValeur instanceof Float) {
						valeur = (Constants.EMPTY_STRING + oValeur).replace('.', ',');
					} else {
						valeur = Constants.EMPTY_STRING + oValeur;
					}

					if (tKey.startsWith("date_")) {
						if (e.getValue() != null) {
							if (resource.get(tKey) instanceof Date) {
								valeur = Constants.format.format((Date) resource.get(tKey));
							} else if (resource.get(tKey) instanceof String) {
								valeur = (String) resource.get(tKey);
							} else {
								valeur = Constants.EMPTY_STRING + resource.get(tKey);
							}
						} else {
							resource.put(tKey, Constants.EMPTY_STRING);
						}
					}

					if (tKey.startsWith("flag_")) {
						if (oValeur != null) {
							valeur = new Boolean(Constants.EMPTY_STRING + oValeur) ? "1" : "0";
						}
					}

					recObj[i][j][0] = cle;
					recObj[i][j][1] = valeur;
					j++;
				}
			}
		}

		return recObj;
	}

	public int getMaxSizeMaps(List<Map<String, Object>> resources) {
		int maxSize = 0;

		for (Map<String, Object> map : resources) {
			final int taille = getSizeMap(map);
			if (taille > maxSize) {
				maxSize = taille;
			}
		}
		return maxSize;
	}

	public Map<String, Object> convertArrayObjectToMap(CallableStatement statement, String objectName,
			Object[] result) throws Exception {

		final Map<String, Object> map = new HashMap<String, Object>();

		for (Object tmp : result) {
			Struct row = (Struct) tmp;
			map.put((String) row.getAttributes()[0], (Object) row.getAttributes()[1]);
		}
		return map;
	}
	
	public Map<String, Object> convertToMapWidthNull(Object obj) throws Exception {
    	Map<String, Object> result = new HashMap<String, Object>();
    	BeanInfo info = Introspector.getBeanInfo(obj.getClass());
    	for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
    	    Method reader = pd.getReadMethod();
    	    if (reader != null) {
    		final String value = getValueBinding(reader.invoke(obj));
    		if (value != null) {
    			result.put(getAttributeNameForMap(pd.getName()).toUpperCase(), getValueBinding(reader.invoke(obj)));
    		}else{
    			
    			result.put(getAttributeNameForMap(pd.getName()).toUpperCase(), Constants.EMPTY_STRING);
    		}
    	    }
    	}
    	return result;
        }
	
	private static String getValueBinding(Object value) {
		if (value == null) {
		    return null;
		}
		if (value instanceof String) {
		    return (String) value;
		}
		if (value instanceof Long) {
			return Constants.EMPTY_STRING + value;
		}
		if (value instanceof Double) {
			final String returnedValue = Constants.EMPTY_STRING + value;
			return returnedValue.replace(".", ",");
		}
		if (value instanceof Integer) {
			return Constants.EMPTY_STRING + value;
		}
		if (value instanceof Float) {
			final String returnedValue = Constants.EMPTY_STRING + value;
			return returnedValue.replace(".", ",");
		}
		if (value instanceof Date) {
		    return Constants.format.format((Date) value);
		}
		if (value instanceof Boolean) {
			
			if(Boolean.valueOf(value.toString())){
				return "1";
			}else{
				return "0";
			}
		   
		}	
		if (value instanceof BigDecimal) {
				final String returnedValue = Constants.EMPTY_STRING + value;
				return returnedValue.replace(".", ",");
		}
		return null;
	    }
	
	public  List<Map<String, Object>> convertToListMap(List<?> objects) throws Exception
    {
    	if (objects == null)
    		return null;
    	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    	
    	Iterator<?> it = objects.iterator();
    	while(it.hasNext())
    	{
    		Object object = (Object)it.next();
    		result.add(convertSingleObject(object));
    	}
    	
    	return result;
    }
	
	private Map<String, Object> convertSingleObject(Object obj) throws Exception {
    	Map<String, Object> result = new HashMap<String, Object>();
    	BeanInfo info = Introspector.getBeanInfo(obj.getClass());
    	for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
    	    Method reader = pd.getReadMethod();
    	    if (reader != null) {
    	    	final String value = getValueBinding(reader.invoke(obj));
    	    	
    	    	if (value != null) {
    	    		result.put(getAttributeNameForMap(pd.getName()).toUpperCase(), getValueBinding(reader.invoke(obj)));
        		}else{        		
        			result.put(getAttributeNameForMap(pd.getName()).toUpperCase(), Constants.EMPTY_STRING);
        		}
    			
    	    }
    	}
    	return result;
    }

	private String getAttributeNameForMap(String inputString) {
		String outputString = "";
		char c; 

		for (int i = 0; i < inputString.length(); i++) {
		    c = inputString.charAt(i);
		    outputString += Character.isUpperCase(c) ? Constants.UNDERSCORE+c  : c; 
		}
		
		return outputString;
	}
	
	
	@SuppressWarnings("unchecked")
	private MessageOracle performeProcedureWithCursors(OracleConnection conn, CallableStatement statement, List<?> params,List<JDOMapping> jdos)
			throws SQLException, Exception, ParseException {
		String retour[] = null;
		MessageOracle msgOracle = new MessageOracle();
		int i = 0;
		if (params != null && !params.isEmpty()) {
			retour = new String[params.size()];
			Iterator<?> it = params.iterator();
			while (it.hasNext()) {
				ParamPs param = (ParamPs) it.next();
				if (param.getOrder() == 0)
					throw new Exception("L'ordre des Parametres est obligatoire!!");
				if (param.getType() == null) {

					throw new Exception("Type du Parametre :" + param.getOrder() + "non renseigné!!");

				} else if (param.getType().equalsIgnoreCase(Constants.STRING)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						statement.setString(param.getOrder(), (String) param.getValue());

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {
						statement.setString(param.getOrder(), (String) param.getValue());
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.VARCHAR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.INT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setInt(param.getOrder(), (Integer) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.LONG)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), Long.parseLong(param.getValue().toString()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setLong(param.getOrder(), (Long) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}
				} else if (param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDouble(param.getOrder(), (Double) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setBigDecimal(param.getOrder(), (BigDecimal) param.getValue());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.NUMERIC);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.DATE)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else if ((param.getSens().equalsIgnoreCase(Constants.INOUT))) {

						if (param.getValue() != null) {
							statement.setDate(param.getOrder(),
									new java.sql.Date(((java.util.Date) param.getValue()).getTime()));
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.DATE);
						}

						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.DATE);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge!!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][] ro = null;
							ro = new Object[getSizeMap((Map<String, Object>) param.getValue())][2];
							ro = convertMapToArrayObject((Map<String, Object>) param.getValue());

							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.T_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.T_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							Object[][][] ro = null;
							final List<Map<String, Object>> lm = (List<Map<String, Object>>) param.getValue();
							if (lm.size() > 0) {
								final Map<String, Object> fElement = lm.get(0);
								ro = new Object[lm.size()][getSizeMap(fElement)][2];
								ro = convertListMapsToArrayObject(lm);
							}
							// Get Oracle Connection
							Array arr = conn.createARRAY(Constants.C_ELEMENT, ro);
							statement.setArray(param.getOrder(), arr);
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						/*
						 * throw new Exception("Sens du Paramétre :" + SenseParam.OUT +
						 * " avec type Param " + Constants.CSTREAM + " non pris en charge");
						 */
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), java.sql.Types.ARRAY, Constants.C_ELEMENT);
						i++;
					} else {
						throw new Exception("Sens du Paramétre :" + param.getOrder() + " non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CSTREAM)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), param.getStream(), param.getSize());
						} else {
							statement.setNull(param.getOrder(), java.sql.Types.NUMERIC);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						throw new Exception("Sens du Parametre :" + Constants.OUT + " avec type Param "
								+ Constants.CSTREAM + " non pris en charge");
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else if (param.getType().equalsIgnoreCase(Constants.CURSOR)) {
					if (param.getSens().equalsIgnoreCase(Constants.IN)) {
						if (param.getValue() != null) {
							statement.setBinaryStream(param.getOrder(), ((InputStream) param.getValue()), 0);
						} else {
							statement.setNull(param.getOrder(), OracleTypes.CURSOR);
						}
					} else if ((param.getSens().equalsIgnoreCase(Constants.OUT))) {
						retour[i] = String.valueOf(param.getOrder());
						statement.registerOutParameter(param.getOrder(), OracleTypes.CURSOR);
						i++;
					} else {
						throw new Exception("Sens du Parametre :" + param.getOrder() + "non pris en charge !!");
					}

				} else {
					throw new Exception("Type du Parametre :" + param.getOrder() + "non pris en charge!! ( ,"
							+ Constants.STRING + "," + Constants.INT + "," + Constants.LONG + "," + Constants.DOUBLE
							+ "," + Constants.BIG_DECIMAL + ")");
				}
			}
		}
		// fin construction de l'appel avec parametres
		statement.execute();
		// recupérer les paramétres en sortie
		if (i >= 2) {

			int fin = (retour[i - 1] != null) ? Integer.valueOf(retour[i - 1]).intValue() : 0;
			int avfin = (retour[i - 2] != null) ? Integer.valueOf(retour[i - 2]).intValue() : 0;

			String typeMsg = statement.getString(avfin);
			String msgTxt = statement.getString(fin);
			msgOracle.setTypeMessage(typeMsg);
			msgOracle.setMessage(msgTxt);
			msgOracle.setRetours(new HashMap<String, Object>());
			if (avfin >= 2) {
				for (int j = 0; j < i; j++) {
					int index = Integer.valueOf(retour[j]).intValue();
					ParamPs param = (ParamPs) params.get(index - 1);
					if (param != null && param.getType().equalsIgnoreCase(Constants.STRING)) {
						String rtr = statement.getString(index);

						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.INT)) {
						int rtr = statement.getInt(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);

					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.LONG)) {
						long rtr = statement.getLong(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DOUBLE)) {
						double rtr = statement.getDouble(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.BIG_DECIMAL)) {
						BigDecimal rtr = statement.getBigDecimal(index);
						msgOracle.getRetours().put(param.getName(), "" + rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.DATE)) {
						Date rtr = statement.getDate(index);
						msgOracle.getRetours().put(param.getName(), rtr);
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.T_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = convertArrayObjectToMap(statement, Constants.O_ELEMENT,
									rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
					if (param != null && param.getType().equalsIgnoreCase(Constants.C_ELEMENT)) {
						if (statement.getObject(index) != null) {
							Object[] rtr = (Object[]) ((Array) statement.getObject(index)).getArray();

							final Map<String, Object> result = convertArrayObjectToMap(statement, Constants.C_ELEMENT,
									rtr);
							// msgOracle.getRetours().put(param.getName(),
							// result);
							msgOracle.setRetours(result);
						}
					}
					//
					if (param != null && param.getType().equalsIgnoreCase(Constants.CURSOR)) {

						Object objet;
						try {
							objet = statement.getObject(index);
							if (objet != null) {
								ResultSet rtr = (ResultSet) objet;
								// Mapping des valeurs du CURSOR
								if(!Util.isNull(jdos)) {
									for(JDOMapping jdo : jdos) {
										if(param.getOrder() == jdo.getIndex()) {
											List<?> list = MappingUtil.resultSetToBeans(rtr,jdo.getJdo());
											msgOracle.getRetours().put(param.getName(), list);
											break;
										}
									}
								}
							}
						} catch (SQLException e) {
							if (e.getMessage().indexOf("Cursor is Closed") >= 0
									|| e.getMessage().toUpperCase().indexOf("CURSOR IS CLOSED") >= 0)
								;
							else
								throw e;
						}
					}
				}
			}
		}
		return msgOracle;

	}
	
	public MessageOracle executeProcedureWithCursors(String namePackage, String namePs, List<?> params,List<JDOMapping> jdos) throws Exception {

		String spackage = (namePackage != null && !namePackage.equals("")) ? namePackage : null;
		String sprocedure = (namePs != null && !namePs.equals("")) ? namePs : null;
		String sCallName = "";
		MessageOracle msgOracle = new MessageOracle();
		Connection conn = null;
		CallableStatement statement = null;
		String inter = "?";
		if (spackage == null && sprocedure == null) {
			throw new Exception("Veuillez donner au moins le nom du package ou de la procédure!");
		} else if (spackage == null) {
			sCallName = sprocedure;
		} else {
			sCallName = spackage + "." + sprocedure;
		}
		if (params == null || (params != null && params.isEmpty())) {
			inter = "";
		} else {
			int size = 0;
			StringBuffer tab = new StringBuffer(",");
			for (int i = 0; i < params.size(); i++) {
				String j = String.valueOf(((ParamPs) params.get(i)).getOrder());
				if (tab.indexOf("," + j + ",") < 0) {
					size++;
					tab.append(j + ",");
				}
			}
			tab = null;
			for (int i = 1; i < size; i++) {
				inter = inter + ",?";
			}
		}
		String sql = "{call " + sCallName + "(" + inter + ")}";

		conn = datasource.getConnection();
//		OracleConnection oraCon = conn.unwrap(OracleConnection.class);
		OracleConnection oraCon = OracleNativeJdbcExtractor.getNativeOracleConnection(conn);
		statement = conn.prepareCall(sql);

		msgOracle = performeProcedureWithCursors(oraCon, statement, params, jdos);
		conn.close();
		return msgOracle;
	}
}
