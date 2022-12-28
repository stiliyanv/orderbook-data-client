package com.nexo.marketdata.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderBookUpdate {

    public List<PriceLevel> as;
    public List<PriceLevel> bs;
    public List<PriceLevel> a;
    public List<PriceLevel> b;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        // asks and best ask
        if (as != null || a != null) {
            List<PriceLevel> asks = new ArrayList<>(as != null ? as : a);
            asks.sort(Collections.reverseOrder(Comparator.comparing(PriceLevel::getPrice)));

            stringBuilder
                    .append("asks: \n")
                    .append(asks.stream()
                            .map(PriceLevel::toString)
                            .collect(Collectors.joining(",\n")))
                    .append("\nbest ask: ")
                    .append(asks.get(asks.size() - 1))
                    .append("\n");
        }

        // bids and best bid
        if (bs != null || b != null) {
            List<PriceLevel> bids = new ArrayList<>(bs != null ? bs : b);
            bids.sort(Collections.reverseOrder(Comparator.comparing(PriceLevel::getPrice)));

            stringBuilder
                    .append("bids: \n")
                    .append(bids.stream()
                            .map(PriceLevel::toString)
                            .collect(Collectors.joining(",\n")))
                    .append("\nbest bid: ")
                    .append(bids.get(0));
        }

        return stringBuilder.toString();
    }
}
