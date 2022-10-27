package sale_server.data_type;

import java.io.Serializable;

public enum ExtractionType implements Serializable {
    CONVENTIONAL(7),
    NUCLEAR(6),
    SOLAR(5),
    WIND(4),
    HYDROELECTRIC(3),
    GEOTHERMAL(2),
    BIOFUEL(1),
    UNRECOGNIZED(0);

    ExtractionType(int numSerial) {
        this.numSerial = numSerial;
    }

    private final int numSerial;

    public int getNumSerial() {
        return numSerial;
    }

    static ExtractionType fromSerial(int numSerial) {
        for (ExtractionType extractionType : ExtractionType.values()) {
            if (extractionType.getNumSerial() == numSerial) {
                return extractionType;
            }
        }
        return UNRECOGNIZED;
    }
}
