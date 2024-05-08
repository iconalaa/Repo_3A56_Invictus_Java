package Services;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import Entities.gratification;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFGeneration {
    public static void generatePdfReport(gratification grat) {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(System.getProperty("user.home") + "/Desktop/" + grat.getDonor().getPrenom_donateur() + "_Report.pdf"));
            writer.setPageEvent(new PdfBorder()); // Set border for all pages

            document.open();

            // Add hospital logo
            Image logo = Image.getInstance(PDFGeneration.class.getResource("/img/logo/logo-row.png"));
            logo.setAlignment(Element.ALIGN_CENTER);
            logo.scaleAbsolute(180, 100);
            document.add(logo);

            // Add professional title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Proof of Donation", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);



            // Add report data
            document.add(new Paragraph("Donor Name: " + grat.getDonor().getPrenom_donateur()));
            document.add(new Paragraph("Donor Last Name: " + grat.getDonor().getNom_donateur()));
            document.add(new Paragraph("Donor Email: " + grat.getDonor().getEmail()));
            document.add(new Paragraph("Donor Type/Function: " + grat.getDonor().getType_donateur()));
            document.add(new Paragraph("Donor Telephone: " + grat.getDonor().getTelephone()));

            LineSeparator separator = new LineSeparator();
            separator.setLineColor(BaseColor.LIGHT_GRAY);
            document.add(new Chunk(separator));



            //document.add(new Paragraph("Date: " + formattedDate));
            document.add(new Paragraph("Title: " + grat.getTitre_grat()));
            document.add(new Paragraph("Description: " + grat.getDesc_grat()));
            document.add(new Paragraph("Amount: " + grat.getMontant()));
            document.add(new Paragraph("Paiment method: Card"));


            // Add a separator line
            LineSeparator separator2 = new LineSeparator();
            separator.setLineColor(BaseColor.LIGHT_GRAY);
            document.add(new Chunk(separator2));

            Image cachet = Image.getInstance(PDFGeneration.class.getResource("/img/Cachet.png"));
            cachet.setAlignment(Element.ALIGN_RIGHT);
            cachet.scaleAbsolute(170, 170);

            document.add(cachet);

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



