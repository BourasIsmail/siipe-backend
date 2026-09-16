package ma.entraide.siipe.controller;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.service.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<byte[]> serveFile(@RequestParam String path) {
        byte[] data = fileStorageService.loadFile(path);
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(Paths.get(path));
            if (contentType == null) contentType = "application/octet-stream";
        } catch (Exception ignored) {}

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(data);
    }
}