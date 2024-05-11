package services.diagnostic;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import entities.Prescription;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfPrescriptionGenerator {

    public static void generatePdfPrescription(Prescription prescription) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "/Desktop/" + prescription.getReport().getImage().getPatient().getName() + "_prescription.pdf"));
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
            Image logo = Image.getInstance(PdfPrescriptionGenerator.class.getResource("/img/logo/logo 2.png"));
            logo.setAlignment(Element.ALIGN_RIGHT);
            logo.scaleAbsolute(80, 80);
            document.add(logo);

            // Add professional title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Medical Prescription N° " + prescription.getId(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Add report data
            document.add(new Paragraph("Patient Name: " + prescription.getReport().getImage().getPatient().getName()));
            document.add(new Paragraph("Patient Last Name: " + prescription.getReport().getImage().getPatient().getLastName()));
            document.add(new Paragraph("Doctor: " + prescription.getReport().getDoctor().getName() + " " + prescription.getReport().getDoctor().getLastName()));
            document.add(new Paragraph("Date: " + prescription.getDate()));

            // Add a separator line
            LineSeparator separator = new LineSeparator();
            separator.setLineColor(BaseColor.LIGHT_GRAY);
            document.add(new Chunk(separator));

            // Add medical interpretation
            Font interpretationFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.DARK_GRAY);

            // Add prescription content
            Paragraph prescriptionContent = new Paragraph("Prescription Content:", interpretationFont);
            prescriptionContent.setAlignment(Element.ALIGN_CENTER);
            prescriptionContent.setSpacingBefore(100); // Add spacing before the paragraph
            prescriptionContent.setSpacingAfter(10);
            document.add(prescriptionContent);
            document.add(new Paragraph(prescription.getContenu()));



            // Add the medical image at the bottom of the page
            String imagePath = "C:\\Users\\Ala\\Desktop\\Repo_3A56_Invictus_Symfony-main\\public\\uploads\\signatures\\" + prescription.getSignature_filename();
            System.out.println(imagePath);
//            Image medicalImage = Image.getInstance(PdfPrescriptionGenerator.class.getResource(imagePath));
//            InputStream imageStream = PdfPrescriptionGenerator.class.getResourceAsStream(imagePath);
            Image medicalImage = Image.getInstance(imagePath);
            InputStream imageStream = new FileInputStream(imagePath);

            if (imageStream != null) {
            medicalImage.scaleToFit(150, 150);
            medicalImage.setAlignment(Element.ALIGN_CENTER);
            medicalImage.setAbsolutePosition((document.getPageSize().getWidth() - medicalImage.getScaledWidth()) / 2, 50);
            document.add(medicalImage);
            } else {
                System.err.println("Failed to load signature: " + imagePath);
            }
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
