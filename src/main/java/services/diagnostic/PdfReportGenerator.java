package services.diagnostic;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import entities.Report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfReportGenerator {

    public static void generatePdfReport(Report report) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "/Desktop/" +report.getImage().getPatient().getName()+ "_Report.pdf"));
            writer.setPageEvent(new PdfPageEventHelper() {
                public void onEndPage(PdfWriter writer, Document document) {
                    PdfContentByte cb = writer.getDirectContent();
                    cb.saveState();
                    cb.setColorStroke(BaseColor.BLACK);
                    cb.rectangle(30, 30, document.getPageSize().getWidth() - 60, document.getPageSize().getHeight() - 60);
                    cb.stroke();
                    cb.restoreState();
                }
            });

            document.open();

            // Add hospital logo
            Image logo = Image.getInstance(PdfReportGenerator.class.getResource("/img/logo/logo 2.png"));
            logo.setAlignment(Element.ALIGN_RIGHT);
            logo.scaleAbsolute(80, 80);
            document.add(logo);

            // Add professional title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Medical Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add report data
            document.add(new Paragraph("Patient Name: " + report.getImage().getPatient().getName()));
            document.add(new Paragraph("Report ID: " + report.getId()));
            document.add(new Paragraph("Doctor: " + report.getDoctor().getName() + " " + report.getDoctor().getLastName()));
            document.add(new Paragraph("Date: " + report.getDate()));

            // Add a separator line
            LineSeparator separator = new LineSeparator();
            separator.setLineColor(BaseColor.LIGHT_GRAY);
            document.add(new Chunk(separator));

            // Add medical interpretation
            Font interpretationFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.DARK_GRAY);
            Paragraph medInterpretation = new Paragraph("Medical Interpretation:", interpretationFont);
            medInterpretation.setSpacingBefore(10);
            document.add(medInterpretation);
            document.add(new Paragraph(report.getInterpretation_med()));

            // Add radiology interpretation
            Paragraph radInterpretation = new Paragraph("Radiology Interpretation:", interpretationFont);
            radInterpretation.setSpacingBefore(10);
            document.add(radInterpretation);
            document.add(new Paragraph(report.getInterpretation_rad()));

            // Add the medical image
//            String imagePath = "/dicom/" + report.getImage().getId() + ".png";
//            System.out.println(imagePath);
//            Image medicalImage = Image.getInstance(PdfReportGenerator.class.getResource(imagePath));
            String imagePath = "C:/Users/Mega-Pc/Desktop/Radiohub/src/main/java/dicom/" + report.getImage().getId() + ".png";
            Image medicalImage = Image.getInstance(imagePath);
            InputStream imageStream = new FileInputStream(imagePath);


            medicalImage.scaleToFit(400, 400);
            medicalImage.setAlignment(Element.ALIGN_CENTER);
            document.add(medicalImage);

            // Close the Document
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    // Custom class for adding a border to all pages
    private static class PdfBorder extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContent();
            Rectangle rect = document.getPageSize();
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1); // Border width (adjust as needed)
            rect.setBorderColor(BaseColor.BLACK); // Border color
            canvas.rectangle(rect);
        }
    }
}
