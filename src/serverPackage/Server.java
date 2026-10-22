package serverPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import commonPackage.Operation;
public class Server {

    // Doit être IDENTIQUE dans client.java
    private static final String SERVER_IP = "192.168.56.1";
    private static final int PORT = 1234;

    public static void main(String[] args) {
        try {
            InetAddress adresse = InetAddress.getByName(SERVER_IP);
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(adresse, PORT));

            System.out.println("SERVEUR (échange d'objets) en attente de la connexion d'un client");
            System.out.println("Écoute sur " + adresse.getHostAddress() + ":" + PORT);

            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté : "
                    + socket.getInetAddress().getHostAddress());

            // Même ordre que le client : flux de sortie d'abord, puis flush, puis flux d'entrée
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                System.out.println("En attente d'un objet du client...");

                // c) Réception de l'objet
                Object recu = in.readObject();
                if (recu instanceof String) {
                    out.writeObject("Au revoir !");
                    out.flush();
                    break;
                }
                Operation op = (Operation) recu;
                int x = op.getOperande1();
                char operateur = op.getOperateur();
                int y = op.getOperande2();
                System.out.println("Objet reçu : " + op);
                String resultat = calculer(x, operateur, y);
                out.writeObject(resultat);
                out.flush();
                System.out.println("Résultat envoyé : " + resultat);
            }
            in.close();
            out.close();
            socket.close();
            serverSocket.close();
            System.out.println("Connexion fermée côté serveur.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static String calculer(int x, char operateur, int y) {
        switch (operateur) {
            case '+':
                return x + " + " + y + " = " + ((long) x + y);
            case '-':
                return x + " - " + y + " = " + ((long) x - y);
            case '*':
                return x + " * " + y + " = " + ((long) x * y);
            case '/':
                if (y == 0) {
                    return "Erreur : division par zéro impossible";
                }
                return x + " / " + y + " = " + ((double) x / y);
            default:
                return "Erreur : opérateur invalide (" + operateur + ")";
        }
    }
}