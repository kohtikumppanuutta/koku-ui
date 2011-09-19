package fi.arcusys.koku.tiva.warrant.model;

public enum AuthorizationStatus {

    OPEN("Open"),
    VALID("Valid"),
    EXPIRED("Expired"),
    REVOKED("Revoked"),
    DECLINED("Declined");
    
    
    private final String value;

    AuthorizationStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthorizationStatus fromValue(String v) {
        for (AuthorizationStatus c: AuthorizationStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}