package com.jesoftware.paymentAuthorizer.service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class InputLineReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readNextLine() {
        return scanner.nextLine();
    }
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }
}
