/**
 * 
 */
package ma.ab.banking.app.config;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
public final class SqlParam {
	private String name;
	private Object value;
	private boolean isNull=false;
	private int type;
	private int sens=0;//0 => In,1=> Out, 2=> INout 
	private List<SqlParam> params;
	
	
	
	public static <T> SqlParam createOut (String name,String javaType) {		
		SqlParam p = create (name,null,javaType);
		p.setSens(1);
		return p;
	}
	
	public static <T> SqlParam createInOut (String name,T o,String javaType) {		
		SqlParam p = create (name,o,javaType);
		p.setSens(2);
		return p;
	}
	
	public static <T> SqlParam create (String name,T o,String javaType) {
		SqlParam p = new SqlParam();
		p.setName(name);
		if (javaType.equals(Integer.class.getName())) {
			setIntValue (p,(Integer) o);
		}else if (javaType.equals(Date.class.getName())) {
			setDateValue(p, (Date) o);
		}else if (javaType.equals(Long.class.getName())) {
			setLongValue(p, (Long) o);
		}else if (javaType.equals(Double.class.getName())) {
			setDoubleValue(p, (Double) o);
		}else if (javaType.equals(String.class.getName())) {
			setStringValue(p, (String) o);
		}
		
		return p;
	}
	
	 static void setIntValue (SqlParam p, Integer o){
		p.setType(java.sql.Types.INTEGER);
		if(o!=null) {
			p.setValue(o);			
		}else {
			p.setNull(true);			
		}
	}
	
	 static void setDateValue (SqlParam p, Date o){
		p.setType(java.sql.Types.DATE);
		if(o!=null) {
			p.setValue(o);			
		}else {
			p.setNull(true);			
		}
	}
	
	 static void setLongValue (SqlParam p, Long o){
		p.setType(java.sql.Types.BIGINT);
		if(o!=null) {
			p.setValue(o);			
		}else {
			p.setNull(true);			
		}
	}
	
	 static void setDoubleValue (SqlParam p, Double o){
		p.setType(java.sql.Types.DOUBLE);
		if(o!=null) {
			p.setValue(o);			
		}else {
			p.setNull(true);			
		}
	}
	
	 static void setDecimalValue (SqlParam p, BigDecimal o){
		p.setType(java.sql.Types.DECIMAL);
		if(o!=null) {
			p.setValue(o);			
		}else {
			p.setNull(true);			
		}
	}
	 static void setStringValue (SqlParam p, String o){
			p.setType(java.sql.Types.VARCHAR);
			if(o!=null) {
				p.setValue(o);			
			}else {
				p.setNull(true);			
			}
	}
	
	public boolean isIn(){
		if(this.sens == 0) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isOut(){
		if(this.sens == 1) {
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isInOut(){
		if(this.sens == 2) {
			return true;
		}else {
			return false;
		}
	}
	
}
