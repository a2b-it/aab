/**
 * 
 */
package ma.alakhdarbank.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author a.bouabidi
 *
 */
public class Cpt {
	@JsonProperty("Z01") 
    public String z01;
    @JsonProperty("C01") 
    public String c01;
    @JsonProperty("C02") 
    public String c02;
    @JsonProperty("C03") 
    public String c03;
    @JsonProperty("C04") 
    public String c04;
    @JsonProperty("C05") 
    public String c05;
    @JsonProperty("C06") 
    public String c06;
    @JsonProperty("C07") 
    public String c07;
    @JsonProperty("C08") 
    public String c08;
    @JsonProperty("C09") 
    public String c09;
    @JsonProperty("C10") 
    public String c10;
    public List<Cli> clis;
}
