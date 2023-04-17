package com.mikhalov.taskonaut.service;

import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.kernel.pdf.navigation.PdfStringDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.mikhalov.taskonaut.dto.ExportParamsDTO;
import com.mikhalov.taskonaut.dto.LabelDTO;
import com.mikhalov.taskonaut.dto.NoteDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfExportService {

    private static final String NOTO_SANS_FONT_PATH = "static/font/NotoSans-Regular.ttf";
    private PdfFont textFont;

    private static IEventHandler getHeaderEventHandler(PdfFont headerFont) {
        return event -> {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfCanvas pdfCanvas = new PdfCanvas(docEvent.getPage());
            Rectangle pageSize = docEvent.getPage().getPageSize();
            int pageNumber = pdfDoc.getPageNumber(docEvent.getPage());

            pdfCanvas.beginText()
                    .setFontAndSize(headerFont, 10)
                    .moveText(pageSize.getWidth() / 2 - 5, pageSize.getBottom() + 20)
                    .showText(String.valueOf(pageNumber))
                    .endText();
        };
    }

    public byte[] exportNotesToPdf(List<NoteDTO> notes, ExportParamsDTO exportParams) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        PdfFont headerFont = PdfFontFactory.createFont("Helvetica-Bold");
        IEventHandler headerEventHandler = getHeaderEventHandler(headerFont);
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, headerEventHandler);
        Document document = new Document(pdfDocument, PageSize.A4);
        textFont = PdfFontFactory.createFont(NOTO_SANS_FONT_PATH, "Cp1251");

        document.add(createTitle());
        document.add(createSnapshot());
        document.add(createSortedBy(exportParams));

        PdfOutline tocOutline = setupTOC(pdfDocument);

        for (NoteDTO note : notes) {
            document.add(createNoteDiv(note, tocOutline, pdfDocument));
        }

        document.close();

        return outputStream.toByteArray();
    }

    private Paragraph createTitle() {
        return new Paragraph("My Notes")
                .setFont(textFont)
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
    }

    private Paragraph createSortedBy(ExportParamsDTO exportParams) {
        String sortedBy = String.format("Sorted by %s %s",
                exportParams.getSort().getValue(),
                exportParams.isAsc() ? "ascending" : "descending");

        return new Paragraph(sortedBy)
                .setFont(textFont)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
    }

    private Paragraph createSnapshot() {
        String snapshot = "Snapshot: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return new Paragraph(snapshot)
                .setFont(textFont)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER);

    }

    private PdfOutline setupTOC(PdfDocument pdfDocument) {
        PdfOutline rootOutline = pdfDocument.getOutlines(false);
        PdfOutline tocOutline = rootOutline.addOutline("Notes by title");
        tocOutline.addDestination(PdfExplicitDestination.createFitH(pdfDocument.getFirstPage(), PageSize.A4.getTop()));
        return tocOutline;
    }

    private Div createNoteDiv(NoteDTO note, PdfOutline tocOutline, PdfDocument pdfDocument) {
        String noteTitle = note.getTitle();
        String noteContent = note.getContent();
        LabelDTO noteLabel = note.getLabelDTO();
        String lastModifiedAt = note.getLastModifiedAt();

        PdfOutline noteOutline = tocOutline.addOutline(noteTitle);
        PdfStringDestination noteDestination = new PdfStringDestination(noteTitle);
        pdfDocument.addNamedDestination(noteTitle, noteDestination.getPdfObject());
        noteOutline.addDestination(noteDestination);

        Div noteDiv = new Div();
        noteDiv.setKeepTogether(true);

        Paragraph noteTitleParagraph = new Paragraph(noteTitle)
                .setFont(textFont)
                .setFontSize(16)
                .setBold()
                .setMarginBottom(5)
                .setDestination(noteTitle);
        noteDiv.add(noteTitleParagraph);

        Paragraph noteContentParagraph = new Paragraph(noteContent)
                .setFont(textFont)
                .setFontSize(12)
                .setMarginBottom(5);
        noteDiv.add(noteContentParagraph);

        if (noteLabel != null) {
            Paragraph noteLabelParagraph = new Paragraph("Label: " + noteLabel.getName())
                    .setFont(textFont)
                    .setFontSize(10)
                    .setItalic()
                    .setMarginBottom(5);
            noteDiv.add(noteLabelParagraph);
        }

        Paragraph noteLastModifiedParagraph = new Paragraph("Last Modified: " + lastModifiedAt)
                .setFont(textFont)
                .setFontSize(10)
                .setMarginBottom(20);
        noteDiv.add(noteLastModifiedParagraph);

        return noteDiv;
    }
}