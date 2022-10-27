package sale_server.data_type;

import java.io.Serializable;

public enum Pays implements Serializable {
    FRANCE("FR"),
    GERMANY("DE"),
    ITALY("IT"),
    SPAIN("ES"),
    UNITED_KINGDOM("UK"),
    UNITED_STATES("US");

    private String code;

    Pays(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Pays fromByte(byte[] b) {
        // TODO retourner en décomposant suivant lettre 1 et 2.
        return Pays.FRANCE;
    }

    public static Pays fromCode(String code) {
        for (Pays pays : Pays.values()) {
            if (pays.getCode().equals(code)) {
                return pays;
            }
        }
        return null;
    }
}
