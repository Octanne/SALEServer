package sale_server;

import sale_server.data_type.Pays;
import sale_server.data_type.PoneOffreEnergie;
import sale_server.requete.tare.EnergieListeRequete;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarcheEnergie {

    private List<PoneOffreEnergie> offreEnergies;

    public MarcheEnergie() {
        offreEnergies = new ArrayList<>();
    }

    public void addOffer(PoneOffreEnergie offer) {
        offreEnergies.add(offer);
    }

    public boolean removeOffer(PoneOffreEnergie offer) {
        return offreEnergies.remove(offer);
    }

    public PoneOffreEnergie getEnergieFromUUID(UUID id) {
        return offreEnergies.stream().filter(offer -> offer.getOffreID().equals(id)).findFirst().orElse(null);
    }

    public boolean hasEnergieFromUUID(UUID id) {
        return offreEnergies.stream().anyMatch(offer -> offer.getOffreID().equals(id));
    }

    public List<PoneOffreEnergie> getOffreEnergies() {
        return offreEnergies;
    }

    public boolean filtrerEnergie(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        return filtrerQuantiteEnergie(requete, poneOffreEnergie) && filtrerPrix(requete,poneOffreEnergie)
                && filtrerOrigine(requete,poneOffreEnergie) && filtrerExtraction(requete,poneOffreEnergie)
                && filtrerType(requete,poneOffreEnergie);
    }

    public boolean filtrerOrigine(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        List<Pays> origineI = requete.getOrigineInterdit();
        List<Pays> origineP = requete.getOriginePreferentiel();

        return (origineI.isEmpty() && origineP.isEmpty()) ||
                (!origineI.isEmpty() && !origineI.contains(poneOffreEnergie.getOrigine())) ||
                (!origineP.isEmpty() && origineP.contains(poneOffreEnergie.getOrigine()));
    }

    public boolean filtrerExtraction(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        return requete.getExtractionType().isEmpty() || requete.getExtractionType().get().equals(poneOffreEnergie.getExtractionType());
    }

    public boolean filtrerType(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        return requete.getTypeEnergie().isEmpty() || requete.getExtractionType().get().equals(poneOffreEnergie.getTypeEnergie());
    }

    public boolean filtrerQuantiteEnergie(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        return (requete.getQuantiteMin() == 0 || requete.getQuantiteMin() <= poneOffreEnergie.getQuantiteEnergie()) &&
                requete.getQuantiteDemandee() <= poneOffreEnergie.getQuantiteEnergie();
    }

    public boolean filtrerPrix(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        return (requete.getPrixMaxUE() == 0 || requete.getPrixMaxUE() >= poneOffreEnergie.getPrixUE()) &&
            (requete.getBudgetMax() == 0 || requete.getBudgetMax() >= poneOffreEnergie.getPrixUE() * requete.getQuantiteDemandee());
    }

    public boolean filtrerInstantDisponible(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        return poneOffreEnergie.isDisponible();
    }

    public boolean filtrerDisponibleAt(EnergieListeRequete requete, PoneOffreEnergie poneOffreEnergie) {
        return requete.getDispoAt().isPresent() && poneOffreEnergie.isDisponibleAt(requete.getDispoAt().get().getCalendar());
    }

    public int getSumQuantiteEnergie(List<PoneOffreEnergie> poneOffreEnergies) {
        int sum = 0;
        for (PoneOffreEnergie poneOffreEnergie : poneOffreEnergies) {
            sum += poneOffreEnergie.getQuantiteEnergie();
        }
        return sum;
    }

}
