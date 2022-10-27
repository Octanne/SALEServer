package sale_server.reponse.tare;

import java.util.HashMap;
import java.util.UUID;

public class EnergieAchatReponse extends TareReponse {

    private final HashMap<UUID, Boolean> resultAchatEnergie;

    public EnergieAchatReponse(HashMap<UUID, Boolean> resultAchatEnergie) {
        this.resultAchatEnergie = resultAchatEnergie;
    }

    public HashMap<UUID, Boolean> getResultAchatEnergie() {
        return resultAchatEnergie;
    }

    public boolean setResultAchatEnergie(UUID idEnergie, boolean result) {
        return Boolean.TRUE.equals(resultAchatEnergie.put(idEnergie, result));
    }

}
