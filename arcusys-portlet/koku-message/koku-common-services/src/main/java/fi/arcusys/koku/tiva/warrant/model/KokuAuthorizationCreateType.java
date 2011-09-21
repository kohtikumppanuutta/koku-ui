package fi.arcusys.koku.tiva.warrant.model;

public enum KokuAuthorizationCreateType {

    ELECTRONIC("Electronic"),
    NON_ELECTRONIC("Non_Electronic");
    
    private final String value;

    KokuAuthorizationCreateType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static KokuAuthorizationCreateType fromValue(String v) {
        for (KokuAuthorizationCreateType c: KokuAuthorizationCreateType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}