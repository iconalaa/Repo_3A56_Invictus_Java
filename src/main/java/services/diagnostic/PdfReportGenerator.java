package services.diagnostic;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Report;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfReportGenerator {

    public static void generatePdfReport(Report report) {
        Document document = new Document();
        try {
            // Specify the output location of the PDF
            PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "/Desktop/Report.pdf"));

            // Open the Document
            document.open();

            // Add hospital name to the header
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
            Paragraph header = new Paragraph("RADIOHUB", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            // Add the logo to the top right corner
            Image logo = Image.getInstance(PdfReportGenerator.class.getResource("/img/logo/logo 2.png"));
            logo.setAbsolutePosition(document.right() - 100, document.top() - 40);
            logo.scaleAbsolute(80, 80); // Adjust the size as needed
            document.add(logo);

            // Add the second image to the document
            String imagePath = "/img/testimage/" + report.getImage().getFilename() + ".png";
            Image additionalImage = Image.getInstance(PdfReportGenerator.class.getResource(imagePath));
            additionalImage.scaleToFit(400, 400); // Adjust the size as needed
            additionalImage.setAlignment(Element.ALIGN_CENTER);
            document.add(additionalImage);

            // Add report data to the Document
            document.add(new Paragraph("Patient Name: " + report.getImage().getPatient().getName()));
            document.add(new Paragraph("Report ID: " + report.getId()));
            document.add(new Paragraph("Doctor: " + report.getDoctor().getName() + " " + report.getDoctor().getLastName()));
            document.add(new Paragraph("Date: " + report.getDate()));
            document.add(new Paragraph("Medical Interpretation: " + report.getInterpretation_med()));
            document.add(new Paragraph("Radiology Interpretation: " + report.getInterpretation_rad()));

            // Close the Document
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
