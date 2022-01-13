package com.book;

import java.util.Collections;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OrderBook {

    private final TreeMap<Integer, Integer> asks;
    private final TreeMap<Integer, Integer> bids;
    private static final String ERROR_MESSAGE = "Command not found";

    public OrderBook() {
        asks = new TreeMap<>();
        bids = new TreeMap<>(Collections.reverseOrder());
    }

    void getOrder(Supplier<String> source, Consumer<String> drain) {
        while (true) {
            String order = source.get();
            if (order == null) break;
            String[] myOrder = order.split(",");
            String commandType = myOrder[0];
            switch (commandType) {
                case "q" -> drain.accept(queryProcess(myOrder));
                case "u" -> updateProcess(myOrder);
                case "o" -> orderProcess(myOrder);
                default -> System.out.println(ERROR_MESSAGE);
            }
        }
    }

    private void updateProcess(String[] myOrder) {
        String command = myOrder[3];
        int price = Integer.parseInt(myOrder[1]);
        int size = Integer.parseInt(myOrder[2]);
        orderCheck(price, size, command);
    }

    private void orderProcess(String[] myOrder) {
        String command = myOrder[1];
        int size = Integer.parseInt(myOrder[2]);
        orderRemove(size, command);
    }

    private String queryProcess(String[] myOrder) {
        String command = myOrder[1];
        switch (command) {
            case "best_bid" -> {
                return getBestBid();
            }
            case "best_ask" -> {
                return getBestAsk();
            }
            case "size" -> {
                int price = Integer.parseInt(myOrder[2]);
                return printSize(price);
            }
            default -> System.out.println(ERROR_MESSAGE + " q,<command>");
        }
        return "Error";
    }

    private String getBestBid() {
        var first = bids.firstKey();
        var second = bids.firstEntry().getValue();
        return first + "," + second;
    }

    private String getBestAsk() {
        var first = asks.firstKey();
        var second = asks.firstEntry().getValue();
        return first + "," + second;
    }

    private String printSize(int price) {
        if (bids.get(price) != null) return bids.get(price).toString();
        if (asks.get(price) != null) return asks.get(price).toString();
        else return "0";
    }

    private void orderRemove(int size, String command) {
        switch (command) {
            case "buy" -> {
                while (size > 0 && !asks.isEmpty()) {
                    if (asks.firstEntry().getValue() == size) {
                        asks.remove(asks.firstKey(), size);
                        break;
                    }
                    var i = asks.firstEntry().getValue() - size;
                    asks.replace(asks.firstKey(), i);
                    size = 0;
                    if (i <= 0) {
                        asks.remove(asks.firstKey());
                        size = -i;
                    }
                }
            }
            case "sell" -> {
                while (size > 0 && !bids.isEmpty()) {
                    if (bids.firstEntry().getValue() == size) {
                        bids.remove(bids.firstKey(), size);
                        break;
                    }
                    var i = bids.firstEntry().getValue() - size;
                    bids.replace(bids.firstKey(), i);
                    size = 0;
                    if (i <= 0) {
                        bids.remove(bids.firstKey());
                        size = -i;
                    }
                }
            }
            default -> System.out.println(ERROR_MESSAGE + " o,<command>,<size>");
        }
    }

    private void orderCheck(int price, int size, String command) {
        switch (command) {
            case "bid" -> {
                if (price > 0 && size > 0) addToList(price, size, command);
                else bids.remove(price);
            }

            case "ask" -> {
                if (price > 0 && size > 0) addToList(price, size, command);
                else asks.remove(price);
            }
            default -> System.out.println(ERROR_MESSAGE + " u,<price>,<size>,<command>");
        }
    }

    private void addToList(int price, int size, String command) {
        if ("bid".equals(command)) {
            if (bids.containsKey(price)) bids.put(price, bids.get(price) + size);
            else bids.put(price, size);
        } else if ("ask".equals(command)) {
            if (asks.containsKey(price)) asks.put(price, asks.get(price) + size);
            else asks.put(price, size);
        }
    }
}
