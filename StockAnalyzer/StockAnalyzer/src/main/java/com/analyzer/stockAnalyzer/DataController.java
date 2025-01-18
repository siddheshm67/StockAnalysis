package com.analyzer.stockAnalyzer;

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
    public String startDataFetching() {
        System.out.println("Started....");
        int token = 2953217;
        String stockName = "TCS";
        LocalDate localDate = LocalDate.of(2025, 1, 13);
        dataService.startStockAnalysis(token,stockName,localDate);
        return "Done....";
    }


}
