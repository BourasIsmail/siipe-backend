package ma.entraide.siipe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.entraide.siipe.entity.Region;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.enums.Role;
import ma.entraide.siipe.repository.RegionRepository;
import ma.entraide.siipe.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createAdminIfNotExists();
        createRegionsIfNotExists();
    }

    private void createAdminIfNotExists() {
        if (!userRepository.existsByEmail("admin@entraide.ma")) {
            User admin = User.builder()
                    .nom("Admin")
                    .prenom("SIIPE")
                    .email("admin@entraide.ma")
                    .password(passwordEncoder.encode("Admin@2024"))
                    .role(Role.ROLE_ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
            log.info("✓ Admin user created: admin@entraide.ma / Admin@2024");
        }
    }

    private void createRegionsIfNotExists() {
        if (regionRepository.count() == 0) {
            String[][] regions = {
                    {"Tanger-Tétouan-Al Hoceïma", "طنجة تطوان الحسيمة", "TTA"},
                    {"L'Oriental", "الشرق", "ORI"},
                    {"Fès-Meknès", "فاس مكناس", "FMK"},
                    {"Rabat-Salé-Kénitra", "الرباط سلا القنيطرة", "RSK"},
                    {"Béni Mellal-Khénifra", "بني ملال خنيفرة", "BMK"},
                    {"Casablanca-Settat", "الدار البيضاء سطات", "CST"},
                    {"Marrakech-Safi", "مراكش آسفي", "MSF"},
                    {"Drâa-Tafilalet", "درعة تافيلالت", "DTF"},
                    {"Souss-Massa", "سوس ماسة", "SOM"},
                    {"Guelmim-Oued Noun", "كلميم واد نون", "GON"},
                    {"Laâyoune-Sakia El Hamra", "العيون الساقية الحمراء", "LSH"},
                    {"Dakhla-Oued Ed-Dahab", "الداخلة وادي الذهب", "DOD"}
            };

            for (String[] r : regions) {
                regionRepository.save(Region.builder()
                        .nomFr(r[0])
                        .nomAr(r[1])
                        .code(r[2])
                        .build());
            }
            log.info("✓ {} regions created", regions.length);
        }
    }
}
