import sale_server.ServeurSALE;
import sale_server.requete.AbstractRequete;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ServeurSALE serveurSALE = new ServeurSALE(1234);

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
                test();
                System.out.println("Test launched");
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

    public static void test() {
        ClientUDPTester clientUDPTester = new ClientUDPTester("localhost", 1234);
        // TODO faire des tests ici
    }
}