/**
 * 
 */
package ma.alakhdarbank.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author a.bouabidi
 *
 */
@Getter
@Setter
@NoArgsConstructor
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class Lot {
	
	public String h01;
    public String h02;
    public String h03;
    public String h04;
    public String h05;
    public String h06;
    public String h07;
    public String h08;
    public String h09;
    public List<Cpt> cpts;
	
	
}
