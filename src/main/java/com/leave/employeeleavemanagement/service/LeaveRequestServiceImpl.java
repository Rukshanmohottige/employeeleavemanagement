package com.leave.employeeleavemanagement.service;

import com.leave.employeeleavemanagement.dto.LeaveRequestDTO;
import com.leave.employeeleavemanagement.exception.ResourceNotFoundException; // අලුතින් එකතු වුණා
import com.leave.employeeleavemanagement.model.Employee;
import com.leave.employeeleavemanagement.model.LeaveRequest;
import com.leave.employeeleavemanagement.repository.EmployeeRepository;
import com.leave.employeeleavemanagement.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public LeaveRequestDTO applyLeave(LeaveRequestDTO dto) {

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        // Overlapping leaves checking
        List<LeaveRequest> overlapping = leaveRequestRepository.findOverlappingLeaves(
                dto.getEmployeeId(), dto.getStartDate(), dto.getEndDate()
        );
        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Leave application failed: Dates overlap with an existing leave request!");
        }

        LeaveRequest leaveRequest = toEntity(dto);
        leaveRequest.setEmployee(employee);
        leaveRequest.setStatus("PENDING");

        LeaveRequest savedLeave = leaveRequestRepository.save(leaveRequest);
        return toDTO(savedLeave);
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveRequestDTO getLeaveRequestById(Long id) {
        // මෙතන ResourceNotFoundException
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
        return toDTO(leaveRequest);
    }

    @Override
    public LeaveRequestDTO updateLeaveStatus(Long id, String status) {

        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));

        leaveRequest.setStatus(status.toUpperCase());
        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
        return toDTO(updatedLeave);
    }

    @Override
    public void deleteLeaveRequest(Long id) {

        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
        leaveRequestRepository.delete(leaveRequest);
    }

    private LeaveRequestDTO toDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus());
        dto.setEmployeeId(leaveRequest.getEmployee().getId());
        return dto;
    }

    private LeaveRequest toEntity(LeaveRequestDTO dto) {

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());
        leaveRequest.setStatus(dto.getStatus());
        return leaveRequest;
    }
}