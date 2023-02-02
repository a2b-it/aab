package ma.cam.commun;

import java.text.SimpleDateFormat;

public class Constants {

//    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 30*60;
    public static final String SIGNING_KEY = "camprojet123r";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";
    
    public static final String dateFormat = "dd/MM/yyyy";
    
    public static final String STRING = "S";// "String";
	public static final String INT = "I";// "Int";
	public static final String LONG = "L";// "Long";
	public static final String DOUBLE = "D";// "Double";
	public static final String BIG_DECIMAL = "BD";// "BigDecimal";
	public static final String CSTREAM = "CS";// "CharacterStream";
	public static final String DATE = "DT";// "Date";
	public static final String CURSOR = "CURSOR";// "String";
	public static final String O_ELEMENT = "O_VINST_ELEMENT";
	public static final String T_ELEMENT = "T_VINST_ELEMENT"; // Table Element
	public static final String C_ELEMENT = "C_ELEMENTS"; // Table Element
	//
	// Senses
	public static final String IN = "IN";
	public static final String OUT = "OUT";
	public static final String INOUT = "INOUT";
	
	public static final String Tech_Error = "Erreur technique:";
    public static final String CURSOR_NAME = "CURSOR";
    public static final String COUNT_NAME = "COUNT";
    
    public static final String EMPTY_STRING = "";
    public static final String NULL_STRING = "null";
    
    public static final String FLAG_OUI = "O";
    
    public static final SimpleDateFormat format = new SimpleDateFormat(dateFormat);
    
    public static final String UNDERSCORE = "_";
    public static final String STATUT_OK = "OK";
    public static final String STATUT_KO = "999";
    
    public static final String ERREUR_R = "R";
}
