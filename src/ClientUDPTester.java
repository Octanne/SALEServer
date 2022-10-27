import sale_server.reponse.AbstractReponse;
import sale_server.reponse.UnreconizedReponse;
import sale_server.requete.AbstractRequete;

import java.io.*;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientUDPTester {

    public final int port;
    public final String addresseStr;

    /**
     * Construit un client UDP
     * @param addresse l'adresse du serveur
     * @param port le port du serveur
     */
    public ClientUDPTester(String addresse, int port) {
        this.port = port;
        this.addresseStr = addresse;
    }

    /**
     * Construit un Client avec les paramètres par défaut
     * addresse : localhost
     * port : 3031
     */
    public ClientUDPTester() {
        addresseStr = "localhost";
        port = 3031;
    }

    /**
     * Envoie une requete au serveur et execute le traitement de la reponse
     *
     * @param requete la requete a envoyer
     * @param handler le handler de la requete
     * @return si la requete a ete traitee
     */
    public boolean envoyerRequete(AbstractRequete requete, ReponseHandler handler) {
        // Création de la socket
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch(SocketException e) {
            System.err.println("Erreur lors de la création du socket : " + e);
            return false;
        }

        // Transformation en tableau d'octets
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(requete);
        } catch(IOException e) {
            System.err.println("Erreur lors de la sérialisation : " + e);
            socket.close();
            return false;
        }

        // Création et envoi du segment UDP
        try {
            byte[] donnees = baos.toByteArray();
            InetAddress adresse = InetAddress.getByName(addresseStr);
            DatagramPacket msg = new DatagramPacket(donnees, donnees.length,
                                                    adresse, port);
            socket.send(msg);
        } catch(UnknownHostException e) {
            System.err.println("Erreur lors de la création de l'adresse : " + e);
            socket.close();
            return false;
        } catch(IOException e) {
            System.err.println("Erreur lors de l'envoi du message : " + e);
            socket.close();
            return false;
        }

        // Lecture de la réponse du serveur
        DatagramPacket msgRecu;
        try {
            byte[] tampon = new byte[8164];
            msgRecu = new DatagramPacket(tampon, tampon.length);
            socket.receive(msgRecu);
        } catch(IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e);
            socket.close();
            return false;
        }

        // Récupération de l'objet
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(msgRecu.getData());
            ObjectInputStream ois = new ObjectInputStream(bais);
            AbstractReponse reponse = (AbstractReponse) ois.readObject();
            System.out.println("Requete recue : " + reponse.getClass().getSimpleName());

            if (reponse instanceof UnreconizedReponse) {
                System.out.println("Requete non reconnue : " + reponse.getStatusMessage());
            } else {
                handler.handle(reponse);
            }
            return true;
        } catch(ClassNotFoundException e) {
            System.err.println("Objet reçu non reconnu : " + e);
            socket.close();
            return false;
        } catch(IOException e) {
            System.err.println("Erreur lors de la récupération de l'objet : " + e);
            socket.close();
            return false;
        }
    }

    public static abstract class ReponseHandler {
            public abstract void handle(AbstractReponse reponse);
    }
}