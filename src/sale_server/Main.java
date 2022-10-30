package sale_server;

import sale_server.data_type.*;
import sale_server.data_type.statut.StatutAjout;
import sale_server.identifier.PoneID;
import sale_server.identifier.TareID;
import sale_server.reponse.AbstractReponse;
import sale_server.reponse.pone.AjoutEnergieReponse;
import sale_server.reponse.pone.SuppressionEnergieReponse;
import sale_server.reponse.tare.EnergieAchatReponse;
import sale_server.reponse.tare.EnergieListeReponse;
import sale_server.requete.pone.AjoutEnergieRequete;
import sale_server.requete.pone.SuppressionEnergieRequete;
import sale_server.requete.tare.EnergieAchatRequete;
import sale_server.requete.tare.EnergieListeRequete;

import java.util.*;

public class Main {

    public static MarcheEnergie marcheEnergie;

    public static void main(String[] args) {
        ServeurSALE serveurSALE = new ServeurSALE(3031);
        loadFakeEnergie();

        serveurSALE.start();
        System.out.println("ServeurSALE started, \"help\" for help");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("cmd> ");
            String input = scanner.nextLine();
            if (input.equals("exit")) {
                serveurSALE.stopServer();
                System.out.println("ServeurSALE stopped");
                break;
            } else if (input.equals("restart")) {
                serveurSALE.killServer();
                System.out.println("ServeurSALE stopped");
                serveurSALE.startServer();
                System.out.println("ServeurSALE restarted");
            } else if (input.equals("launch_test")) {
                System.out.println("Test launched !");
                test();
            } else if (input.equals("help")) {
                System.out.println("exit: stop the server");
                System.out.println("restart: restart the server");
                System.out.println("launch_test: launch a test");
            } else {
                System.out.println("Unknown command, \"help\" for help");
            }
        }

        scanner.close();
    }

    public static void loadFakeEnergie() {
        // Mock of the market
        marcheEnergie = new MarcheEnergie();
        // create mock offers with different types
        marcheEnergie.addOffer(new PoneOffreEnergie(10223, EnergieType.ELECTRICITY,
                ExtractionType.NUCLEAR, Pays.FRANCE, 100,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(10224, EnergieType.ELECTRICITY,
                ExtractionType.SOLAR, Pays.GERMANY, 100,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(34225, EnergieType.ELECTRICITY,
                ExtractionType.WIND, Pays.ITALY, 250,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(10226, EnergieType.ELECTRICITY,
                ExtractionType.BIOFUEL, Pays.UNITED_KINGDOM, 230,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(1027, EnergieType.ELECTRICITY,
                ExtractionType.GEOTHERMAL, Pays.SPAIN, 100,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(54228, EnergieType.GAS,
                ExtractionType.CONVENTIONAL, Pays.UNITED_STATES, 90,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(33428, EnergieType.GAS,
                ExtractionType.CONVENTIONAL, Pays.SPAIN, 80,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(5428, EnergieType.OIL,
                ExtractionType.CONVENTIONAL, Pays.UNITED_STATES, 100,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.addOffer(new PoneOffreEnergie(54228, EnergieType.COAL,
                ExtractionType.CONVENTIONAL, Pays.GERMANY, 100,
                new DispoAt(21, 5, 2018, 0, 0)));
        marcheEnergie.getOffreEnergies().get(5).soldTo(new TareID(43,"tare1"));
    }

    public static void test() {
        ClientUDPTester clientUDPTester = new ClientUDPTester("localhost", 3031, 3032);

        List<Pays> paysP = new ArrayList<>();
        paysP.add(Pays.FRANCE);
        paysP.add(Pays.UNITED_STATES);

        // Test requete de liste d'energie
        TareID tareID = new TareID(45884,"tare1");
        EnergieListeRequete energieListeRequete = new EnergieListeRequete(paysP, new ArrayList<>(),
                149000,500,200,1500, null,
                null, null, tareID);

        clientUDPTester.envoyerRequete(energieListeRequete, new ClientUDPTester.ReponseHandler() {
            @Override
            public void handle(AbstractReponse reponse) {
                System.out.println("[CLI] Réponse du serveur : " + reponse.getStatusMessage());
                System.out.println("[CLI] Type de réponse : " + reponse.getClass().getSimpleName());
                // Nombre d'offre
                System.out.println("[CLI] Nombre d'offre : " + ((EnergieListeReponse) reponse).getListeEnergie().length);
                // Afficher les offres
                EnergieListeReponse energieListeReponse = (EnergieListeReponse) reponse;
                System.out.println("[CLI] Offres : ");
                for (PoneOffreEnergie offreEnergie : energieListeReponse.getListeEnergie()) {
                    System.out.println("[CLI] " + offreEnergie);
                }
            }
        });

        // Test energie achat requete
        EnergieAchatRequete energieAchatRequete = new EnergieAchatRequete(tareID, new UUID[] {
                UUID.randomUUID(),
                Main.marcheEnergie.getOffreEnergies().get(0).getOffreID(),
                Main.marcheEnergie.getOffreEnergies().get(3).getOffreID(),
                Main.marcheEnergie.getOffreEnergies().get(5).getOffreID(),
        });

        clientUDPTester.envoyerRequete(energieAchatRequete, new ClientUDPTester.ReponseHandler() {
            @Override
            public void handle(AbstractReponse reponse) {
                System.out.println("[CLI] Réponse du serveur : " + reponse.getStatusMessage());
                System.out.println("[CLI] Type de réponse : " + reponse.getClass().getSimpleName());
                // Nombre d'offre
                System.out.println("[CLI] Nombre d'offre : " + ((EnergieAchatReponse) reponse).getResultAchatEnergie().size());
                // Afficher les offres
                EnergieAchatReponse energieAchatReponse = (EnergieAchatReponse) reponse;
                System.out.println("[CLI] Result d'achat : ");
                for (UUID offreEnergie : energieAchatReponse.getResultAchatEnergie().keySet()) {
                    System.out.println("[CLI] " + offreEnergie + " : " + energieAchatReponse.getResultAchatEnergie().get(offreEnergie));
                }
            }
        });

        // Test requête d'ajout energie
        PoneID poneID = new PoneID(1558, "pone1");
        PoneOffreEnergie poneOffreEnergie[] = {
                new PoneOffreEnergie(10223, EnergieType.ELECTRICITY,
                ExtractionType.NUCLEAR, Pays.FRANCE, 100,
                new DispoAt(21, 5, 2018, 0, 0)),

                new PoneOffreEnergie(10224, EnergieType.ELECTRICITY,
                        ExtractionType.SOLAR, Pays.GERMANY, 100,
                        new DispoAt(21, 5, 2018, 0, 0))
        };

        AjoutEnergieRequete energieAjoutRequete = new AjoutEnergieRequete(poneID, poneOffreEnergie);
        clientUDPTester.envoyerRequete(energieAjoutRequete, new ClientUDPTester.ReponseHandler() {
            @Override
            public void handle(AbstractReponse reponse) {
                System.out.println("[CLI] Réponse du serveur : " + reponse.getStatusMessage());
                System.out.println("[CLI] Type de réponse : " + reponse.getClass().getSimpleName());
                // Afficher les offres
                AjoutEnergieReponse ajoutEnergieReponse = (AjoutEnergieReponse) reponse;
                System.out.println("[CLI] Les réponses : ");
                for (UUID status : ajoutEnergieReponse.getResultAjoutEnergie().keySet()) {
                    System.out.println("[CLI] " + status + " : " + ajoutEnergieReponse.getResultAjoutEnergie().get(status));
                }
            }
        });

        // Test requête de suppression energie
        SuppressionEnergieRequete suppressionEnergieRequete = new SuppressionEnergieRequete(poneID,
                new UUID[]{
                        UUID.randomUUID(),
                        poneOffreEnergie[0].getOffreID(),
                        poneOffreEnergie[1].getOffreID()
                });
        clientUDPTester.envoyerRequete(suppressionEnergieRequete, new ClientUDPTester.ReponseHandler() {
            @Override
            public void handle(AbstractReponse reponse) {
                System.out.println("[CLI] Réponse du serveur : " + reponse.getStatusMessage());
                System.out.println("[CLI] Type de réponse : " + reponse.getClass().getSimpleName());
                // Afficher les offres
                SuppressionEnergieReponse suppressionEnergieReponse = (SuppressionEnergieReponse) reponse;
                System.out.println("[CLI] Les réponses : ");
                for (UUID status : suppressionEnergieReponse.getResultSuppressionEnergie().keySet()) {
                    System.out.println("[CLI] " + status + " : " +
                            suppressionEnergieReponse.getResultSuppressionEnergie().get(status));
                }
            }
        });
    }
}