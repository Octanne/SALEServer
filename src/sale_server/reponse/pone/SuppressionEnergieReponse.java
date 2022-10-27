package sale_server.reponse.pone;

import java.util.HashMap;
import java.util.UUID;

public class SuppressionEnergieReponse extends PoneReponse {

    private final HashMap<UUID, Boolean> resultSuppressionEnergie;

    public SuppressionEnergieReponse(HashMap<UUID, Boolean> resultSuppressionEnergie) {
        this.resultSuppressionEnergie = resultSuppressionEnergie;
    }

    public HashMap<UUID, Boolean> getResultSuppressionEnergie() {
        return resultSuppressionEnergie;
    }

    public boolean setResultSuppressionEnergie(UUID idEnergie, boolean result) {
        return Boolean.TRUE.equals(resultSuppressionEnergie.put(idEnergie, result));
    }
}
