package com.nexo.marketdata.controllers;

import com.nexo.marketdata.services.KrakenWebSocketService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *  Controller that exposes rest endpoints that enable the user
 *  to perform the following operations on Kraken Websocket public channel:
 *  - open a session
 *  - close a session
 *  - subscribe to a topic
 */
@RestController
@RequestMapping("kraken-ws")
public class KrakenWebSocketsApiController {

    private final KrakenWebSocketService krakenWebSocketService;

    public KrakenWebSocketsApiController(KrakenWebSocketService krakenWebSocketService) {
        this.krakenWebSocketService = krakenWebSocketService;
    }

    /**
     * Open a session to Kraken Websocket public channel
     *
     * @return response whether the session was opened or not
     */
    @PostMapping(value = "/open")
    @ResponseBody()
    public Map<String, Boolean> connect() {
        return Collections.singletonMap("connected", krakenWebSocketService.connect());
    }

    /**
     * Subscribe for a stream of Kraken Websocket public channel order book updates
     *
     * @return response whether the subscription was successful or not
     */
    @PostMapping("/subscribe")
    public Map<String, Boolean> subscribe(
            @RequestParam(value = "pairs", required = false) List<String> pairs) {
        return Collections.singletonMap("subscribed", krakenWebSocketService.subscribe(pairs));
    }

    /**
     * Close an opened session to Kraken Websocket public channel
     *
     * @return response whether the session was closed or not
     */
    @PostMapping("/close")
    public Map<String, Boolean> disconnect() {
        return Collections.singletonMap("disconnected", krakenWebSocketService.disconnect());
    }
}
