package ma.entraide.siipe.controller;

import lombok.RequiredArgsConstructor;
import ma.entraide.siipe.dto.response.DashboardResponse;
import ma.entraide.siipe.entity.User;
import ma.entraide.siipe.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(@AuthenticationPrincipal User user) {
        // DELEGUE sees only their province stats
        if (user.getProvince() != null &&
                user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DELEGUE"))) {
            return ResponseEntity.ok(dashboardService.getDashboardByProvince(user.getProvince().getId()));
        }
        return ResponseEntity.ok(dashboardService.getGlobalDashboard());
    }

    @GetMapping("/province/{provinceId}")
    public ResponseEntity<DashboardResponse> getDashboardByProvince(@PathVariable Long provinceId) {
        return ResponseEntity.ok(dashboardService.getDashboardByProvince(provinceId));
    }
}
