package enrollment;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.File;
import java.time.LocalDate;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

public class ItextEnroll {
	public void titeGeneratePDF(String Encoder, String transaction_id, String total, String balance, LocalDate date, String firstName,
			String middleName, String lastName, String libTF, String medTF, String sciTF, String compTF,
			String athTF, String mediaTF, String categoryValue, String priceValue) throws FileNotFoundException {

		// public void createPDF() throws FileNotFoundException{
		// TODO Auto-generated method stub
    	String path = "C:\\Users\\SHEAL\\git\\Student-Information-System\\transaction print\\sample2.pdf";
		PdfWriter pdfWriter = new PdfWriter(path);
		PdfDocument pdfDocument = new PdfDocument(pdfWriter);
		pdfDocument.setDefaultPageSize(PageSize.LEGAL);
		Document document = new Document(pdfDocument);
		Table TITLE = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
		Border separator = new SolidBorder(1.5f);
		Style header5 = new Style().setFontSize(15).setBold().setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.CENTER);
		Style header3 = new Style().setFontSize(15).setBold().setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.CENTER);
		Style header2 = new Style().setFontSize(30).setBold().setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.CENTER);
		Style header1 = new Style().setFontSize(20).setBold().setBorder(Border.NO_BORDER)
				.setTextAlignment(TextAlignment.CENTER);
		TITLE.addCell(new Cell().add(new Paragraph("BILL")).addStyle(header2));
		document.add(TITLE);
		Table CashierAndTime = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
		CashierAndTime.addCell(new Cell().add(new Paragraph("Date:" + date)).addStyle(header1));
		CashierAndTime.addCell(new Cell().add(new Paragraph("Encoder Name:" + Encoder)).addStyle(header1));
		document.add(CashierAndTime);
		Table TransacID = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
		TransacID.addCell(new Cell().add(new Paragraph("")).addStyle(header1));
		TransacID.addCell(new Cell().add(new Paragraph("Transaction ID:" + transaction_id)).addStyle(header1));
		document.add(TransacID.setBorderBottom(separator));
		Table Name = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
		Name.addCell(new Cell().add(new Paragraph("Name: " + firstName + " " + middleName + " " + lastName))
				.addStyle(header5));
		Name.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
		document.add(Name);
		Table MiscFee = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();

		MiscFee.addCell(new Cell().add(new Paragraph("Miscellanous Fee")).addStyle(header3));
		MiscFee.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
		MiscFee.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
		document.add(MiscFee);
		Table Bill = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
		if (libTF != null) {
			String libraryFee = libTF;
			Bill.addCell(new Cell().add(new Paragraph("Library Fee: " + libraryFee)).addStyle(header3));
			Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
		}
		if (medTF != null) {
			String medicalFee = medTF;
			Bill.addCell(new Cell().add(new Paragraph("Medical Fee: " + medicalFee)).addStyle(header3));
			Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
		}
		if (sciTF != null) {
			String scienceFee = sciTF;
			Bill.addCell(new Cell().add(new Paragraph("Science Lab Fee: " + scienceFee)).addStyle(header3));
			Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
		}
		if (compTF != null) {
			String computerFee = compTF;
			Bill.addCell(new Cell().add(new Paragraph("Computer Lab Fee: " + computerFee)).addStyle(header3));
			Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
		}
		if (athTF != null) {
			String athleteFee = athTF;
			Bill.addCell(new Cell().add(new Paragraph("Athletic Fee: " + athleteFee)).addStyle(header3));
			Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
		}
		if (mediaTF != null) {
			String mediaFee = mediaTF;
			Bill.addCell(new Cell().add(new Paragraph("Media Fee: " + mediaFee)).addStyle(header3));
			Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
		}
		if (categoryValue != null && priceValue != null) {
	    Bill.addCell(new Cell().add(new Paragraph(categoryValue + " : " + priceValue )).addStyle(header3));
		}
		document.add(Bill.setBorderBottom(separator));
		Table Total = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
		Total.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
		Total.addCell(new Cell().add(new Paragraph("Total: " + total)).addStyle(header3));
		document.add(Total);
		Table Balance = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
		Balance.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
		Balance.addCell(new Cell().add(new Paragraph("Balance: " + balance)).addStyle(header3));

		document.add(Balance);
		document.close();
	}

	public void openReceipt(String path) throws IOException, java.io.IOException {
		 try {

	            File pdfFile = new File(path);
	            if (pdfFile.exists()) {

	                if (Desktop.isDesktopSupported()) {
	                    Desktop.getDesktop().open(pdfFile);
	                } else {
	                    System.out.println("Awt Desktop is not supported!");
	                }

	            } else {
	                System.out.println("File does not exists!");
	            }


	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	}

	public String getReceiptPath(String lastName) {
		String currentDir = System.getProperty("user.dir");
		StringBuilder path = new StringBuilder();
		path.append(currentDir + "C:\\\\Users\\\\user\\\\git\\\\Student-Information-System\\\\transaction print" + lastName + ".pdf");
		return path.toString();
	}

}
