package sale_server.reponse.pone;

import java.util.HashMap;
import java.util.UUID;

public class AjoutEnergieReponse extends PoneReponse {

    private final HashMap<UUID, Boolean> resultAjoutEnergie;

    public AjoutEnergieReponse(HashMap<UUID, Boolean> resultAjoutEnergie) {
        this.resultAjoutEnergie = resultAjoutEnergie;
    }

    public HashMap<UUID, Boolean> getResultAjoutEnergie() {
        return resultAjoutEnergie;
    }

    public boolean setResultAjoutEnergie(UUID idEnergie, boolean result) {
        return Boolean.TRUE.equals(resultAjoutEnergie.put(idEnergie, result));
    }
}
