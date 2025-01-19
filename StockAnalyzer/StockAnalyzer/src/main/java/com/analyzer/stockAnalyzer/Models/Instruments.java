package com.analyzer.stockAnalyzer.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Instruments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long instrumentId;
    @JsonProperty("instrument_token")
    private Long instrumentToken;

    @JsonProperty("exchange_token")
    private String exchangeToken;

    @JsonProperty("tradingsymbol")
    private String tradingSymbol;

    @JsonProperty("name")
    private String name;

    @JsonProperty("exchange")
    private String exchange;

    // Getters and setters

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }
    public Long getInstrumentToken() {
        return instrumentToken;
    }

    public void setInstrumentToken(Long instrumentToken) {
        this.instrumentToken = instrumentToken;
    }

    public String getExchangeToken() {
        return exchangeToken;
    }

    public void setExchangeToken(String exchangeToken) {
        this.exchangeToken = exchangeToken;
    }

    public String getTradingSymbol() {
        return tradingSymbol;
    }

    public void setTradingSymbol(String tradingSymbol) {
        this.tradingSymbol = tradingSymbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "instrumentId=" + instrumentId +
                ", instrumentToken=" + instrumentToken +
                ", exchangeToken='" + exchangeToken + '\'' +
                ", tradingSymbol='" + tradingSymbol + '\'' +
                ", name='" + name + '\'' +
                ", exchange='" + exchange + '\'' +
                '}';
    }
}

