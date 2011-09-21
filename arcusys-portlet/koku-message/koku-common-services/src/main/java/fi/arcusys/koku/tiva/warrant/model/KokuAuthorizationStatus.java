package fi.arcusys.koku.tiva.warrant.model;

public enum KokuAuthorizationStatus {

    OPEN("Open"),
    VALID("Valid"),
    EXPIRED("Expired"),
    REVOKED("Revoked"),
    DECLINED("Declined");
    
    
    private final String value;

    KokuAuthorizationStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static KokuAuthorizationStatus fromValue(String v) {
        for (KokuAuthorizationStatus c: KokuAuthorizationStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}