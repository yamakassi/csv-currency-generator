package com.yamakassi;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String BASE_FILE_NAME = "crypto_currency";
    private static final double MAX_CHANGE_PERCEMTAGE = 0.01;
    private static final Map<String, Double> cryptoCurrency = new HashMap<>();

    static {
        cryptoCurrency.put("Bitcoin", 20000.0);
        cryptoCurrency.put("Ethereum", 1800.0);
        cryptoCurrency.put("Ripple", 0.613);
        cryptoCurrency.put("Cardano", 0.2);
        cryptoCurrency.put("Solana", 41.78);
        cryptoCurrency.put("BNB", 236.04);
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <outputDirectory>");
            System.exit(1);
        }

        String outputDirectory = args[0];
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        executorService.scheduleAtFixedRate(() -> {
            Timestamp timestamp  = new Timestamp(new Date().getTime());
            String csvFilePath = outputDirectory + "/" + BASE_FILE_NAME + "_" + timestamp.getTime() + ".csv";

            try (CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath))) {
                String[] header = {"currency", "price", "timestamp"};
                csvWriter.writeNext(header);
                for (Map.Entry<String, Double> entry : cryptoCurrency.entrySet()) {
                    double currentPrice = entry.getValue();
                    double newPrice = newPrice(currentPrice);
                    entry.setValue(newPrice);
                    String[] data = {entry.getKey(), entry.getValue().toString(), String.valueOf(timestamp)};
                    System.out.println(entry.getKey()+":"+ entry.getValue().toString()+":"+ timestamp);
                    csvWriter.writeNext(data);
                }
                System.out.println("WRITE");
            } catch (Exception e) {
                System.out.println("ERROR");
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static Double newPrice(Double currentPrice) {
        Random random = new Random();
        double changePercentage = (random.nextDouble() - 0.5) * MAX_CHANGE_PERCEMTAGE;
        return currentPrice * (1 + changePercentage);
    }
}
