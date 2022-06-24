package com.bezkoder.spring.files.csv.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.files.csv.model.Bankcoded;
import com.bezkoder.spring.files.csv.model.Tutorial;

public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERs = { "Id", "Title", "Description", "Published" };
  static String[] HEADERs_bankcode = { "銀行代號", "分支機構代號", "金融機構名稱", "分支機構名稱", "地址" };

  public static boolean hasCSVFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  public static List<Tutorial> csvToTutorials(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<Tutorial> tutorials = new ArrayList<Tutorial>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
        Tutorial tutorial = new Tutorial(
              Long.parseLong(csvRecord.get("Id")),
              csvRecord.get("Title"),
              csvRecord.get("Description"),
              Boolean.parseBoolean(csvRecord.get("Published"))
            );

        tutorials.add(tutorial);
      }

      return tutorials;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  
  public static List<Bankcoded> csvToBankcodeds(InputStream is) {
    Instant date = ZonedDateTime.now().toInstant();
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<Bankcoded> bankcodeds = new ArrayList<Bankcoded>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
        if ("".equals(csvRecord.get(1)) || csvRecord.get(1) == null) {
          continue;
        }
        String newBankCode = ("00" + csvRecord.get(0)).substring(("00" + csvRecord.get(0)).length() - 3);
        String newBranchCode = ("000" + csvRecord.get(1)).substring(("000" + csvRecord.get(1)).length() - 4);
        Bankcoded bankcoded = new Bankcoded();
        bankcoded.setBankAccount(newBankCode + newBranchCode);
        bankcoded.setBankName(csvRecord.get(2) + csvRecord.get(3));
        bankcoded.setBankShortName(csvRecord.get(2));
        bankcoded.setCreateMan("測試人員");
        bankcoded.setChangeMan("測試人員");
        bankcoded.setCreateDate(date);
        bankcoded.setChangeDate(date);

        bankcodeds.add(bankcoded);
      }

      return bankcodeds;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream tutorialsToCSV(List<Tutorial> tutorials) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      for (Tutorial tutorial : tutorials) {
        List<String> data = Arrays.asList(
              String.valueOf(tutorial.getId()),
              tutorial.getTitle(),
              tutorial.getDescription(),
              String.valueOf(tutorial.isPublished())
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }
  

}
