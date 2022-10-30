package sale_server.reponse.pone;

import sale_server.data_type.statut.StatutSuppression;

import java.util.HashMap;
import java.util.UUID;

public class SuppressionEnergieReponse extends PoneReponse {

    private final HashMap<UUID, StatutSuppression> resultSuppressionEnergie;

    public SuppressionEnergieReponse(HashMap<UUID, StatutSuppression> resultSuppressionEnergie) {
        this.resultSuppressionEnergie = resultSuppressionEnergie;
    }

    public HashMap<UUID, StatutSuppression> getResultSuppressionEnergie() {
        return resultSuppressionEnergie;
    }

    public void putResultSuppressionEnergie(UUID idEnergie, StatutSuppression result) {
        resultSuppressionEnergie.put(idEnergie, result);
    }
}
