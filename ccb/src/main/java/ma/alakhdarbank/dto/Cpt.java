/**
 * 
 */
package ma.alakhdarbank.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Cpt {
	@JsonProperty("z01") 
    private String z01;
    @JsonProperty("c01") 
    private String c01;
    @JsonProperty("c02") 
    private String c02;
    @JsonProperty("c03") 
    private String c03;
    @JsonProperty("c04") 
    private String c04;
    @JsonProperty("c05") 
    private String c05;
    @JsonProperty("c06") 
    private String c06;
    @JsonProperty("c07") 
    private String c07;
    @JsonProperty("c08") 
    private String c08;
    @JsonProperty("c09") 
    private String c09;
    @JsonProperty("c10") 
    private String c10;
    @JsonProperty("c11") 
    private String c11;
    @JsonProperty("clis") 
    private List<Clis> clis;
	public Cpt() {
		super();
		
	}
    
    public String getC11 () {
    	return c09;
    }
 
}

