package com.nexo.marketdata.services;

import java.util.List;

public interface KrakenWebSocketService {

    /**
     * Open a session to Kraken Websocket public channel
     *
     * @return {@code true} if the connection is opened successfully, {@code false} otherwise
     */
    boolean connect();

    /**
     * Subscribe for a stream of Kraken Websocket public channel order book updates
     *
     * @param pairs {@link List} of asset pairs
     * @return {@code true} if the subscription is established successfully, {@code false} otherwise
     */
    boolean subscribe(List<String> pairs);

    /**
     * Close an opened session to Kraken Websocket public channel
     *
     * @return {@code true} if the connection is closed successfully, {@code false} otherwise
     */
    boolean disconnect();
}
