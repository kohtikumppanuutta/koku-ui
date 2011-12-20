/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.kks.model;

/**
 * Consent request object
 * 
 * @author Ixonos / tuomape
 *
 */
public class ConsentRequest {

  private String customerId;
  private String consentType;

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getConsentType() {
    return consentType;
  }

  public void setConsentType(String consentType) {
    this.consentType = consentType;
  }

}
