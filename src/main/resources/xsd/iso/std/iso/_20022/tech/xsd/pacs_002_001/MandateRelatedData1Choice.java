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
 * <p>Classe Java pour MandateRelatedData1Choice complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType name="MandateRelatedData1Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="DrctDbtMndt" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.11}MandateRelatedInformation14" minOccurs="0"/>
 *         &lt;element name="CdtTrfMndt" type="{urn:iso:std:iso:20022:tech:xsd:pacs.002.001.11}CreditTransferMandateData1" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MandateRelatedData1Choice", propOrder = {
    "drctDbtMndt",
    "cdtTrfMndt"
})
public class MandateRelatedData1Choice {

    @XmlElement(name = "DrctDbtMndt")
    protected MandateRelatedInformation14 drctDbtMndt;
    @XmlElement(name = "CdtTrfMndt")
    protected CreditTransferMandateData1 cdtTrfMndt;

    /**
     * Obtient la valeur de la propri�t� drctDbtMndt.
     * 
     * @return
     *     possible object is
     *     {@link MandateRelatedInformation14 }
     *     
     */
    public MandateRelatedInformation14 getDrctDbtMndt() {
        return drctDbtMndt;
    }

    /**
     * D�finit la valeur de la propri�t� drctDbtMndt.
     * 
     * @param value
     *     allowed object is
     *     {@link MandateRelatedInformation14 }
     *     
     */
    public void setDrctDbtMndt(MandateRelatedInformation14 value) {
        this.drctDbtMndt = value;
    }

    /**
     * Obtient la valeur de la propri�t� cdtTrfMndt.
     * 
     * @return
     *     possible object is
     *     {@link CreditTransferMandateData1 }
     *     
     */
    public CreditTransferMandateData1 getCdtTrfMndt() {
        return cdtTrfMndt;
    }

    /**
     * D�finit la valeur de la propri�t� cdtTrfMndt.
     * 
     * @param value
     *     allowed object is
     *     {@link CreditTransferMandateData1 }
     *     
     */
    public void setCdtTrfMndt(CreditTransferMandateData1 value) {
        this.cdtTrfMndt = value;
    }

}
