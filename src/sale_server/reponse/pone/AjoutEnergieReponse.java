package sale_server.reponse.pone;

import sale_server.data_type.statut.StatutAjout;

import java.util.HashMap;
import java.util.UUID;

public class AjoutEnergieReponse extends PoneReponse {

    private final HashMap<UUID, StatutAjout> resultAjoutEnergie;

    public AjoutEnergieReponse(HashMap<UUID, StatutAjout> resultAjoutEnergie) {
        this.resultAjoutEnergie = resultAjoutEnergie;
    }

    public HashMap<UUID, StatutAjout> getResultAjoutEnergie() {
        return resultAjoutEnergie;
    }

    public void putResultAjoutEnergie(UUID idEnergie, StatutAjout result) {
        resultAjoutEnergie.put(idEnergie, result);
    }
}
