package com.book;

import java.util.Collections;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class OrderBook {

    private static final TreeMap<Integer, Integer> asks = new TreeMap<>();
    private static final TreeMap<Integer, Integer> bids = new TreeMap<>();

    private OrderBook() {}

    static void getOrder(Supplier<String> source, Consumer<String> drain) {
        while (true) {
            String order = source.get();
            if (Objects.isNull(order)) break;
            String[] myOrder = order.split(",");
            String commandType = myOrder[0];
            switch (commandType) {
                case "q" -> drain.accept(queryProcess(myOrder));
                case "u" -> updateProcess(myOrder);
                case "o" -> orderProcess(myOrder);
            }
        }
    }

    private static void updateProcess(String[] myOrder) {
        String command = myOrder[3];
        int price = Integer.parseInt(myOrder[1]);
        int size = Integer.parseInt(myOrder[2]);
        orderCheck(price, size, command);
    }

    private static void orderProcess(String[] myOrder) {
        String command = myOrder[1];
        int size = Integer.parseInt(myOrder[2]);
        switch (command) {
            case "buy" -> buyItem(size);
            case "sell" -> sellItem(size);
        }
    }

    private static String queryProcess(String[] myOrder) {
        String command = myOrder[1];
        String result = "";
        switch (command) {
            case "best_bid" -> result = getBestBid();
            case "best_ask" -> result = getBestAsk();
            case "size" -> result = printSize(Integer.parseInt(myOrder[2]));
        }
        return result;
    }

    private static String getBestBid() {
        var best = bids.lastEntry();
        if (Objects.nonNull(best)) {
            return best.getKey() + "," + best.getValue();
        }
        return "";
    }

    private static String getBestAsk() {
        var best = asks.firstEntry();
        if (Objects.nonNull(best)) {
            return best.getKey() + "," + best.getValue();
        }
        return "";
    }

    private static String printSize(int price) {
        var size = bids.containsKey(price) ? bids.get(price) : asks.get(price);
        if (Objects.nonNull(size)) {
            return Integer.toString(size);
        }
        return "0";
    }

    private static void buyItem(int size) {
        while (size > 0) {
            var best = getBestAsk().split(",");
            var price = Integer.parseInt(best[0]);
            var value = Integer.parseInt(best[1]);
            size -= value;
            if (size == 0 || size > 0) {
                asks.remove(price);
            } else {
                asks.put(price, (size * -1));
            }
        }
    }

    private static void sellItem(int size) {
        while (size > 0) {
            var best = getBestBid().split(",");
            var price = Integer.parseInt(best[0]);
            var value = Integer.parseInt(best[1]);
            size -= value;
            if (size == 0 || size > 0) {
                bids.remove(price);
            } else {
                bids.put(price, (size * -1));
            }
        }
    }

    private static void orderCheck(int price, int size, String command) {
        if ("bid".equals(command)) {
            if (size == 0) {
                bids.remove(price);
            } else {
                bids.put(price, size);
            }
        } else {
            if (size == 0) {
                asks.remove(price);
            } else {
                asks.put(price, size);
            }
        }
    }
}
