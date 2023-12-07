package enrollment;

import java.io.FileNotFoundException;
import java.time.LocalDate;

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


public class Itext {
	public void generatePDF(String transaction_id, double total, double balance,  LocalDate date) throws FileNotFoundException {
		
	//public void createPDF() throws FileNotFoundException{
		// TODO Auto-generated method stub
		String path = "C:\\seiffer2\\sample5.pdf";
	    PdfWriter pdfWriter = new PdfWriter(path);
	    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
	    pdfDocument.setDefaultPageSize(PageSize.LEGAL);
	    Document document = new Document(pdfDocument);
	    Table TITLE = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
	    Border separator = new SolidBorder(1.5f);
	    Style header5 = new Style()
                .setFontSize(15)
                .setBold()
                .setBorder(Border.NO_BORDER)
	    .setTextAlignment(TextAlignment.CENTER);
	    Style header3 = new Style()
                .setFontSize(15)
                .setBold()
                .setBorder(Border.NO_BORDER)
	    .setTextAlignment(TextAlignment.CENTER);
	    Style header2 = new Style()
                .setFontSize(30)
                .setBold()
                .setBorder(Border.NO_BORDER)
	           .setTextAlignment(TextAlignment.CENTER);
	    Style header1 = new Style()
                .setFontSize(20)
                .setBold()
                .setBorder(Border.NO_BORDER)
	    .setTextAlignment(TextAlignment.CENTER);
	    TITLE.addCell(new Cell().add(new Paragraph("BILL")).addStyle(header2));
	    document.add(TITLE);
	    Table CashierAndTime = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
	    CashierAndTime.addCell(new Cell().add(new Paragraph("Date:" + date)).addStyle(header1));
	    CashierAndTime.addCell(new Cell().add(new Paragraph("Cashier Name:Seiffer")).addStyle(header1));
	    document.add(CashierAndTime);
	    Table TransacID = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
	    TransacID.addCell(new Cell().add(new Paragraph("")).addStyle(header1));
	    TransacID.addCell(new Cell().add(new Paragraph("Transaction ID:" + transaction_id)).addStyle(header1));
	    document.add(TransacID.setBorderBottom(separator));
	    Table Name = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
	    Name.addCell(new Cell().add(new Paragraph("Name:")).addStyle(header5));
	    Name.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
	    document.add(Name);
	    Table MiscFee = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
	 
	    MiscFee.addCell(new Cell().add(new Paragraph("Miscellanous Fee")).addStyle(header3));
	    MiscFee.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
	    MiscFee.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
	    document.add(MiscFee);
	    Table Bill = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
	    Bill.addCell(new Cell().add(new Paragraph("Library Fee")).addStyle(header3));
	    Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
	    Bill.addCell(new Cell().add(new Paragraph("Medical Fee")).addStyle(header3));
	    Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
	    Bill.addCell(new Cell().add(new Paragraph("Science Lab Fee")).addStyle(header3));
	    Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header5));
	    Bill.addCell(new Cell().add(new Paragraph("Computer Lab Fee")).addStyle(header3));
	    Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
	    Bill.addCell(new Cell().add(new Paragraph("Athletic Fee")).addStyle(header3));
	    Bill.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
	    Bill.addCell(new Cell().add(new Paragraph("Media Fee")).addStyle(header3));
	    document.add(Bill.setBorderBottom(separator));
	    Table Total = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
	    Total.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
	    Total.addCell(new Cell().add(new Paragraph("Total" + total)).addStyle(header3));
	    document.add(Total);
	    Table Balance = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();
	    Balance.addCell(new Cell().add(new Paragraph("")).addStyle(header3));
	    Balance.addCell(new Cell().add(new Paragraph("Balance" + balance)).addStyle(header3));
	  
	    document.add(Balance);
	    document.close();
	}

	
				
	}

	

