package ma.entraide.siipe.controller;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.entity.Province;
import ma.entraide.siipe.entity.Region;
import ma.entraide.siipe.service.GeoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/geo")
@RequiredArgsConstructor
public class GeoController {

    private final GeoService geoService;

    @GetMapping("/regions")
    public ResponseEntity<List<Region>> getAllRegions() {
        return ResponseEntity.ok(geoService.getAllRegions());
    }

    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getAllProvinces() {
        return ResponseEntity.ok(geoService.getAllProvinces());
    }

    @GetMapping("/provinces/region/{regionId}")
    public ResponseEntity<List<Province>> getProvincesByRegion(@PathVariable Long regionId) {
        return ResponseEntity.ok(geoService.getProvincesByRegion(regionId));
    }
}
