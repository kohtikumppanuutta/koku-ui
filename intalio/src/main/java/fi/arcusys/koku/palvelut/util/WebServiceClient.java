package fi.arcusys.koku.palvelut.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Sample class for KokuAppointmentProcessingServiceImpl <br/>
 * http://gatein.intra.arcusys.fi:8080/kv-model-0.1-SNAPSHOT/KokuAppointmentProcessingServiceImpl
 * 
 * @author Toni Turunen
 *
 */
public class WebServiceClient {

    private static final String MESSAGE =
        "<message xmlns=\"http://tempuri.org\">Hello Web Service World</message>";

    private final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

    public void setDefaultUri(String defaultUri) {
        webServiceTemplate.setDefaultUri(defaultUri);
    }

    // send to the configured default URI
    public ByteArrayOutputStream simpleSendAndReceive(String data) {
        StreamSource source = new StreamSource(new StringReader(data));
        ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
        StreamResult result = new StreamResult(System.out);
        webServiceTemplate.sendSourceAndReceiveToResult(source, result);
        return stream;
    }

    // send to an explicit URI
    public void customSendAndReceive() {
        StreamSource source = new StreamSource(new StringReader(MESSAGE));
        StreamResult result = new StreamResult(System.out);
        webServiceTemplate.sendSourceAndReceiveToResult("http://localhost:8080/AnotherWebService",
            source, result);
    }

}
