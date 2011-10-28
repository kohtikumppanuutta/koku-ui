
package fi.arcusys.koku.palvelut.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "VeeraServicesFacade", targetNamespace = "http://facade.palvelut.palvelukanava.koku.arcusys.fi/", wsdlLocation = "http://127.0.0.1:8080/palvelut-web-service-0.1-SNAPSHOT/VeeraServicesSessionBean?wsdl")
public class VeeraServicesWsClient extends Service
{

    private final static URL VEERASERVICESFACADE_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:8080/palvelut-web-service-0.1-SNAPSHOT/VeeraServicesSessionBean?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        VEERASERVICESFACADE_WSDL_LOCATION = url;
    }

    public VeeraServicesWsClient(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public VeeraServicesWsClient(URL wsdlLocation) {
        super(wsdlLocation, new QName("http://facade.palvelut.palvelukanava.koku.arcusys.fi/", "VeeraServicesFacade"));
    }

    public VeeraServicesWsClient() {
        super(VEERASERVICESFACADE_WSDL_LOCATION, new QName("http://facade.palvelut.palvelukanava.koku.arcusys.fi/", "VeeraServicesFacade"));
    }

    /**
     * 
     * @return
     *     returns VeeraServicesFacadeWS
     */
    @WebEndpoint(name = "VeeraServicesFacadePort")
    public VeeraServicesFacadeWS getVeeraServicesFacadePort() {
        return (VeeraServicesFacadeWS)super.getPort(new QName("http://facade.palvelut.palvelukanava.koku.arcusys.fi/", "VeeraServicesFacadePort"), VeeraServicesFacadeWS.class);
    }

}
