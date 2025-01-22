/*
package com.analyzer.stockAnalyzer.Service;

import com.analyzer.stockAnalyzer.Models.HistoricalData;
import com.analyzer.stockAnalyzer.Repo.DataRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceTest {

    @Autowired
    private DataRepo dataRepo;


    @Autowired
    private InstrumentService instrumentService;


    public void startStockAnalysis(int tokenParam, String name, LocalDate dateParam) {

        HistoricalData historicalData = new HistoricalData();
        String enctoken = "nlCGwM41iIY5nU8rOov4U9MxZAB+OUcoRFiUyUfISXhpe89FNY70L31fnQi+C9ctXXjrmLV5/0eLT77VqsJPAqFXlC6e/Z00bv7lywMzvy/VgnFy8jOolg==";
        int token = tokenParam;
        String stockName = name;

        List<String> timeFrameList = Arrays.asList("day", "15minute","minute");
        for (String currentTimeFrame : timeFrameList) {

            String timeframe = currentTimeFrame;
            // Main date
            LocalDate eventDate = dateParam;

            LocalDate startDate = eventDate.minusDays(1);
            LocalDate endDate = eventDate.plusDays(1);

            while (!startDate.isAfter(endDate)) {
                System.out.println("Checking for Date: " + startDate);

                boolean dataFound = false;
                int retryCount = 0;

                // Retry loop to adjust dates
                while (retryCount < 10 && !dataFound) { // Limit retries to 5

                    String url = String.format("https://kite.zerodha.com/oms/instruments/historical/%d/%s", token, timeframe);
                    Map<String, String> params = new HashMap<>();
                    params.put("oi", "1");
                    params.put("from", startDate.toString());
                    params.put("to", startDate.toString());

                    String authorizationHeader = "enctoken " + enctoken;

                    try {
                        StringBuilder urlWithParams = new StringBuilder(url);
                        urlWithParams.append("?");
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            urlWithParams.append(entry.getKey())
                                    .append("=")
                                    .append(entry.getValue())
                                    .append("&");
                        }
                        urlWithParams.deleteCharAt(urlWithParams.length() - 1);

                        URL requestUrl = new URL(urlWithParams.toString());
                        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setRequestProperty("Authorization", authorizationHeader);

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String inputLine;

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();

                            ObjectMapper objectMapper = new ObjectMapper();
                            JsonNode rootNode = objectMapper.readTree(response.toString());
                            JsonNode candlesNode = rootNode.path("data").path("candles");

                            if (!candlesNode.isEmpty()) {
                                for (JsonNode candle : candlesNode) {
                                    String dateTime = candle.get(0).asText();
                                    String date = dateTime.split("T")[0];
                                    String localTime = dateTime.split("T")[1];

                                    // Parse the string as OffsetTime
                                    String timeWithoutOffset = localTime.split("\\+")[0];

                                    // Parse the time string into a LocalTime object
                                    LocalTime time = LocalTime.parse(timeWithoutOffset);


                                    double open = candle.get(1).asDouble();
                                    double high = candle.get(2).asDouble();
                                    double low = candle.get(3).asDouble();
                                    double close = candle.get(4).asDouble();
                                    int volume = candle.get(5).asInt();


                                    if (timeframe.equals("day") && LocalDate.parse(date).equals(eventDate)) {

                                        historicalData.setToken((long) token);
                                        historicalData.setName(stockName);
                                        historicalData.setOpen(open);
                                        historicalData.setHigh(high);
                                        historicalData.setLow(low);
                                        historicalData.setClose(close);
                                        historicalData.setVolume((long) volume);
                                        historicalData.setDate(LocalDate.parse(date));
                                        historicalData.setTime(time);
                                    } else if (timeframe.equals("15minute") && LocalDate.parse(date).equals(eventDate) && time.equals(LocalTime.of(14, 00))){
                                        historicalData.setFifteenMin2PMOpen(open);
                                        historicalData.setFifteenMin2PMTime(time);
                                    }else if (timeframe.equals("15minute") && LocalDate.parse(date).equals(eventDate) && time.equals(LocalTime.of(15, 00))){
                                        historicalData.setFifteenMin3PMOpen(open);
                                        historicalData.setFifteenMin3PMTime(time);
                                    } else if (LocalDate.parse(date).isAfter(eventDate) && timeframe.equals("15minute") && time.equals(LocalTime.of(9, 15))) {

                                        //ND_15MIN_9-15AM_HIGH
                                        historicalData.setNdFifteenMin915AMHigh(high);
                                        // ND_15MIN_9-15AM_High_Time
                                        historicalData.setNdFifteenMin915AMHighTime(time);
                                        // ND_15MIN_9-15AM_Open
                                        historicalData.setNdFifteenMin915AMOpen(open);
                                        // ND_15MIN_9-15AM_Open_Time
                                        historicalData.setNdFifteenMin915AMOpenTime(time);
                                        // ND_15MIN_9-15AM_Close
                                        historicalData.setNdFifteenMin915AMClose(close);
                                        // ND_15MIN_9-15AM_Close_Time
                                        historicalData.setNdFifteenMin915AMCloseTime(time);
                                    } else if (LocalDate.parse(date).isBefore(eventDate) && timeframe.equals("day") ) {

                                        //PD_Open
                                        historicalData.setPdOpen(open);
                                        // PD_High
                                        historicalData.setPdHigh(high);
                                        // PD_Low
                                        historicalData.setPdLow(low);
                                        // PD_Close
                                        historicalData.setPdClose(close);
                                        // PD_Volume
                                        historicalData.setPdVolume(volume);
                                        // PD_Date
                                        historicalData.setPdDate(LocalDate.parse(date));
                                    }
                                }
                                dataFound = true; // Exit the retry loop as data has been found
                            } else {
                                System.out.println("No data available for date: " + startDate);
                            }
                        } else {
                            System.out.println("GET request failed. Response Code: " + responseCode);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Adjust date for next retry
                    retryCount++;
                    if (!dataFound) {
                        long diff = ChronoUnit.DAYS.between(startDate, eventDate);
                        if (diff > 0) {
                            startDate = startDate.minusDays(1);
                        } else {
                            startDate = startDate.plusDays(1);
                        }
                    }
                }

                if (!dataFound) {
                    System.out.println("No data found after retries for date: " + startDate);
                    break; // Exit the outer loop if no data is found after retries
                }

                // Move to the next date
                startDate = startDate.plusDays(retryCount);
            }

        }

        HistoricalData analyzedData = AnalyzeStocks(historicalData);
        HistoricalData saved = null;
        try {
            saved = dataRepo.save(analyzedData);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (saved != null){
          //  writeToExcelFile(saved);
        }else {
            System.out.println("null!!!!");
        }
        System.out.println(saved);
    }

    private HistoricalData AnalyzeStocks(HistoricalData data){

        // double open, double high, double low, double close,
        //        double fifteenMin2PmOpen, double nextDay15Min9_15AmClose

        */
/*double riskRewardRatioTwoPm = calculateRiskRewardRatio(data.getOpen(),data.getHigh(),data.getLow(),data.getClose(),data.getFifteenMin2PMOpen(), data.getNdFifteenMin915AMClose());
        if (riskRewardRatioTwoPm > 0) {
            data.setTwoPmCalculatedRiskRewardRatio(Math.round(riskRewardRatioTwoPm * 100.0) / 100.0);
            data.setTwoPmRiskRewardRatioPorL("PROFIT");
        } else {
            data.setTwoPmCalculatedRiskRewardRatio(Math.round(riskRewardRatioTwoPm * 100.0) / 100.0);
            data.setTwoPmRiskRewardRatioPorL("LOSS");
        }


        double riskRewardRatioThreePm = calculateRiskRewardRatio(data.getOpen(),data.getHigh(),data.getLow(),data.getClose(),data.getFifteenMin3PMOpen(), data.getNdFifteenMin915AMClose());
        if (riskRewardRatioThreePm > 0) {
            data.setThreePmCalculatedRiskRewardRatio(Math.round(riskRewardRatioThreePm * 100.0) / 100.0);
            data.setThreePmRiskRewardRatioPorL("PROFIT");
        } else {
            data.setThreePmCalculatedRiskRewardRatio(Math.round(riskRewardRatioThreePm * 100.0) / 100.0);
            data.setThreePmRiskRewardRatioPorL("LOSS");
        }

        //next

        double a = calculateRiskRewardRatio(data.getOpen(),data.getHigh(),data.getLow(),data.getClose(),data.getFifteenMin2PMOpen(), data.getNdFifteenMin915AMOpen());
        data.setTwoPmToNineFifteenAmOpenCalculatedRiskRewardRatio(Math.round(a * 100.0) / 100.0);
        if (a > 0) {
            data.setTwoPmToNineFifteenAmOpenRiskRewardRatioPorL("PROFIT");
        } else {
            data.setTwoPmToNineFifteenAmOpenRiskRewardRatioPorL("LOSS");
        }


        double b = calculateRiskRewardRatio(data.getOpen(),data.getHigh(),data.getLow(),data.getClose(),data.getFifteenMin3PMOpen(), data.getNdFifteenMin915AMOpen());
        data.setThreePmToNineFifteenAmOpenCalculatedRiskRewardRatio(Math.round(b * 100.0) / 100.0);
        if (b > 0) {
            data.setThreePmToNineFifteenAmOpenRiskRewardRatioPorL("PROFIT");
        } else {
            data.setThreePmToNineFifteenAmOpenRiskRewardRatioPorL("LOSS");
        }*//*


        return data;
    }

    private boolean writeToExcelFile(HistoricalData data){

        // File name for Excel
        String fileName = "HistoricalData.xlsx";

        // Write the object to Excel
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(fileName)) {
            LocalDateTime now = LocalDateTime.now();
            String string = now.toString();
            // Create a sheet
            Sheet sheet = workbook.createSheet("report");

            // Write header row
            String[] headers = {
                    "Record ID",
                    "Token",
                    "Name",
                    "Open",
                    "High",
                    "Low",
                    "Close",
                    "Volume",
                    "Date",
                    "Time",
                    "15MIN_2PM_Open",
                    "15MIN_2PM_Time",
                    "15MIN_3PM_Open",
                    "15MIN_3PM_Time",
                    "ND_15MIN_9-15AM_High",
                    "ND_15MIN_9-15AM_High_Time",
                    "ND_15MIN_9-15AM_Open",
                    "ND_15MIN_9-15AM_Open_Time",
                    "ND_15MIN_9-15AM_Close",
                    "ND_15MIN_9-15AM_Close_Time",
                    "PD_Open",
                    "PD_High",
                    "PD_Low",
                    "PD_Close",
                    "PD_Volume",
                    "PD_Date",
                    "ND_15MIN_9-15AM_High > (25% of (H-L))+H (Yes/No)",
                    "ND_15MIN_9-15AM_High - Diff Between - H (in %)",
                    "PD_Close - Open (Diff in %)",
                    "PD_Close - Open (Red/Green)",
                    "15MIN_2PM_Open - ND_15MIN_9-15AM_Open (Diff)",
                    "15MIN_2PM_Open - ND_15MIN_9-15AM_Open (Profit/Loss)",
                    "15MIN_3PM_Open - ND_15MIN_9-15AM_Open (Diff)",
                    "15MIN_3PM_Open - ND_15MIN_9-15AM_Open (Profit/Loss)",
                    "15MIN_2PM_Open - ND_15MIN_9-15AM_Close (Diff)",
                    "15MIN_2PM_Open - ND_15MIN_9-15AM_Close (Profit/Loss)",
                    "15MIN_3PM_Open - ND_15MIN_9-15AM_Close (Diff)",
                    "15MIN_3PM_Open - ND_15MIN_9-15AM_Close (Profit/Loss)"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Write data row
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(data.getRecordId());
            dataRow.createCell(1).setCellValue(data.getToken());
            dataRow.createCell(2).setCellValue(data.getName());
            dataRow.createCell(3).setCellValue(data.getOpen());
            dataRow.createCell(4).setCellValue(data.getHigh());
            dataRow.createCell(5).setCellValue(data.getLow());
            dataRow.createCell(6).setCellValue(data.getClose());
            dataRow.createCell(7).setCellValue(data.getVolume());
            dataRow.createCell(8).setCellValue(data.getDate().toString());
            dataRow.createCell(9).setCellValue(data.getTime().toString());
            dataRow.createCell(10).setCellValue(data.getFifteenMin2PMOpen());
            dataRow.createCell(11).setCellValue(data.getFifteenMin2PMTime().toString());
            dataRow.createCell(12).setCellValue(data.getFifteenMin3PMOpen());
            dataRow.createCell(13).setCellValue(data.getFifteenMin3PMTime().toString());
            dataRow.createCell(14).setCellValue(data.getNdFifteenMin915AMHigh());
            dataRow.createCell(15).setCellValue(data.getNdFifteenMin915AMHighTime().toString());
            dataRow.createCell(16).setCellValue(data.getNdFifteenMin915AMOpen());
            dataRow.createCell(17).setCellValue(data.getNdFifteenMin915AMOpenTime().toString());
            dataRow.createCell(18).setCellValue(data.getNdFifteenMin915AMClose());
            dataRow.createCell(19).setCellValue(data.getNdFifteenMin915AMCloseTime().toString());
            dataRow.createCell(20).setCellValue(data.getPdOpen());
            dataRow.createCell(21).setCellValue(data.getPdHigh());
            dataRow.createCell(22).setCellValue(data.getPdLow());
            dataRow.createCell(23).setCellValue(data.getPdClose());
            dataRow.createCell(24).setCellValue(data.getPdVolume());
            dataRow.createCell(25).setCellValue(data.getPdDate().toString());



            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write workbook to file
            workbook.write(fileOut);
            System.out.println("Excel file written successfully: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String readInputFile(String filePath, int limit){

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header line if the CSV has headers
             br.readLine();
            int counter = 1;
            while ((line = br.readLine()) != null) {
                if (counter <= limit) {
                    // Split the line by comma (you can adjust the delimiter if needed)
                    String[] columns = line.split(",");

                    if (columns.length >= 2) { // Check if there are at least two columns
                        String date = columns[0]; // First column
                        String name = columns[1]; // Second column
                        Long token = instrumentService.getTokenByName(name);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        // Convert the string to LocalDate
                        LocalDate localDate = LocalDate.parse(date, formatter);
                        startStockAnalysis(Math.toIntExact(token),name, localDate);

                        // Print the values of the first and second columns
                       // System.out.println("First Column: " + firstColumn + ", Second Column: " + secondColumn);

                    }
                }else {
                    break;
                }
                counter++;
            }
            System.out.println("total count :"+counter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "done....";
    }

    public double calculateRiskRewardRatio( double open, double high, double low, double close,
                                            double fifteenMin2PmOpen, double nextDay15Min9_15AmClose) {
        // Entry price: 2 PM 15 min candle open
        double entryPrice = fifteenMin2PmOpen;

        // Stop loss calculation
        double stopLoss = (0.25 * (high - low)) + high;

        // Exit price: next day 9:15 AM 15 min candle close
        double exitPrice = nextDay15Min9_15AmClose;

        // Risk: Stop loss minus entry price
        double risk = stopLoss - entryPrice;

        // Reward: Entry price minus exit price
        double reward = entryPrice - exitPrice;

        // Check if stop loss is hit
        if (exitPrice > stopLoss) {
            return 0.0; // Loss scenario
        }

        // Calculate risk-reward ratio
        double riskRewardRatio = reward / risk;
        return  riskRewardRatio;
    }
}
*/
