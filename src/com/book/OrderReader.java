package com.book;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Supplier;

public class OrderReader {

    private OrderReader() {
    }

    static Supplier<String> fileSupplier(BufferedReader reader) {
        return () -> {
            try {
                return reader.readLine();
            } catch (IOException e) {
                System.out.println("Error! Unable to read file.");
            }
            return null;
        };
    }
}
