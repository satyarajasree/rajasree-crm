package com.rajasreeit.backend.service;

import com.rajasreeit.backend.dto.EnquiryWithEmployeeName;
import com.rajasreeit.backend.entities.CrmAdmin;
import com.rajasreeit.backend.entities.CrmEmployee;
import com.rajasreeit.backend.entities.crmEmployeeEntities.*;
import com.rajasreeit.backend.repo.CrmAdminRepo;
import com.rajasreeit.backend.repo.crmEmployeeRepos.EnquiryRepo;
import com.rajasreeit.backend.repo.crmEmployeeRepos.LeavesRepo;
import com.rajasreeit.backend.repo.crmEmployeeRepos.PunchActivityRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CrmAdminService {

    @Autowired
    private CrmAdminRepo crmAdminRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LeavesRepo leavesRepo;

    @Autowired
    private PunchActivityRepo punchActivityRepo;

    @Autowired
    private EnquiryRepo enquiryRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public CrmAdmin register(CrmAdmin crmAdmin){
        crmAdmin.setPassword(encoder.encode(crmAdmin.getPassword()));
        return crmAdminRepo.save(crmAdmin);
    }

    public String verify(CrmAdmin crmAdmin){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(crmAdmin.getUsername(), crmAdmin.getPassword()));

        if(authentication.isAuthenticated()){
            return jwtService.generateToken(crmAdmin.getUsername(), "ADMIN");
        }
        return  "fail";
    }

    public List<LeaveWithEmployeeName> getLeaves(LeavesEnum status) {
        List<CrmLeaves> leavesList = leavesRepo.findByLeavesEnum(status);

        // Return a list of custom objects containing the relevant fields
        return leavesList.stream()
                .map(leave -> {
                    LeaveWithEmployeeName leaveDetails = new LeaveWithEmployeeName();
                    leaveDetails.setStartDate(leave.getStartDate());
                    leaveDetails.setEndDate(leave.getEndDate());
                    leaveDetails.setReason(leave.getReason());
                    leaveDetails.setLeavesEnum(leave.getLeavesEnum());
                    leaveDetails.setLeaveType(leave.getLeaveType());
                    leaveDetails.setLeaveDay(leave.getLeaveDay());
                    leaveDetails.setId(leave.getId());

                    // Access and set employee's name
                    CrmEmployee crmEmployee = leave.getCrmEmployee();
                    if (crmEmployee != null) {
                        leaveDetails.setEmployeeName(crmEmployee.getFullName());
                    }

                    return leaveDetails;
                })
                .collect(Collectors.toList());
    }

    public CrmLeaves updateLeavesEnum(int leaveId, LeavesEnum newStatus) {
        Optional<CrmLeaves> optionalLeave = leavesRepo.findById(leaveId);
        if (optionalLeave.isPresent()) {
            CrmLeaves crmLeave = optionalLeave.get();
            crmLeave.setLeavesEnum(newStatus);
            return leavesRepo.save(crmLeave);
        } else {
            throw new RuntimeException("Leave with ID " + leaveId + " not found.");
        }
    }


    public List<CrmPunchActivity> getAllPunchActivities() {
        return punchActivityRepo.findAll();
    }

    public Optional<CrmPunchActivity> getPunchActivityById(int id) {
        return punchActivityRepo.findById(id);
    }

    @Transactional
    public CrmPunchActivity updatePunchActivity(int id, String date, String timeOfPunchIn,
                                                String timeOfPunchOut, MultipartFile punchInImage,
                                                MultipartFile punchOutImage) {
        return punchActivityRepo.findById(id).map(activity -> {
            // Update the punch activity details
            activity.setDate(date);
            activity.setTimeOfPunchIn(timeOfPunchIn);
            activity.setTimeOfPunchOut(timeOfPunchOut);

            // Set images if present
            if (punchInImage != null && !punchInImage.isEmpty()) {
                try {
                    activity.setPunchInImage(punchInImage.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (punchOutImage != null && !punchOutImage.isEmpty()) {
                try {
                    activity.setPunchOutImage(punchOutImage.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            // Save and return the updated activity
            return punchActivityRepo.save(activity);
        }).orElseThrow(() -> new RuntimeException("Punch activity not found with id: " + id));
    }


    public List<EnquiryWithEmployeeName> getAllEnquiries() {
        return enquiryRepo.findAll().stream().map(enquiry -> {
            EnquiryWithEmployeeName dto = new EnquiryWithEmployeeName();
            dto.setId(enquiry.getId());
            dto.setEmployeeName(enquiry.getCrmEmployee().getFullName()); // Assuming CrmEmployee has a getFullName() method
            dto.setTitle(enquiry.getTitle());
            dto.setMessage(enquiry.getMessage());
            return dto;
        }).collect(Collectors.toList());
    }

    public void deleteEnquiry(int id){
        enquiryRepo.deleteById(id);
    }
}
