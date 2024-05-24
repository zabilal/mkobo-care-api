package com.mkobo.kobocare.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import com.mkobo.kobocare.entities.Patient;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

public class CSVHelper {

  public static ByteArrayInputStream patientProfileToCSV(Patient patient) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
      List<String> data =
          Arrays.asList(
              String.valueOf(patient.getId()),
              patient.getName(),
              String.valueOf(patient.getAge()),
              String.valueOf(patient.getLastVisitDate()));

      csvPrinter.printRecord(data);
      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("Error exporting patient data to CSV : " + e.getMessage());
    }
  }
}
