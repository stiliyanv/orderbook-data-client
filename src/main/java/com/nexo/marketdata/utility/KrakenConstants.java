package com.nexo.marketdata.utility;

/**
 * Class that contains constants related to <a href="https://docs.kraken.com/websockets/">Kraken Websockets API</a>
 */
public final class KrakenConstants {
    public static final String KRAKEN_WEBSOCKET_PUBLIC_URL = "wss://beta-ws.kraken.com/";

    // JSON response properties and values
    public static final String EVENT = "event";
    public static final String STATUS = "status";
    public static final String PAIR = "pair";
    public static final String SUBSCRIPTION = "subscription";
    public static final String NAME = "name";
    public static final String NAME_BOOK = "book";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String STATUS_ERROR = "error";

    // event types
    public static final String EVENT_SUBSCRIPTION_STATUS = "subscriptionStatus";
    public static final String EVENT_SUBSCRIBE = "subscribe";

    private KrakenConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
