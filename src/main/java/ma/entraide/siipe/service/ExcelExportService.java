package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.entity.*;
import ma.entraide.siipe.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final BeneficiaireRepository beneficiaireRepo;
    private final PersonnelRepository personnelRepo;
    private final EtablissementCentreRepository etablissementRepo;

    public byte[] exportBeneficiaires(Long provinceId) throws IOException {
        List<Beneficiaire> list = provinceId != null
                ? beneficiaireRepo.findByEtablissementCentreProvinceIdAndDeletedFalse(provinceId)
                : beneficiaireRepo.findByDeletedFalse();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Bénéficiaires");

            // Header style
            XSSFCellStyle headerStyle = createHeaderStyle(workbook);
            XSSFCellStyle dateStyle = createDateStyle(workbook);

            // Title row
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Liste des Bénéficiaires - SIIPE");
            XSSFCellStyle titleStyle = workbook.createCellStyle();
            XSSFFont titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleFont.setColor(IndexedColors.WHITE.getIndex());
            titleStyle.setFont(titleFont);
            titleStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte)46, (byte)125, (byte)50}, null));
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));

            // Headers
            String[] headers = {"ID", "Nom", "Prénom", "CIN", "Sexe", "Date Naissance",
                    "Situation", "Établissement", "Programme", "Province",
                    "Date Entrée", "Assistante Sociale", "Créé le"};
            Row headerRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowNum = 2;
            for (Beneficiaire b : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(b.getId());
                row.createCell(1).setCellValue(b.getNom());
                row.createCell(2).setCellValue(b.getPrenom());
                row.createCell(3).setCellValue(b.getCin() != null ? b.getCin() : "");
                row.createCell(4).setCellValue(b.getSexe() != null ? b.getSexe().name() : "");
                row.createCell(5).setCellValue(b.getDateNaissance() != null ? b.getDateNaissance().toString() : "");
                row.createCell(6).setCellValue(b.getSituationDifficulte() != null ? b.getSituationDifficulte().name() : "");
                row.createCell(7).setCellValue(b.getEtablissementCentre() != null ? b.getEtablissementCentre().getNomFr() : "");
                row.createCell(8).setCellValue(b.getProgramme() != null ? b.getProgramme().getNomFr() : "");
                row.createCell(9).setCellValue(b.getEtablissementCentre() != null && b.getEtablissementCentre().getProvince() != null
                        ? b.getEtablissementCentre().getProvince().getNomFr() : "");
                row.createCell(10).setCellValue(b.getDateEntree() != null ? b.getDateEntree().toString() : "");
                row.createCell(11).setCellValue(b.getAssistanteSociale() != null ? b.getAssistanteSociale().getFullName() : "");
                row.createCell(12).setCellValue(b.getCreatedAt() != null ? b.getCreatedAt().toLocalDate().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportPersonnel(Long provinceId) throws IOException {
        List<Personnel> list = provinceId != null
                ? personnelRepo.findByProvinceId(provinceId)
                : personnelRepo.findByDeletedFalse();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Personnel");
            XSSFCellStyle headerStyle = createHeaderStyle(workbook);

            // Title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Liste du Personnel - SIIPE");

            // Headers
            String[] headers = {"ID", "Nom", "Prénom", "CIN", "Matricule", "Sexe",
                    "Grade", "Fonction", "Poste occupé", "Établissement",
                    "Programme", "Date Recrutement", "Salaire"};
            Row headerRow = sheet.createRow(1);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int rowNum = 2;
            for (Personnel p : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNom());
                row.createCell(2).setCellValue(p.getPrenom());
                row.createCell(3).setCellValue(p.getCin() != null ? p.getCin() : "");
                row.createCell(4).setCellValue(p.getMatricule());
                row.createCell(5).setCellValue(p.getSexe() != null ? p.getSexe().name() : "");
                row.createCell(6).setCellValue(p.getGrade() != null ? p.getGrade().name() : "");
                row.createCell(7).setCellValue(p.getFonction() != null ? p.getFonction().name() : "");
                row.createCell(8).setCellValue(p.getPosteOccupe() != null ? p.getPosteOccupe().name() : "");
                row.createCell(9).setCellValue(p.getEtablissementCentre() != null ? p.getEtablissementCentre().getNomFr() : "");
                row.createCell(10).setCellValue(p.getProgramme() != null ? p.getProgramme().getNomFr() : "");
                row.createCell(11).setCellValue(p.getDateRecrutement() != null ? p.getDateRecrutement().toString() : "");
                row.createCell(12).setCellValue(p.getSalaire() != null ? p.getSalaire() : 0);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportEtablissements(Long provinceId) throws IOException {
        List<EtablissementCentre> list = provinceId != null
                ? etablissementRepo.findByProvinceIdAndDeletedFalse(provinceId)
                : etablissementRepo.findByDeletedFalse();

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Établissements");
            XSSFCellStyle headerStyle = createHeaderStyle(workbook);

            String[] headers = {"ID", "Nom FR", "Nom AR", "Code", "Téléphone",
                    "Adresse", "Province", "Milieu", "Type Local",
                    "Capacité Accueil", "Géré par", "Date Exploitation"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (EtablissementCentre e : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(e.getId());
                row.createCell(1).setCellValue(e.getNomFr());
                row.createCell(2).setCellValue(e.getNomAr() != null ? e.getNomAr() : "");
                row.createCell(3).setCellValue(e.getCode() != null ? e.getCode() : "");
                row.createCell(4).setCellValue(e.getTelephone() != null ? e.getTelephone() : "");
                row.createCell(5).setCellValue(e.getAdresse() != null ? e.getAdresse() : "");
                row.createCell(6).setCellValue(e.getProvince() != null ? e.getProvince().getNomFr() : "");
                row.createCell(7).setCellValue(e.getMilieu() != null ? e.getMilieu().name() : "");
                row.createCell(8).setCellValue(e.getTypeLocal() != null ? e.getTypeLocal().name() : "");
                row.createCell(9).setCellValue(e.getCapaciteAccueil() != null ? e.getCapaciteAccueil() : 0);
                row.createCell(10).setCellValue(e.getGererPar() != null ? e.getGererPar().name() : "");
                row.createCell(11).setCellValue(e.getDateExploitation() != null ? e.getDateExploitation().toString() : "");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)46, (byte)125, (byte)50}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private XSSFCellStyle createDateStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
        return style;
    }
}
