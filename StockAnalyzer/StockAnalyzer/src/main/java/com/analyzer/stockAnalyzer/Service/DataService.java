package com.analyzer.stockAnalyzer.Service;

import com.analyzer.stockAnalyzer.Repo.DataRepo;
import com.analyzer.stockAnalyzer.Models.HistoricalData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.util.*;

@Service
public class DataService {

    @Autowired
    private DataRepo dataRepo;


    @Autowired
    private InstrumentService instrumentService;


    public void startStockAnalysis(int tokenParam, String name, LocalDate dateParam) {

        HistoricalData historicalData = new HistoricalData();
        String enctoken = "yaltI8/o7NUHwWBEm/ZTm9HUy8uKTBraQi1NqydArcFuA6LwF7LxcoerZ3a6EyXbc4zZ4rwshfZCZWIDU6k7GlykPmOLZnrVT1H3Zlvf5pDfaQKHYtn4FQ==";
        int token = tokenParam;
        String stockName = name;

        List<String> timeFrameList = Arrays.asList("day", "15minute");
        for (String currentTimeFrame : timeFrameList) {

            System.out.println(" === timeframe : "+currentTimeFrame);

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
                    System.out.println("Attempt " + (retryCount + 1) + " for date: " + startDate);

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

                                    System.out.println("date and time : "+dateTime);


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

                                        System.out.println("Logged : timeframe.equals(\"day\") && LocalDate.parse(date).equals(eventDate" );

                                    } else if (timeframe.equals("15minute") && LocalDate.parse(date).equals(eventDate) && time.equals(LocalTime.of(14, 00))){
                                        historicalData.setFifteenMin2PMOpen(open);
                                        historicalData.setFifteenMin2PMTime(time);
                                        System.out.println("Logged : timeframe.equals(\"15minute\") && LocalDate.parse(date).equals(eventDate) && time.equals(LocalTime.of(14, 00))");
                                    }else if (timeframe.equals("15minute") && LocalDate.parse(date).equals(eventDate) && time.equals(LocalTime.of(15, 00))){
                                        historicalData.setFifteenMin3PMOpen(open);
                                        historicalData.setFifteenMin3PMTime(time);
                                        System.out.println("Logged : timeframe.equals(\"15minute\") && LocalDate.parse(date).equals(eventDate) && time.equals(LocalTime.of(15, 00) ");
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

                                        System.out.println("Logged : LocalDate.parse(date).isAfter(eventDate) && timeframe.equals(\"15minute\") && time.equals(LocalTime.of(9, 15)");
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

                                        System.out.println("Logged : LocalDate.parse(date).isBefore(eventDate) && timeframe.equals(\"day\")");
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
            System.out.println("stoped null!!!!");
        }
        System.out.println(saved);
    }

    private HistoricalData AnalyzeStocks(HistoricalData data){

       // ND_15MIN_9-15AM_HIGH > (25% of (H-L))+H = (Yes/No)
        double a = ((data.getHigh() - data.getLow()) * 0.25)+data.getHigh();
        double b = data.getNdFifteenMin915AMHigh();
        if (b>a){
            data.setEquationOne("YES");
        }else {
            data.setEquationOne("NO");
        }

        //ND_15MIN_9-15AM_HIGH  diff btwn  H = in%
        //double percentageChange = ((newPrice - oldPrice) / oldPrice) * 100;
        double c = ((data.getNdFifteenMin915AMHigh() - data.getHigh()) / data.getHigh()) * 100;
        data.setEquationTwo(c);

        //PD_CLOSE - Open = Diff in %
        double d = ((data.getOpen() - data.getPdClose())/data.getPdClose())*100;
        data.setEquationThree(d);

        //PD_CLOSE - Open = (RED/Green)
        double e = data.getPdClose() - data.getOpen();
        if (e > 0){
            data.setEquationFour("GREEN");
        }else {
            data.setEquationFour("RED");
        }

        //15MIN_2PM_Open - ND_15MIN_9-15AM_Open = calcute diff in percentage
        double f = ((data.getFifteenMin2PMOpen() - data.getNdFifteenMin915AMOpen()) / data.getNdFifteenMin915AMOpen()) * 100;
        data.setEquationFive(f);

        //15MIN_2PM_Open - ND_15MIN_9-15AM_Open = profit/loss
        double g = data.getFifteenMin2PMOpen() - data.getNdFifteenMin915AMOpen();
        if (g > 0){
            data.setEquationSix("PROFIT");
        }else {
            data.setEquationSix("LOSS");
        }

        //15MIN_3PM_Open - ND_15MIN_9-15AM_Open = diff
        double h = ((data.getFifteenMin3PMOpen() - data.getNdFifteenMin915AMOpen()) / data.getNdFifteenMin915AMOpen()) * 100;
        data.setEquationSeven(h);

        //15MIN_3PM_Open - ND_15MIN_9-15AM_Open = profit/loss
        double i = data.getFifteenMin3PMOpen() - data.getNdFifteenMin915AMOpen();
        if (i > 0){
            data.setEquationEight("PROFIT");
        }else {
            data.setEquationEight("LOSS");
        }

        //15MIN_2PM_Open - ND_15MIN_9-15AM_close = diff
        double j = ((data.getFifteenMin2PMOpen() - data.getNdFifteenMin915AMClose()) / data.getNdFifteenMin915AMClose()) * 100;
        data.setEquationNine(j);

        //15MIN_2PM_Open - ND_15MIN_9-15AM_Close = profit/loss
        double k = data.getFifteenMin2PMOpen() - data.getNdFifteenMin915AMClose();
        if (k > 0){
            data.setEquationTen("PROFIT");
        }else {
            data.setEquationTen("LOSS");
        }

        //15MIN_3PM_Open - ND_15MIN_9-15AM_Close = diff
        double l = ((data.getFifteenMin3PMOpen() - data.getNdFifteenMin915AMClose()) / data.getNdFifteenMin915AMClose()) * 100;
        data.setEquationEleven(l);

        //15MIN_3PM_Open - ND_15MIN_9-15AM_Close = profit/loss
        double m = data.getFifteenMin3PMOpen() - data.getNdFifteenMin915AMClose();
        if (m > 0){
            data.setEquationTwelve("PROFIT");
        }else {
            data.setEquationTwelve("LOSS");
        }

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
            dataRow.createCell(26).setCellValue(data.getEquationOne());
            dataRow.createCell(27).setCellValue(data.getEquationTwo());
            dataRow.createCell(28).setCellValue(data.getEquationThree());
            dataRow.createCell(29).setCellValue(data.getEquationFour());
            dataRow.createCell(30).setCellValue(data.getEquationFive());
            dataRow.createCell(31).setCellValue(data.getEquationSix());
            dataRow.createCell(32).setCellValue(data.getEquationSeven());
            dataRow.createCell(33).setCellValue(data.getEquationEight());
            dataRow.createCell(34).setCellValue(data.getEquationNine());
            dataRow.createCell(35).setCellValue(data.getEquationTen());
            dataRow.createCell(36).setCellValue(data.getEquationEleven());
            dataRow.createCell(37).setCellValue(data.getEquationTwelve());


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
}
