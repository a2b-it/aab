//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2021.01.09 � 02:39:00 AM WEST 
//


package iso.std.iso._20022.tech.xsd.pacs_002_001;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour Document complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="Document">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FIToFIPmtStsRpt" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.11}FIToFIPaymentStatusReportV11"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Document", propOrder = {
    "fiToFIPmtStsRpt"
})
public class Document {

    @XmlElement(name = "FIToFIPmtStsRpt", required = true)
    protected FIToFIPaymentStatusReportV11 fiToFIPmtStsRpt;

    /**
     * Obtient la valeur de la propri�t� fiToFIPmtStsRpt.
     * 
     * @return
     *     possible object is
     *     {@link FIToFIPaymentStatusReportV11 }
     *     
     */
    public FIToFIPaymentStatusReportV11 getFIToFIPmtStsRpt() {
        return fiToFIPmtStsRpt;
    }

    /**
     * D�finit la valeur de la propri�t� fiToFIPmtStsRpt.
     * 
     * @param value
     *     allowed object is
     *     {@link FIToFIPaymentStatusReportV11 }
     *     
     */
    public void setFIToFIPmtStsRpt(FIToFIPaymentStatusReportV11 value) {
        this.fiToFIPmtStsRpt = value;
    }

}
