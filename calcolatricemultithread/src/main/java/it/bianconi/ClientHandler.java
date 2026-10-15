package it.bianconi;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        // try-with-resources per chiudere automaticamente gli stream e il socket alla fine
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Legge la stringa inviata dal client (es. "10.5 + 5")
            String richiesta = in.readLine();
            if (richiesta == null) return;

            // Divide la stringa usando lo spazio come separatore
            String[] parti = richiesta.trim().split("\\s+");

            // Controllo sintassi: ci devono essere esattamente 3 elementi (num1, operatore, num2)
            if (parti.length != 3) {
                out.println("ERRORE: Sintassi non valida. Usa il formato: numero operatore numero");
                return;
            }

            double num1, num2;
            String operatore = parti[1];

            // Validazione dei numeri
            try {
                num1 = Double.parseDouble(parti[0]);
                num2 = Double.parseDouble(parti[2]);
            } catch (NumberFormatException e) {
                out.println("ERRORE: I valori inseriti devono essere numeri decimali o interi.");
                return;
            }

            // Elaborazione dell'operazione aritmetica
            String risposta;
            switch (operatore) {
                case "+":
                    risposta = "RISULTATO: " + (num1 + num2);
                    break;
                case "-":
                    risposta = "RISULTATO: " + (num1 - num2);
                    break;
                case "*":
                    risposta = "RISULTATO: " + (num1 * num2);
                    break;
                case "/":
                    if (num2 == 0) {
                        risposta = "ERRORE: Divisione per zero non consentita.";
                    } else {
                        risposta = "RISULTATO: " + (num1 / num2);
                    }
                    break;
                default:
                    risposta = "ERRORE: Operatore '" + operatore + "' non riconosciuto. Usa +, -, * o /.";
                    break;
            }

            // Invia il risultato o l'errore al client
            out.println(risposta);

        } catch (IOException e) {
            System.err.println("Errore di comunicazione con il client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Connessione chiusa con il client.");
            } catch (IOException e) {
                System.err.println("Impossibile chiudere il socket del client: " + e.getMessage());
            }
        }
    }
}
 

