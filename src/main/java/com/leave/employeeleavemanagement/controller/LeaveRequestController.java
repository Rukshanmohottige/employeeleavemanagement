package com.leave.employeeleavemanagement.controller;

import com.leave.employeeleavemanagement.dto.LeaveRequestDTO;
import com.leave.employeeleavemanagement.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    // 1. Apply for Leave (POST)
    @PostMapping
    public ResponseEntity<LeaveRequestDTO> applyLeave(@Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        LeaveRequestDTO createdLeave = leaveRequestService.applyLeave(leaveRequestDTO);
        return new ResponseEntity<>(createdLeave, HttpStatus.CREATED);
    }

    // 2. Get All Leave Requests (GET)
    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    // 3. Get Leave Request By ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestDTO> getLeaveRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestById(id));
    }

    // 4. Approve or Reject Leave (PUT)

    @PutMapping("/{id}/status")
    public ResponseEntity<LeaveRequestDTO> updateLeaveStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(leaveRequestService.updateLeaveStatus(id, status));
    }

    // 5. Delete Leave Request (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.ok("Leave request deleted successfully!");
    }
}