package com.book;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.function.Consumer;

public class OrderWriter {

    private OrderWriter() {
    }

    static Consumer<String> fileConsumer(BufferedWriter writer) {
        return s -> {
            try {
                writer.write(s);
                writer.newLine();
            } catch (IOException e) {
                System.out.println("Error! Unable to write to file.");
            }
        };
    }
}
