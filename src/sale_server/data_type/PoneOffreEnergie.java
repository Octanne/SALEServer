package sale_server.data_type;

import sale_server.data_type.DispoAt;
import sale_server.data_type.EnergieType;
import sale_server.data_type.ExtractionType;
import sale_server.data_type.Pays;
import sale_server.identifier.TareID;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Optional;

public class PoneOffreEnergie implements Serializable {

    private final int quantiteEnergie;

    private final EnergieType typeEnergie;
    private final ExtractionType extractionType;

    private final Pays origine;

    private final GregorianCalendar dateDisponibilite;
    private final int prixUE;

    private Optional<TareID> tareAcheteur;

    public PoneOffreEnergie(int quantiteEnergie, EnergieType typeEnergie, ExtractionType extractionType
            , Pays origine, int prixUE, DispoAt dispoAt) {
        this.quantiteEnergie = quantiteEnergie;
        this.typeEnergie = typeEnergie;
        this.extractionType = extractionType;
        this.origine = origine;
        this.prixUE = prixUE;
        this.dateDisponibilite = dispoAt.getCalendar();
    }

    public int getQuantiteEnergie() {
        return quantiteEnergie;
    }

    public EnergieType getTypeEnergie() {
        return typeEnergie;
    }

    public ExtractionType getExtractionType() {
        return extractionType;
    }

    public Pays getOrigine() {
        return origine;
    }

    public int getPrixUE() {
        return prixUE;
    }

    public String toString() {
        return "PoneOffreEnergie{" +
                "quantiteEnergie=" + quantiteEnergie +
                ", typeEnergie=" + typeEnergie +
                ", extractionType=" + extractionType +
                ", origine=" + origine +
                ", dateDisponibilite=" + timeToShow(dateDisponibilite) +
                ", prixUE=" + prixUE +
                '}';
    }

    private static String timeToShow(GregorianCalendar time) {
        return time.get(GregorianCalendar.YEAR) + "-" +
                time.get(GregorianCalendar.MONTH) + "-" +
                time.get(GregorianCalendar.DAY_OF_MONTH) + " " +
                time.get(GregorianCalendar.HOUR_OF_DAY) + ":" +
                time.get(GregorianCalendar.MINUTE);
    }

    public boolean hasTareAcheteur() {
        return tareAcheteur.isPresent();
    }

    public void setTareAcheteur(TareID tareAcheteur) {
        this.tareAcheteur = Optional.of(tareAcheteur);
    }

    public TareID getTareAcheteur() {
        return tareAcheteur.get();
    }

    public boolean isDisponible() {
        return dateDisponibilite.after(new GregorianCalendar());
    }

    public boolean isDisponibleAt(GregorianCalendar date) {
        return dateDisponibilite.after(date);
    }

    public byte[] formatCodeDeSuivie() {
        // TODO faire le formatage du code de suivi de cette unité d'énergie
        return new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    }

}
