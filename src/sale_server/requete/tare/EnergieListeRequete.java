package sale_server.requete.tare;

import sale_server.data_type.DispoAt;
import sale_server.data_type.EnergieType;
import sale_server.data_type.ExtractionType;
import sale_server.data_type.Pays;
import sale_server.identifier.TareID;

import java.util.*;

public class EnergieListeRequete extends TareRequete {
    final private List<Pays> originePreferentiel;
    final private List<Pays> origineInterdit;

    private final int budgetMax;
    private final int prixMaxUE;

    private final int quantiteMin;
    private final int quantiteDemandee;

    private final DispoAt dispoAt;

    private final ExtractionType extractionType;

    private final EnergieType typeEnergie;

    public EnergieListeRequete(List<Pays> originePreferentiel, List<Pays> origineInterdit, int budgetMax,
                               int prixMaxUE, int quantiteMin, int quantiteDemandee, DispoAt dispoAt,
                               ExtractionType extractionType, EnergieType typeEnergie, TareID tareID) {
        super(tareID);
        this.originePreferentiel = originePreferentiel;
        this.origineInterdit = origineInterdit;
        this.budgetMax = budgetMax;
        this.prixMaxUE = prixMaxUE;
        this.quantiteMin = quantiteMin;
        this.quantiteDemandee = quantiteDemandee;
        this.extractionType = extractionType;
        this.dispoAt = dispoAt;
        this.typeEnergie = typeEnergie;
    }

    public EnergieListeRequete(List<Pays> originePreferentiel, List<Pays> origineInterdit, int budgetMax,
                               int prixMaxUE, int quantiteMin, int quantiteDemandee, DispoAt dispoAt,
                               ExtractionType extractionType, EnergieType typeEnergie, TareID tareID, int portReponse) {
        super(tareID, portReponse);
        this.originePreferentiel = originePreferentiel;
        this.origineInterdit = origineInterdit;
        this.budgetMax = budgetMax;
        this.prixMaxUE = prixMaxUE;
        this.quantiteMin = quantiteMin;
        this.quantiteDemandee = quantiteDemandee;
        this.extractionType = extractionType;
        this.dispoAt = dispoAt;
        this.typeEnergie = typeEnergie;
    }

    public List<Pays> getOriginePreferentiel() {
        return originePreferentiel;
    }

    public List<Pays> getOrigineInterdit() {
        return origineInterdit;
    }

    public int getBudgetMax() {
        return budgetMax;
    }

    public int getPrixMaxUE() {
        return prixMaxUE;
    }

    public int getQuantiteMin() {
        return quantiteMin;
    }

    public int getQuantiteDemandee() {
        return quantiteDemandee;
    }

    public Optional<ExtractionType> getExtractionType() {
        return Optional.ofNullable(extractionType);
    }

    public Optional<EnergieType> getTypeEnergie() {
        return Optional.ofNullable(typeEnergie);
    }

    public Optional<DispoAt> getDispoAt() {
        return Optional.ofNullable(dispoAt);
    }

}