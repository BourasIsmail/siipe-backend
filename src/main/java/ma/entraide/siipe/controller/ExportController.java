package ma.entraide.siipe.controller;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.entity.Beneficiaire;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.exception.ResourceNotFoundException;
import ma.entraide.siipe.repository.BeneficiaireRepository;
import ma.entraide.siipe.service.ExcelExportService;
import ma.entraide.siipe.service.PdfExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExcelExportService excelService;
    private final PdfExportService pdfService;
    private final BeneficiaireRepository beneficiaireRepo;

    // ---- Excel exports ----

    @GetMapping("/excel/beneficiaires")
    public ResponseEntity<byte[]> exportBeneficiairesExcel(@AuthenticationPrincipal User user) throws Exception {
        Long provinceId = getProvinceFilter(user);
        byte[] data = excelService.exportBeneficiaires(provinceId);
        return buildExcelResponse(data, "beneficiaires.xlsx");
    }

    @GetMapping("/excel/personnel")
    public ResponseEntity<byte[]> exportPersonnelExcel(@AuthenticationPrincipal User user) throws Exception {
        Long provinceId = getProvinceFilter(user);
        byte[] data = excelService.exportPersonnel(provinceId);
        return buildExcelResponse(data, "personnel.xlsx");
    }

    @GetMapping("/excel/etablissements")
    public ResponseEntity<byte[]> exportEtablissementsExcel(@AuthenticationPrincipal User user) throws Exception {
        Long provinceId = getProvinceFilter(user);
        byte[] data = excelService.exportEtablissements(provinceId);
        return buildExcelResponse(data, "etablissements.xlsx");
    }

    // ---- PDF exports ----

    @GetMapping("/pdf/beneficiaires")
    public ResponseEntity<byte[]> exportBeneficiairesPdf(@AuthenticationPrincipal User user) throws Exception {
        Long provinceId = getProvinceFilter(user);
        byte[] data = pdfService.exportBeneficiairesPdf(provinceId);
        return buildPdfResponse(data, "beneficiaires.pdf");
    }

    @GetMapping("/pdf/personnel")
    public ResponseEntity<byte[]> exportPersonnelPdf(@AuthenticationPrincipal User user) throws Exception {
        Long provinceId = getProvinceFilter(user);
        byte[] data = pdfService.exportPersonnelPdf(provinceId);
        return buildPdfResponse(data, "personnel.pdf");
    }

    @GetMapping("/pdf/beneficiaire/{id}")
    public ResponseEntity<byte[]> exportBeneficiaireFiche(@PathVariable Long id) throws Exception {
        Beneficiaire b = beneficiaireRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bénéficiaire non trouvé"));
        byte[] data = pdfService.exportBeneficiaireFichePdf(b);
        return buildPdfResponse(data, "fiche-beneficiaire-" + id + ".pdf");
    }

    private Long getProvinceFilter(User user) {
        if (user.getProvince() != null &&
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELEGUE"))) {
            return user.getProvince().getId();
        }
        return null;
    }

    private ResponseEntity<byte[]> buildExcelResponse(byte[] data, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    private ResponseEntity<byte[]> buildPdfResponse(byte[] data, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(data);
    }
}
