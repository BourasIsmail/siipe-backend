package ma.entraide.siipe.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.entity.*;
import ma.entraide.siipe.repository.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    private final BeneficiaireRepository beneficiaireRepo;
    private final PersonnelRepository personnelRepo;
    private final EtablissementCentreRepository etablissementRepo;

    private static final BaseColor GREEN = new BaseColor(46, 125, 50);
    private static final BaseColor LIGHT_GREEN = new BaseColor(200, 230, 201);
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.WHITE);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
    private static final Font DATA_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);

    public byte[] exportBeneficiairesPdf(Long provinceId) throws DocumentException {
        List<Beneficiaire> list = provinceId != null
                ? beneficiaireRepo.findByEtablissementCentreProvinceIdAndDeletedFalse(provinceId)
                : beneficiaireRepo.findByDeletedFalse();

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        addTitle(document, "Liste des Bénéficiaires - SIIPE");
        addMeta(document, "Total: " + list.size() + " bénéficiaires");

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        float[] widths = {0.5f, 1.2f, 1.2f, 1f, 0.8f, 1f, 1.2f, 1.2f, 1f};
        table.setWidths(widths);

        addTableHeader(table, new String[]{"ID", "Nom", "Prénom", "CIN", "Sexe",
                "Date Naiss.", "Situation", "Établissement", "Province"});

        boolean alternate = false;
        for (Beneficiaire b : list) {
            BaseColor rowColor = alternate ? LIGHT_GREEN : BaseColor.WHITE;
            addRow(table, rowColor,
                    String.valueOf(b.getId()),
                    b.getNom(),
                    b.getPrenom(),
                    b.getCin() != null ? b.getCin() : "",
                    b.getSexe() != null ? b.getSexe().name() : "",
                    b.getDateNaissance() != null ? b.getDateNaissance().toString() : "",
                    b.getSituationDifficulte() != null ? b.getSituationDifficulte().name() : "",
                    b.getEtablissementCentre() != null ? b.getEtablissementCentre().getNomFr() : "",
                    b.getEtablissementCentre() != null && b.getEtablissementCentre().getProvince() != null
                            ? b.getEtablissementCentre().getProvince().getNomFr() : ""
            );
            alternate = !alternate;
        }

        document.add(table);
        addFooter(document);
        document.close();
        return out.toByteArray();
    }

    public byte[] exportPersonnelPdf(Long provinceId) throws DocumentException {
        List<Personnel> list = provinceId != null
                ? personnelRepo.findByProvinceId(provinceId)
                : personnelRepo.findByDeletedFalse();

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 30, 30);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        addTitle(document, "Liste du Personnel - SIIPE");
        addMeta(document, "Total: " + list.size() + " agents");

        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        addTableHeader(table, new String[]{"ID", "Nom", "Prénom", "Matricule",
                "Grade", "Fonction", "Établissement", "Date Recrutement"});

        boolean alternate = false;
        for (Personnel p : list) {
            BaseColor rowColor = alternate ? LIGHT_GREEN : BaseColor.WHITE;
            addRow(table, rowColor,
                    String.valueOf(p.getId()),
                    p.getNom(),
                    p.getPrenom(),
                    p.getMatricule(),
                    p.getGrade() != null ? p.getGrade().name() : "",
                    p.getFonction() != null ? p.getFonction().name() : "",
                    p.getEtablissementCentre() != null ? p.getEtablissementCentre().getNomFr() : "",
                    p.getDateRecrutement() != null ? p.getDateRecrutement().toString() : ""
            );
            alternate = !alternate;
        }

        document.add(table);
        addFooter(document);
        document.close();
        return out.toByteArray();
    }

    public byte[] exportBeneficiaireFichePdf(Beneficiaire b) throws DocumentException {
        Document document = new Document(PageSize.A4, 40, 40, 50, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        addTitle(document, "Fiche Bénéficiaire");

        // Identity section
        addSectionTitle(document, "Identité");
        addInfoLine(document, "Nom complet", b.getNom() + " " + b.getPrenom());
        addInfoLine(document, "CIN", b.getCin() != null ? b.getCin() : "N/A");
        addInfoLine(document, "Date de naissance", b.getDateNaissance() != null ? b.getDateNaissance().toString() : "N/A");
        addInfoLine(document, "Lieu de naissance", b.getLieuNaissance() != null ? b.getLieuNaissance() : "N/A");
        addInfoLine(document, "Sexe", b.getSexe() != null ? b.getSexe().name() : "N/A");
        addInfoLine(document, "Nationalité", b.getNationalite() != null ? b.getNationalite() : "N/A");
        addInfoLine(document, "Adresse", b.getAdresse() != null ? b.getAdresse() : "N/A");

        // Situation section
        addSectionTitle(document, "Situation");
        addInfoLine(document, "Situation de difficulté",
                b.getSituationDifficulte() != null ? b.getSituationDifficulte().name() : "N/A");
        addInfoLine(document, "Date d'entrée", b.getDateEntree() != null ? b.getDateEntree().toString() : "N/A");
        addInfoLine(document, "Établissement",
                b.getEtablissementCentre() != null ? b.getEtablissementCentre().getNomFr() : "N/A");
        addInfoLine(document, "Programme", b.getProgramme() != null ? b.getProgramme().getNomFr() : "N/A");

        // Family section
        addSectionTitle(document, "Famille / Tuteurs");
        addInfoLine(document, "Nom du père", b.getNomPere() != null ? b.getNomPere() : "N/A");
        addInfoLine(document, "Nom de la mère", b.getNomMere() != null ? b.getNomMere() : "N/A");
        addInfoLine(document, "Tuteur", b.getNomTuteur() != null ? b.getNomTuteur() : "N/A");
        addInfoLine(document, "Téléphone parent", b.getTelephoneParent() != null ? b.getTelephoneParent() : "N/A");

        addFooter(document);
        document.close();
        return out.toByteArray();
    }

    private void addTitle(Document doc, String text) throws DocumentException {
        PdfPTable titleTable = new PdfPTable(1);
        titleTable.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell(new Phrase(text, TITLE_FONT));
        cell.setBackgroundColor(GREEN);
        cell.setPadding(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        titleTable.addCell(cell);
        doc.add(titleTable);
        doc.add(Chunk.NEWLINE);
    }

    private void addMeta(Document doc, String text) throws DocumentException {
        Paragraph p = new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));
        p.setSpacingAfter(10);
        doc.add(p);
    }

    private void addSectionTitle(Document doc, String text) throws DocumentException {
        Paragraph p = new Paragraph(text, new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, GREEN));
        p.setSpacingBefore(12);
        p.setSpacingAfter(4);
        doc.add(p);
        doc.add(new LineSeparator());
    }

    private void addInfoLine(Document doc, String label, String value) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2});

        PdfPCell labelCell = new PdfPCell(new Phrase(label + ":", HEADER_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(4);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, DATA_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(4);

        table.addCell(labelCell);
        table.addCell(valueCell);
        doc.add(table);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(GREEN);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(6);
            table.addCell(cell);
        }
    }

    private void addRow(PdfPTable table, BaseColor bg, String... values) {
        for (String val : values) {
            PdfPCell cell = new PdfPCell(new Phrase(val != null ? val : "", DATA_FONT));
            cell.setBackgroundColor(bg);
            cell.setPadding(4);
            table.addCell(cell);
        }
    }

    private void addFooter(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        Paragraph footer = new Paragraph(
                "Généré par SIIPE - Entraide Nationale | " + java.time.LocalDate.now(),
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);
    }
}
