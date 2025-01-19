package com.analyzer.stockAnalyzer.Controller;

import com.analyzer.stockAnalyzer.Models.Instruments;
import com.analyzer.stockAnalyzer.Service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/instruments/loading")
public class InstrumentController {
    @Autowired
    private InstrumentService instrumentService;

    @GetMapping("/start")
    public String startDataFetching() {
        System.out.println("Started....");
        String filePath = "nse_instruments.json";
        instrumentService.saveInstrumentsFromFile(filePath);
        return "Instruments loaded successfully!";
    }

    @GetMapping("/name/{name}")
    public String getTokenForStock(@PathVariable("name") String name){
        Long token = instrumentService.getTokenByName(name);
        return "Token : "+token;

    }
}
