package fi.arcusys.koku.tiva.tietopyynto.model;

public enum KokuInformationAccessType {

    MANUAL("Manual"),
    PORTAL("Portal");
    
    private final String value;

    KokuInformationAccessType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static KokuInformationAccessType fromValue(String v) {
        for (KokuInformationAccessType c: KokuInformationAccessType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
