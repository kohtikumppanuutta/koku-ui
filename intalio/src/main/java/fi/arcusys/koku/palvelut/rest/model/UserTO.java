package fi.arcusys.koku.palvelut.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * Aug 11, 2011
 */
@XmlRootElement
public class UserTO {
    private String userUid = "guest";

    /**
     * @return the userUid
     */
    
    public String getUserUid() {
        return userUid;
    }

    /**
     * @param userUid the userUid to set
     */
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
    
}
