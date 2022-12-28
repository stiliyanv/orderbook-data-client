package com.nexo.marketdata.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nexo.marketdata.constants.KrakenConstants.EVENT;
import static com.nexo.marketdata.constants.KrakenConstants.EVENT_SUBSCRIBE;
import static com.nexo.marketdata.constants.KrakenConstants.KRAKEN_WEBSOCKET_PUBLIC_URL;
import static com.nexo.marketdata.constants.KrakenConstants.NAME;
import static com.nexo.marketdata.constants.KrakenConstants.NAME_BOOK;
import static com.nexo.marketdata.constants.KrakenConstants.PAIR;
import static com.nexo.marketdata.constants.KrakenConstants.SUBSCRIPTION;

@Slf4j
@Service
public class KrakenWebSocketServiceImpl implements KrakenWebSocketService {

    private final KrakenWebSocketHandler krakenWebSocketHandler;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public KrakenWebSocketServiceImpl() {
        krakenWebSocketHandler = new KrakenWebSocketHandler();
    }

    @Override
    public boolean connect() {
        if (!krakenWebSocketHandler.isSessionOpen()) {
            return krakenWebSocketHandler.openSession();
        }
        log.debug("Kraken Websocket public channel ({}) session already opened", KRAKEN_WEBSOCKET_PUBLIC_URL);
        return true;
    }

    @Override
    public boolean subscribe(List<String> pairs) {
        if (!krakenWebSocketHandler.isSessionOpen()) {
            log.warn("Kraken Websocket public channel ({}) session is not open. Open one before trying to subscribe.",
                    KRAKEN_WEBSOCKET_PUBLIC_URL);
            return false;
        }

        log.debug("Subscribing to pairs: {}", pairs.toString());
        ObjectNode rootNode = objectMapper.createObjectNode();

        // event
        rootNode.put(EVENT, EVENT_SUBSCRIBE);

        // pair
        ArrayNode pairsNode = objectMapper.valueToTree(pairs);
        rootNode.putArray(PAIR).addAll(pairsNode);

        // subscription
        ObjectNode subscriptionNode = rootNode.putObject(SUBSCRIPTION);
        subscriptionNode.put(NAME, NAME_BOOK);

        return krakenWebSocketHandler.subscribe(rootNode.toString());
    }

    @Override
    public boolean disconnect() {
        if (krakenWebSocketHandler.isSessionOpen()) {
            return krakenWebSocketHandler.closeSession();
        }
        log.debug("Kraken Websocket public channel ({}) session not opened or already closed",
                KRAKEN_WEBSOCKET_PUBLIC_URL);
        return true;
    }
}
