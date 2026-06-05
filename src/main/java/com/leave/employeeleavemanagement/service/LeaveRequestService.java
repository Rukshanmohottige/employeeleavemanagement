package com.leave.employeeleavemanagement.service;

import com.leave.employeeleavemanagement.dto.LeaveRequestDTO;
import java.util.List;

public interface LeaveRequestService {
    LeaveRequestDTO applyLeave(LeaveRequestDTO leaveRequestDTO);
    List<LeaveRequestDTO> getAllLeaveRequests();
    LeaveRequestDTO getLeaveRequestById(Long id);
    LeaveRequestDTO updateLeaveStatus(Long id, String status); // APPROVED හෝ REJECTED කිරීමට
    void deleteLeaveRequest(Long id);
}