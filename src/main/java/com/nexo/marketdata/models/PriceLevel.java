package com.nexo.marketdata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"price", "volume", "timestamp"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class PriceLevel {
    public Double price;
    public Double volume;
    public Double timestamp;

    @Override
    public String toString() {
        return String.format(" [ %f, %f ]", price, volume);
    }
}
