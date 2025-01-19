package com.analyzer.stockAnalyzer.Models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class HistoricalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @OrderColumn(name = "sequence")
    private Long token;

    @OrderColumn(name = "sequence")
    private String name;

    @OrderColumn(name = "sequence")
    private double open;

    @OrderColumn(name = "sequence")
    private double high;

    @OrderColumn(name = "sequence")
    private double low;

    @OrderColumn(name = "sequence")
    private double close;

    @OrderColumn(name = "sequence")
    private Long volume;

    @OrderColumn(name = "sequence")
    private LocalDate date;

    @OrderColumn(name = "sequence")
    private LocalTime time;

    @OrderColumn(name = "sequence")
    private double fifteenMin2PMOpen;

    @OrderColumn(name = "sequence")
    private LocalTime fifteenMin2PMTime;

    @OrderColumn(name = "sequence")
    private double fifteenMin3PMOpen;

    @OrderColumn(name = "sequence")
    private LocalTime fifteenMin3PMTime;

    @OrderColumn(name = "sequence")
    private double ndFifteenMin915AMHigh;

    @OrderColumn(name = "sequence")
    private LocalTime ndFifteenMin915AMHighTime;

    @OrderColumn(name = "sequence")
    private double ndFifteenMin915AMOpen;

    @OrderColumn(name = "sequence")
    private LocalTime ndFifteenMin915AMOpenTime;

    @OrderColumn(name = "sequence")
    private double ndFifteenMin915AMClose;

    @OrderColumn(name = "sequence")
    private LocalTime ndFifteenMin915AMCloseTime;

    @OrderColumn(name = "sequence")
    private double pdOpen;

    @OrderColumn(name = "sequence")
    private double pdHigh;

    @OrderColumn(name = "sequence")
    private double pdLow;

    @OrderColumn(name = "sequence")
    private double pdClose;

    @OrderColumn(name = "sequence")
    private double pdVolume;

    @OrderColumn(name = "sequence")
    private LocalDate pdDate;

    //ND_15MIN_9-15AM_HIGH > (25% of (H-L))+H = (Yes/No)
    private String equationOne;

    //ND_15MIN_9-15AM_HIGH  -diff btwn-  H = in%
    private double equationTwo;

    //PD_CLOSE - Open = Diff in %
    private double equationThree;

    //PD_CLOSE - Open = (RED/Green)
    private String equationFour;

    //15MIN_2PM_Open - ND_15MIN_9-15AM_Open = diff
    private double equationFive;

    //15MIN_2PM_Open - ND_15MIN_9-15AM_Open = profit/loss
    private String equationSix;

    //15MIN_3PM_Open - ND_15MIN_9-15AM_Open = diff
    private double equationSeven;

    //15MIN_3PM_Open - ND_15MIN_9-15AM_Open = profit/loss
    private String equationEight;

    //15MIN_2PM_Open - ND_15MIN_9-15AM_close = diff
    private double equationNine;

    //15MIN_2PM_Open - ND_15MIN_9-15AM_Close = profit/loss
    private String equationTen;

    //15MIN_3PM_Open - ND_15MIN_9-15AM_Close = diff
    private double equationEleven;

    //15MIN_3PM_Open - ND_15MIN_9-15AM_Close = profit/loss
    private String equationTwelve;

    public String getEquationOne() {
        return equationOne;
    }

    public void setEquationOne(String equationOne) {
        this.equationOne = equationOne;
    }

    public double getEquationTwo() {
        return equationTwo;
    }

    public void setEquationTwo(double equationTwo) {
        this.equationTwo = equationTwo;
    }

    public double getEquationThree() {
        return equationThree;
    }

    public void setEquationThree(double equationThree) {
        this.equationThree = equationThree;
    }

    public String getEquationFour() {
        return equationFour;
    }

    public void setEquationFour(String equationFour) {
        this.equationFour = equationFour;
    }

    public double getEquationFive() {
        return equationFive;
    }

    public void setEquationFive(double equationFive) {
        this.equationFive = equationFive;
    }

    public String getEquationSix() {
        return equationSix;
    }

    public void setEquationSix(String equationSix) {
        this.equationSix = equationSix;
    }

    public double getEquationSeven() {
        return equationSeven;
    }

    public void setEquationSeven(double equationSeven) {
        this.equationSeven = equationSeven;
    }

    public String getEquationEight() {
        return equationEight;
    }

    public void setEquationEight(String equationEight) {
        this.equationEight = equationEight;
    }

    public double getEquationNine() {
        return equationNine;
    }

    public void setEquationNine(double equationNine) {
        this.equationNine = equationNine;
    }

    public String getEquationTen() {
        return equationTen;
    }

    public void setEquationTen(String equationTen) {
        this.equationTen = equationTen;
    }

    public double getEquationEleven() {
        return equationEleven;
    }

    public void setEquationEleven(double equationEleven) {
        this.equationEleven = equationEleven;
    }

    public String getEquationTwelve() {
        return equationTwelve;
    }

    public void setEquationTwelve(String equationTwelve) {
        this.equationTwelve = equationTwelve;
    }

    // Getters and Setters
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public double getFifteenMin2PMOpen() {
        return fifteenMin2PMOpen;
    }

    public void setFifteenMin2PMOpen(double fifteenMin2PMOpen) {
        this.fifteenMin2PMOpen = fifteenMin2PMOpen;
    }

    public LocalTime getFifteenMin2PMTime() {
        return fifteenMin2PMTime;
    }

    public void setFifteenMin2PMTime(LocalTime fifteenMin2PMTime) {
        this.fifteenMin2PMTime = fifteenMin2PMTime;
    }

    public double getFifteenMin3PMOpen() {
        return fifteenMin3PMOpen;
    }

    public void setFifteenMin3PMOpen(double fifteenMin3PMOpen) {
        this.fifteenMin3PMOpen = fifteenMin3PMOpen;
    }

    public LocalTime getFifteenMin3PMTime() {
        return fifteenMin3PMTime;
    }

    public void setFifteenMin3PMTime(LocalTime fifteenMin3PMTime) {
        this.fifteenMin3PMTime = fifteenMin3PMTime;
    }

    public double getNdFifteenMin915AMHigh() {
        return ndFifteenMin915AMHigh;
    }

    public void setNdFifteenMin915AMHigh(double ndFifteenMin915AMHigh) {
        this.ndFifteenMin915AMHigh = ndFifteenMin915AMHigh;
    }

    public LocalTime getNdFifteenMin915AMHighTime() {
        return ndFifteenMin915AMHighTime;
    }

    public void setNdFifteenMin915AMHighTime(LocalTime ndFifteenMin915AMHighTime) {
        this.ndFifteenMin915AMHighTime = ndFifteenMin915AMHighTime;
    }

    public double getNdFifteenMin915AMOpen() {
        return ndFifteenMin915AMOpen;
    }

    public void setNdFifteenMin915AMOpen(double ndFifteenMin915AMOpen) {
        this.ndFifteenMin915AMOpen = ndFifteenMin915AMOpen;
    }

    public LocalTime getNdFifteenMin915AMOpenTime() {
        return ndFifteenMin915AMOpenTime;
    }

    public void setNdFifteenMin915AMOpenTime(LocalTime ndFifteenMin915AMOpenTime) {
        this.ndFifteenMin915AMOpenTime = ndFifteenMin915AMOpenTime;
    }

    public double getNdFifteenMin915AMClose() {
        return ndFifteenMin915AMClose;
    }

    public void setNdFifteenMin915AMClose(double ndFifteenMin915AMClose) {
        this.ndFifteenMin915AMClose = ndFifteenMin915AMClose;
    }

    public LocalTime getNdFifteenMin915AMCloseTime() {
        return ndFifteenMin915AMCloseTime;
    }

    public void setNdFifteenMin915AMCloseTime(LocalTime ndFifteenMin915AMCloseTime) {
        this.ndFifteenMin915AMCloseTime = ndFifteenMin915AMCloseTime;
    }

    public double getPdOpen() {
        return pdOpen;
    }

    public void setPdOpen(double pdOpen) {
        this.pdOpen = pdOpen;
    }

    public double getPdHigh() {
        return pdHigh;
    }

    public void setPdHigh(double pdHigh) {
        this.pdHigh = pdHigh;
    }

    public double getPdLow() {
        return pdLow;
    }

    public void setPdLow(double pdLow) {
        this.pdLow = pdLow;
    }

    public double getPdClose() {
        return pdClose;
    }

    public void setPdClose(double pdClose) {
        this.pdClose = pdClose;
    }

    public double getPdVolume() {
        return pdVolume;
    }

    public void setPdVolume(double pdVolume) {
        this.pdVolume = pdVolume;
    }

    public LocalDate getPdDate() {
        return pdDate;
    }

    public void setPdDate(LocalDate pdDate) {
        this.pdDate = pdDate;
    }

    @Override
    public String toString() {
        return "HistoricalData{" +
                "recordId=" + recordId +
                ", token=" + token +
                ", name='" + name + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", date=" + date +
                ", time=" + time +
                ", fifteenMin2PMOpen=" + fifteenMin2PMOpen +
                ", fifteenMin2PMTime=" + fifteenMin2PMTime +
                ", fifteenMin3PMOpen=" + fifteenMin3PMOpen +
                ", fifteenMin3PMTime=" + fifteenMin3PMTime +
                ", ndFifteenMin915AMHigh=" + ndFifteenMin915AMHigh +
                ", ndFifteenMin915AMHighTime=" + ndFifteenMin915AMHighTime +
                ", ndFifteenMin915AMOpen=" + ndFifteenMin915AMOpen +
                ", ndFifteenMin915AMOpenTime=" + ndFifteenMin915AMOpenTime +
                ", ndFifteenMin915AMClose=" + ndFifteenMin915AMClose +
                ", ndFifteenMin915AMCloseTime=" + ndFifteenMin915AMCloseTime +
                ", pdOpen=" + pdOpen +
                ", pdHigh=" + pdHigh +
                ", pdLow=" + pdLow +
                ", pdClose=" + pdClose +
                ", pdVolume=" + pdVolume +
                ", pdDate=" + pdDate +
                ", equationOne='" + equationOne + '\'' +
                ", equationTwo=" + equationTwo +
                ", equationThree=" + equationThree +
                ", equationFour='" + equationFour + '\'' +
                ", equationFive=" + equationFive +
                ", equationSix='" + equationSix + '\'' +
                ", equationSeven=" + equationSeven +
                ", equationEight='" + equationEight + '\'' +
                ", equationNine=" + equationNine +
                ", equationTen='" + equationTen + '\'' +
                ", equationEleven=" + equationEleven +
                ", equationTwelve='" + equationTwelve + '\'' +
                '}';
    }
}

