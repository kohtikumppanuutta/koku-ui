
package fi.arcusys.koku.palvelut.ws.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for findCategoryByEntryAndCompanyIdResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="findCategoryByEntryAndCompanyIdResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://facade.palvelut.palvelukanava.koku.arcusys.fi/}veeraCategoryDTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findCategoryByEntryAndCompanyIdResponse", propOrder = {
    "_return"
})
public class FindCategoryByEntryAndCompanyIdResponse {

    @XmlElement(name = "return")
    protected VeeraCategoryDTO _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link VeeraCategoryDTO }
     *     
     */
    public VeeraCategoryDTO getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link VeeraCategoryDTO }
     *     
     */
    public void setReturn(VeeraCategoryDTO value) {
        this._return = value;
    }

}
