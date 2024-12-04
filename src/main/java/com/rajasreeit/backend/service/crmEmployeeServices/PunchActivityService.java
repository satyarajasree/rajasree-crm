package com.rajasreeit.backend.service.crmEmployeeServices;

import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.CrmPunchActivity;

import com.rajasreeit.backend.filter.JwtFilter;
import com.rajasreeit.backend.repo.CrmEmployeeRepo;
import com.rajasreeit.backend.repo.crmEmployeeRepos.PunchActivityRepo;
import com.rajasreeit.backend.service.CrmDetailsService;
import com.rajasreeit.backend.service.CrmEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PunchActivityService {

    @Autowired
    private PunchActivityRepo punchActivityRepo;

    @Autowired
    private CrmEmployeeRepo crmEmployeeRepo;

    @Autowired
    private CrmDetailsService crmEmployeeService;

    @Autowired
    private JwtFilter jwtUtil;



    public CrmPunchActivity savePunchActivity(MultipartFile punchInImage, MultipartFile punchOutImage, String mobile) throws IOException {
        // Find employee by mobile number
        CrmEmployee crmEmployee = crmEmployeeRepo.findByMobile(mobile);
        if (crmEmployee == null) {
            throw new RuntimeException("Employee not found");
        }

        // Get current date
        String currentDate = LocalDate.now().toString();

        // Check if a record already exists for today's date
        Optional<CrmPunchActivity> existingPunchOpt = Optional.ofNullable(punchActivityRepo.findByCrmEmployeeAndDate(crmEmployee, currentDate));

        CrmPunchActivity punchActivity;

        if (existingPunchOpt.isPresent()) {
            // Update existing record
            punchActivity = existingPunchOpt.get();

            if (punchInImage != null) {
                if (punchActivity.getTimeOfPunchIn() != null) {
                    throw new IllegalStateException("Employee has already punched in for today.");
                }
                punchActivity.setPunchInImage(punchInImage.getBytes());
                punchActivity.setTimeOfPunchIn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }

            if (punchOutImage != null) {
                if (punchActivity.getTimeOfPunchOut() != null) {
                    throw new IllegalStateException("Employee has already punched out for today.");
                }
                punchActivity.setPunchOutImage(punchOutImage.getBytes());
                punchActivity.setTimeOfPunchOut(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        } else {
            // Create a new record for punch-in
            if (punchOutImage != null) {
                throw new IllegalStateException("Punch-out cannot be recorded before punch-in.");
            }

            punchActivity = new CrmPunchActivity();
            punchActivity.setCrmEmployee(crmEmployee);
            punchActivity.setDate(currentDate);

            if (punchInImage != null) {
                punchActivity.setPunchInImage(punchInImage.getBytes());
                punchActivity.setTimeOfPunchIn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        }

        // Save the punch activity
        return punchActivityRepo.save(punchActivity);
    }

    public List<CrmPunchActivity> getAllPunchActivities() {
        return punchActivityRepo.findAll();
    }

    public Optional<CrmPunchActivity> getPunchActivityById(int id) {
        return punchActivityRepo.findById(id);
    }

    public List<CrmPunchActivity> getPunchActivitiesByMobile(String mobile) {
        // Find employee by mobile number
        CrmEmployee crmEmployee = crmEmployeeRepo.findByMobile(mobile);
        if (crmEmployee == null) {
            throw new RuntimeException("Employee not found for the given mobile number");
        }

        // Fetch punch activities for the employee
        List<CrmPunchActivity> punchActivities = punchActivityRepo.findByCrmEmployee(crmEmployee);
        if (punchActivities.isEmpty()) {
            throw new RuntimeException("No punch activities found for the given mobile number");
        }

        return punchActivities;
    }

}
