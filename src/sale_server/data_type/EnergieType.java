package sale_server.data_type;

import java.io.Serializable;

public enum EnergieType implements Serializable {
    ELECTRICITY(5),
    GAS(4),
    OIL(3),
    COAL(2),
    FISSIBLE(1),
    UNRECOGNIZED(0);

    private int serialNum;

    EnergieType(int serialNum) {
        this.serialNum = serialNum;
    }

    public int getSerialNum() {
        return serialNum;
    }

    static EnergieType fromSerial(int serialNum) {
        for (EnergieType energieType : EnergieType.values()) {
            if (energieType.getSerialNum() == serialNum) {
                return energieType;
            }
        }
        return UNRECOGNIZED;
    }
}
