package com.book;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.NoSuchElementException;

import static java.nio.file.Files.*;

public class App {
    public static void main(String[] args) throws IOException {
        var processor = new OrderBook();

        try (var reader = newBufferedReader(Path.of("input.txt"));
             var writer = newBufferedWriter(Path.of("output.txt"))) {
            processor.getOrder(OrderReader.fileSupplier(reader), OrderWriter.fileConsumer(writer));
            System.out.println("The output.txt file was created successfully.");
        } catch (NoSuchFileException e) {
            System.out.println("Error! File not found.");
        } catch (ArrayIndexOutOfBoundsException | NoSuchElementException | NumberFormatException e) {
            System.out.println("Error! Incorrect data.");
        }
    }
}
