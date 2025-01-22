package com.analyzer.stockAnalyzer.Controller;

import com.analyzer.stockAnalyzer.Service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/data/fetching")
public class DataController {

    @Autowired
    private DataService dataService;

    @GetMapping("/start")
    public String startDataFetching() throws IOException {
        System.out.println("Started....");
        int token = 2953217;
        String stockName = "TCS";
        LocalDate localDate = LocalDate.of(2025, 1, 13);
        dataService.startStockAnalysis(token,stockName,localDate);
        return "Done....";
    }

    @GetMapping("/read")
    public String readInputFile() {
        System.out.println("Started....");
        dataService.readInputFile("Backtest High volume 10x, Technical Analysis Scanner.csv",1000,163);
        System.out.println(".........Done.......");
        return "Done....";
    }

    @GetMapping("/sma")
    public String getVolumeSma() throws IOException {
        System.out.println("Started....");
        dataService.getPreviousTenDaysVolumeSma("",0,LocalDate.of(2025,01,15));
        System.out.println(".........Done.......");
        return "Done....";
    }




}
