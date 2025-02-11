package com.analyzer.stockAnalyzer.Service;

import com.analyzer.stockAnalyzer.Models.Instruments;
import com.analyzer.stockAnalyzer.Repo.InstrumentRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class InstrumentService {

    @Autowired
    private InstrumentRepo repository;

    public void saveInstrumentsFromFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Pre-read JSON array to get the count
            List<Instruments> instrumentsList = mapper.readValue(
                    new File(filePath), new TypeReference<>() {}
            );

            int totalCount ;
            instrumentsList.forEach(e-> {
                int count =0;
                Instruments saved = repository.save(e);
                if (saved.getInstrumentId()!=null){
                    System.out.println(count);
                   count++;
                }
            });

            System.out.println("Total records saved...");
        } catch (Exception e) {
            e.printStackTrace();
        }



       /* try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            MappingIterator<Instruments> iterator = mapper.readerFor(Instruments.class).readValues(reader);

            List<Instruments> batch = new ArrayList<>();
            int totalRecords = 0;
            while (iterator.hasNext()) {
                batch.add(iterator.next());
                totalRecords++;
                Instruments stock = iterator.next();
                repository.save(stock);
            }

            if (!batch.isEmpty()) {
                repository.saveAll(batch);
                System.out.println("Saved final batch. Total saved: " + totalRecords);
            }

            System.out.println("All records processed. Total records: " + totalRecords);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read or parse the file", e);
        }*/
        System.out.println("Ended...");
    }


    public Long getTokenByName(String name) {
        System.out.println("getting token for : " + name);
        Optional<List<Instruments>> symbols = null;
        Long instrumentToken = null;
        try {
           symbols  = repository.findByTradingSymbolContaining(name);

        List<Instruments> instrumentsList = symbols.get();
        if (instrumentsList.size() > 1){
            System.out.println(instrumentsList);
            for (Instruments instrument: instrumentsList){
                if (instrument.getTradingSymbol().equals(name)){
                instrumentToken = instrument.getInstrumentToken();
                }
            }
        }else {
            Instruments stockDetail = instrumentsList.get(0);
            instrumentToken = stockDetail.getInstrumentToken();
        }


//
        System.out.println("StockName " + name + " | Token " + instrumentToken);
        }catch (Exception e){
            e.getMessage();
        }
        return instrumentToken;
    }


}
