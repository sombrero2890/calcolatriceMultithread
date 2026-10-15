package it.bianconi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345; 

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server calcolatrice avviata");
            System.out.println("In ascolto sulla porta: " + PORT);

            // Ciclo infinito per accettare continui client
            while (true) {
                // Il server si mette in attesa di un client
                Socket clientSocket = serverSocket.accept(); 
                System.out.println("Nuovo client connesso da: " + clientSocket.getRemoteSocketAddress());

                // Crea il gestore del client e lo avvia in un thread separato
                ClientHandler handler = new ClientHandler(clientSocket);
                Thread thread = new Thread(handler);
                thread.start(); 
            }
        } catch (IOException e) {
            System.err.println("Errore nel server: " + e.getMessage());
        }
    }
}
