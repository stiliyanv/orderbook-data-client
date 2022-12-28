package com.nexo.marketdata.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexo.marketdata.models.OrderBookUpdate;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;

import static com.nexo.marketdata.utility.KrakenConstants.ERROR_MESSAGE;
import static com.nexo.marketdata.utility.KrakenConstants.EVENT;
import static com.nexo.marketdata.utility.KrakenConstants.EVENT_SUBSCRIPTION_STATUS;
import static com.nexo.marketdata.utility.KrakenConstants.KRAKEN_WEBSOCKET_PUBLIC_URL;
import static com.nexo.marketdata.utility.KrakenConstants.STATUS;
import static com.nexo.marketdata.utility.KrakenConstants.STATUS_ERROR;

@Slf4j
@NoArgsConstructor
public class KrakenWebSocketHandler extends TextWebSocketHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private WebSocketSession webSocketSession;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        log.debug("Text message received {}", payload);
        JsonNode rootNode;

        try {
            rootNode = objectMapper.readTree(payload);
        } catch (JsonProcessingException e) {
            log.error("Exception while parsing message payload {}", payload, e);
            return;
        }

        if (rootNode.findValue(EVENT) != null) {
            handleEvents(rootNode);
        } else {
            log.debug("Order book update detected");
            consolePrintOrderBookUpdate(rootNode);
        }
    }

    public boolean subscribe(String payload) {
        try {
            webSocketSession.sendMessage(new TextMessage(payload));
        } catch (IOException e) {
            log.error("Exception while requesting subscription for Kraken Websocket public channel ({})",
                    KRAKEN_WEBSOCKET_PUBLIC_URL, e);
            return false;
        }
        return true;
    }

    public boolean isSessionOpen() {
        return webSocketSession != null && webSocketSession.isOpen();
    }

    public boolean closeSession() {
        try {
            webSocketSession.close();
            log.debug("Kraken Websocket public channel ({}) session closed", KRAKEN_WEBSOCKET_PUBLIC_URL);
            return true;
        } catch (IOException e) {
            log.error("Exception while trying to close Kraken Websockets session", e);
            return false;
        }
    }

    public boolean openSession() {
        try {
            webSocketSession = new StandardWebSocketClient().doHandshake(
                    this,
                    new WebSocketHttpHeaders(),
                    URI.create(KRAKEN_WEBSOCKET_PUBLIC_URL)).get();
            log.debug("Kraken Websocket public channel ({}) session opened", KRAKEN_WEBSOCKET_PUBLIC_URL);
            return true;
        } catch (Exception e) {
            log.error("Exception while trying to open a session to Kraken Websockets public channel ({})",
                    KRAKEN_WEBSOCKET_PUBLIC_URL, e);
            return false;
        }
    }

    private void handleEvents(JsonNode rootNode) {
        String event = rootNode.findValue(EVENT).asText();
        log.debug("Event detected: {}", event);

        if (event.equals(EVENT_SUBSCRIPTION_STATUS) &&
                rootNode.findValue(STATUS).asText().equals(STATUS_ERROR)) {
            log.error("Subscription status: {}, Error message: {}",
                    rootNode.findValue(STATUS).asText(),
                    rootNode.findValue(ERROR_MESSAGE).asText());
        } else {
            log.debug("No further actions for event of type {}", event);
        }
    }

    private void consolePrintOrderBookUpdate(JsonNode rootNode) {
        System.out.println("<------------------------------------->");
        System.out.println(objectMapper.convertValue(rootNode.get(1), OrderBookUpdate.class));
        System.out.printf("pair: %s%n", rootNode.get(3));
        System.out.println(">-------------------------------------<");
    }
}