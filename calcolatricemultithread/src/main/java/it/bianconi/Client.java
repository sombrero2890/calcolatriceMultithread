package it.bianconi;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Scanner tastiera = new Scanner(System.in);

        System.out.println("=== INTERFACCIA CLIENT CALCOLATRICE ===");
        
        // Acquisizione dati dall'utente
        System.out.print("Inserisci il primo numero: ");
        String num1 = tastiera.nextLine().trim();

        System.out.print("Inserisci l'operatore (+, -, *, /): ");
        String operatore = tastiera.nextLine().trim();

        System.out.print("Inserisci il secondo numero: ");
        String num2 = tastiera.nextLine().trim();

        // Composizione della stringa da inviare separata da spazi
        String stringaDaInviare = num1 + " " + operatore + " " + num2;

        // Tentativo di connessione e comunicazione con il Server
        try (
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Connesso al server. Invio dati...");
            
            // Invia la stringa formattata
            out.println(stringaDaInviare);

            // Riceve la risposta elaborata dal server
            String rispostaServer = in.readLine();
            System.out.println("\n[SERVER] " + rispostaServer);

        } catch (IOException e) {
            System.err.println("Impossibile connettersi al server: " + e.getMessage());
        } finally {
            tastiera.close();
            System.out.println("\nConnessione terminata.");
        }
    }
}
