/**
 * 
 */
package ma.alakhdarbank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Clis {	
	@JsonProperty("z01") 
    private String z01;
    @JsonProperty("p41") 
    private String p41;
    @JsonProperty("p02") 
    private String p02;
    @JsonProperty("p03") 
    private String p03;
    @JsonProperty("p04") 
    private String p04;
    @JsonProperty("p05") 
    private String p05;
    @JsonProperty("p06") 
    private String p06;
    @JsonProperty("p07") 
    private String p07;
    @JsonProperty("p08") 
    private String p08;
    @JsonProperty("p09") 
    private String p09;
    @JsonProperty("p10") 
    private String p10;
    @JsonProperty("p11") 
    private String p11;
    @JsonProperty("p12") 
    private String p12;
    @JsonProperty("p13") 
    private String p13;
    @JsonProperty("p14") 
    private String p14;
    @JsonProperty("p15") 
    private String p15;
    @JsonProperty("p16") 
    private String p16;
    @JsonProperty("p17") 
    private String p17;
    @JsonProperty("p18") 
    private String p18;
    @JsonProperty("p19") 
    private String p19;
    @JsonProperty("p20") 
    private String p20;
    @JsonProperty("p21") 
    private String p21;
    @JsonProperty("p22") 
    private String p22;
    @JsonProperty("p23") 
    private String p23;
    @JsonProperty("p24") 
    private String p24;
    @JsonProperty("p25") 
    private String p25;
    @JsonProperty("p26") 
    private String p26;
    @JsonProperty("p27") 
    private String p27;
    @JsonProperty("p28") 
    private String p28;
    @JsonProperty("p29") 
    private String p29;
    @JsonProperty("p30") 
    private String p30;
    @JsonProperty("p31") 
    private String p31;
    @JsonProperty("p32") 
    private String p32;
    @JsonProperty("p33") 
    private String p33;
    @JsonProperty("p34") 
    private String p34;
    @JsonProperty("p35") 
    private String p35;
    @JsonProperty("p36") 
    private String p36;
    @JsonProperty("p37") 
    private String p37;
    @JsonProperty("p38") 
    private String p38;
    @JsonProperty("p39") 
    private String p39;
    @JsonProperty("p40") 
    private String p40;

    @JsonProperty("m38") 
    private String m38;
    @JsonProperty("m02") 
    private String m02;
    @JsonProperty("m03") 
    private String m03;
    @JsonProperty("m04") 
    private String m04;
    @JsonProperty("m05") 
    private String m05;
    @JsonProperty("m06") 
    private String m06;
    @JsonProperty("m07") 
    private String m07;
    @JsonProperty("m08") 
    private String m08;
    @JsonProperty("m09") 
    private String m09;
    @JsonProperty("m10") 
    private String m10;
    @JsonProperty("m11") 
    private String m11;
    @JsonProperty("m12") 
    private String m12;
    @JsonProperty("m13") 
    private String m13;
    @JsonProperty("m14") 
    private String m14;
    @JsonProperty("m15") 
    private String m15;
    @JsonProperty("m16") 
    private String m16;
    @JsonProperty("m17") 
    private String m17;
    @JsonProperty("m18") 
    private String m18;
    @JsonProperty("m19") 
    private String m19;
    @JsonProperty("m20") 
    private String m20;
    @JsonProperty("m21") 
    private String m21;
    @JsonProperty("m22") 
    private String m22;
    @JsonProperty("m23") 
    private String m23;
    @JsonProperty("m24") 
    private String m24;
    @JsonProperty("m25") 
    private String m25;
    @JsonProperty("m26") 
    private String m26;
    @JsonProperty("m27") 
    private String m27;
    @JsonProperty("m28") 
    private String m28;
    @JsonProperty("m29") 
    private String m29;
    @JsonProperty("m30") 
    private String m30;
    @JsonProperty("m31") 
    private String m31;
    @JsonProperty("m32") 
    private String m32;
    @JsonProperty("m33") 
    private String m33;
    @JsonProperty("m34") 
    private String m34;
    @JsonProperty("m35") 
    private String m35;
    @JsonProperty("m36") 
    private String m36;
    @JsonProperty("m37") 
    private String m37;
    @JsonProperty("l05") 
    private String l05;
    
    @JsonIgnore
    private String id;
    
    
    public String getL05 () {
    	return "1";
    }
    
    public String getId () {
    	return (this.z01.equals("M")?this.m03:this.p03);
    }
}
