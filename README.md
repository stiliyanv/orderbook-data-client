# orderbook-data-client

Spring boot REST application for reading and printing (on the console) market data for specified asset pairs from Kraken exchange by using [Kraken Websockets API](https://docs.kraken.com/websockets/).

> The application is build with and requires Java 11

- [Run](#run)
- [Endpoints](#endpoints)
- [Examples](#examples)
  - [Open a session](#open-a-session)
  - [Subscribe](#subscribe)
  - [Trying to subscribe without opening a session](#trying-to-subscribe-without-opening-a-session)
  - [Trying to subscribe using incorrect asset pairs](#trying-to-subscribe-using-incorrect-asset-pairs)
  - [Close a session](#close-a-session)

## Run

Clone/download the source code and run from the root directory
```
mvn spring-boot:run
```

The application will run on `http://localhost:8080`

## Endpoints

| POST Request           | Parameters  | Details |
| ---------------------- | ----------- | ------- |
| `/kraken-ws/open`      | -           | Open a session to Kraken Websocket public channel |
| `/kraken-ws/subscribe` | List of Asset Pairs (example: `XBT/USD,ETH/USD`) | Subscribe for a stream of Kraken Websocket public channel order book updates and print the result of this subscription on the console |
| `/kraken-ws/close`     | -           | Close an opened session to Kraken Websocket public channel |

## Examples

### Open a session
```
# POST Request
http://localhost:8080/kraken-ws/open

# Response
{
    "connected": true
}

# Application logs
...
2022-12-29 00:31:58.339 DEBUG 12241 --- [nio-8080-exec-2] c.n.m.services.KrakenWebSocketHandler    : Kraken Websocket public channel (wss://beta-ws.kraken.com/) session opened
...
```

### Subscribe
```
# POST Request
http://localhost:8080/kraken-ws/subscribe?pairs=XBT/USD,ETH/USD

# Response
{
    "subscribed": true
}

# Application logs
...
2022-12-29 00:56:00.765 DEBUG 15366 --- [nio-8080-exec-4] c.n.m.s.KrakenWebSocketServiceImpl       : Subscribing to pairs: [XBT/USD, ETH/USD]
...
2022-12-29 00:48:35.879 DEBUG 12241 --- [ient-SecureIO-2] c.n.m.services.KrakenWebSocketHandler    : Text message received [560,{"as":[["1185.36000","266.13742360","1672267715.002363"],["1185.37000","1.25000000","1672267661.625803"],["1185.39000","0.01687564","1672267221.736063"],["1185.41000","2.05426511","1672267715.685005"],["1185.42000","25.30971346","1672267715.685468"],["1185.43000","2.10886821","1672267700.022327"],["1185.45000","0.01687493","1672267702.584682"],["1185.47000","12.05445608","1672267703.123344"],["1185.48000","0.77798779","1672267691.161173"],["1185.51000","63.26425360","1672267663.590031"]],"bs":[["1185.35000","16.44150393","1672267714.711156"],["1185.29000","5.00000000","1672267702.903075"],["1185.10000","0.67376003","1672267714.269392"],["1185.09000","1.24346238","1672267697.604726"],["1185.07000","0.01687664","1672267708.601346"],["1185.06000","8.68436312","1672267664.767898"],["1185.01000","7.18508143","1672267714.133622"],["1185.00000","2.01687763","1672267652.564827"],["1184.99000","0.02851711","1672267714.763951"],["1184.98000","63.29169047","1672267662.080712"]]},"book-10","ETH/USD"]
2022-12-29 00:48:35.882 DEBUG 12241 --- [ient-SecureIO-2] c.n.m.services.KrakenWebSocketHandler    : Order book update detected
...
2022-12-29 00:48:35.995 DEBUG 12241 --- [ient-SecureIO-1] c.n.m.services.KrakenWebSocketHandler    : Text message received [336,{"b":[["16513.90000","1.50792329","1672267715.948165"]],"c":"1440852813"},"book-10","XBT/USD"]
2022-12-29 00:48:35.997 DEBUG 12241 --- [ient-SecureIO-1] c.n.m.services.KrakenWebSocketHandler    : Order book update detected
...
2022-12-29 00:48:36.831 DEBUG 12241 --- [ient-SecureIO-1] c.n.m.services.KrakenWebSocketHandler    : Text message received [336,{"a":[["16514.80000","0.00000000","1672267716.784761"],["16520.90000","0.91010000","1672267713.296832","r"]],"c":"1926571405"},"book-10","XBT/USD"]
2022-12-29 00:48:36.832 DEBUG 12241 --- [ient-SecureIO-1] c.n.m.services.KrakenWebSocketHandler    : Order book update detected
...

# Console
...
<------------------------------------->
asks: 
 [ 1185.510000, 63.264254 ],
 [ 1185.480000, 0.777988 ],
 [ 1185.470000, 12.054456 ],
 [ 1185.450000, 0.016875 ],
 [ 1185.430000, 2.108868 ],
 [ 1185.420000, 25.309713 ],
 [ 1185.410000, 2.054265 ],
 [ 1185.390000, 0.016876 ],
 [ 1185.370000, 1.250000 ],
 [ 1185.360000, 266.137424 ]
best ask:  [ 1185.360000, 266.137424 ]
bids: 
 [ 1185.350000, 16.441504 ],
 [ 1185.290000, 5.000000 ],
 [ 1185.100000, 0.673760 ],
 [ 1185.090000, 1.243462 ],
 [ 1185.070000, 0.016877 ],
 [ 1185.060000, 8.684363 ],
 [ 1185.010000, 7.185081 ],
 [ 1185.000000, 2.016878 ],
 [ 1184.990000, 0.028517 ],
 [ 1184.980000, 63.291690 ]
best bid:  [ 1185.350000, 16.441504 ]
pair: "ETH/USD"
>-------------------------------------<
...
<------------------------------------->
bids: 
 [ 16513.900000, 1.507923 ]
best bid:  [ 16513.900000, 1.507923 ]
pair: "XBT/USD"
>-------------------------------------<
...
<------------------------------------->
asks: 
 [ 16520.900000, 0.910100 ],
 [ 16514.800000, 0.000000 ]
best ask:  [ 16514.800000, 0.000000 ]
pair: "XBT/USD"
>-------------------------------------<
...
```

### Trying to subscribe without opening a session
```
# POST Request
http://localhost:8080/kraken-ws/subscribe?pairs=XBT/USD,ETH/USD

# Response
{
    "subscribed": false
}

# Application logs
...
2022-12-29 00:37:46.267  WARN 12241 --- [nio-8080-exec-9] c.n.m.s.KrakenWebSocketServiceImpl       : Kraken Websocket public channel (wss://beta-ws.kraken.com/) session is not open. Open one before trying to subscribe.
...
```

### Trying to subscribe using incorrect asset pairs
```
# POST Request
http://localhost:8080/kraken-ws/subscribe?pairs=XXXXXXX,AAA/AAAA

# Response
{
    "subscribed": false
}

# Application logs
...
2022-12-29 00:46:58.250 DEBUG 12241 --- [nio-8080-exec-4] c.n.m.s.KrakenWebSocketServiceImpl       : Subscribing to pairs: [XXXXXXX, AAA/AAAA]
...
2022-12-29 00:46:58.445 ERROR 12241 --- [ient-SecureIO-1] c.n.m.services.KrakenWebSocketHandler    : Subscription status: error, Error message: Currency pair not in ISO 4217-A3 format XXXXXXX
...
2022-12-29 00:46:58.448 ERROR 12241 --- [ient-SecureIO-1] c.n.m.services.KrakenWebSocketHandler    : Subscription status: error, Error message: Currency pair not supported AAA/AAAA
...
```

### Close a session
```
# POST Request
http://localhost:8080/kraken-ws/close

# Response
{
    "disconnected": true
}

# Application logs
...
2022-12-29 00:35:58.784 DEBUG 12241 --- [nio-8080-exec-7] c.n.m.services.KrakenWebSocketHandler    : Kraken Websocket public channel (wss://beta-ws.kraken.com/) session closed
...
```