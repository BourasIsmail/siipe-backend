package ma.entraide.siipe.service;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.entity.Province;
import ma.entraide.siipe.entity.Region;
import ma.entraide.siipe.repository.ProvinceRepository;
import ma.entraide.siipe.repository.RegionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeoService {

    private final RegionRepository regionRepository;
    private final ProvinceRepository provinceRepository;

    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }

    public List<Province> getProvincesByRegion(Long regionId) {
        return provinceRepository.findByRegionId(regionId);
    }
}
