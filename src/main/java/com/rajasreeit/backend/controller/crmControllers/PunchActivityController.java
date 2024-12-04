package com.rajasreeit.backend.controller.crmControllers;

import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmPunchActivity;
import com.rajasreeit.backend.service.JwtService;
import com.rajasreeit.backend.service.crmEmployeeServices.PunchActivityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("crm/employee")
public class PunchActivityController {

    @Autowired
    private PunchActivityService punchActivityService;

    @Autowired
    private JwtService jwtUtils;

    @PostMapping("punch")
    public ResponseEntity<?> savePunchActivity(
            @RequestParam(value = "punchInImage", required = false) MultipartFile punchInImage,
            @RequestParam(value = "punchOutImage", required = false) MultipartFile punchOutImage,
            HttpServletRequest request) {

        try {

            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new RuntimeException("JWT Token is missing or invalid");
            }

            String token = authorizationHeader.substring(7);
            String mobileNumber = jwtUtils.extractUsername(token);

            CrmPunchActivity savedPunchActivity = punchActivityService.savePunchActivity(punchInImage, punchOutImage, mobileNumber);
            return ResponseEntity.ok(savedPunchActivity);

        } catch (IllegalStateException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/punch/all")
    public ResponseEntity<List<CrmPunchActivity>> getAllPunchActivities() {
        List<CrmPunchActivity> punchActivities = punchActivityService.getAllPunchActivities();
        return ResponseEntity.ok(punchActivities);
    }


    @GetMapping("/punch/{id}")
    public ResponseEntity<CrmPunchActivity> getPunchActivityById(@PathVariable int id) {
        Optional<CrmPunchActivity> punchActivity = punchActivityService.getPunchActivityById(id);
        return punchActivity.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/punch-activities")
    public ResponseEntity<List<CrmPunchActivity>> getPunchActivities(@RequestParam String mobile) {
        try {
            List<CrmPunchActivity> punchActivities = punchActivityService.getPunchActivitiesByMobile(mobile);
            return ResponseEntity.ok(punchActivities);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
