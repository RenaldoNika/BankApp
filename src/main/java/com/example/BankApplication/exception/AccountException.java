package com.example.BankApplication.exception;

import java.util.Scanner;

public class AccountException extends RuntimeException {
    public AccountException(String message) {
        super(message);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ChatBot: Pershendetje! Si mund te ndihmoj?");

        while (true) {
            System.out.print("Ti: ");
            String input = scanner.nextLine().toLowerCase();

            if (input.contains("pershendetje") || input.contains("hello")) {
                System.out.println("ChatBot: Pershendetje! Si je?");
            } else if (input.contains("si je")) {
                System.out.println("ChatBot: Shume mire, faleminderit! Po ti?");
            } else if (input.contains("mirupafshim") || input.contains("bye")) {
                System.out.println("ChatBot: Mirupafshim! Kalofsh bukur 😊");
                break;
            } else {
                System.out.println("ChatBot: Nuk te kuptova. Mund ta perserisesh?");
            }
        }
        scanner.close();
    }
}

