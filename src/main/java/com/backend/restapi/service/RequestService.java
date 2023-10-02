package com.backend.restapi.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.restapi.dto.RequestDto;
import com.backend.restapi.models.Request;
import com.backend.restapi.models.RequestRole;
import com.backend.restapi.models.RequestStatus;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.RequestRepository;
import com.backend.restapi.repository.RequestRoleRepository;
import com.backend.restapi.repository.RequestStatusRepository;
import com.backend.restapi.repository.UserRepository;

@Service
public class RequestService {
	private final RequestRepository requestRepository;
	private final RequestRoleRepository requestRoleRepository;
	private final RequestStatusRepository requestStatusRepository;
	private final UserRepository userRepository;

    private final RequestRateLimiterService rateLimiterService;

	@Autowired
	public RequestService(RequestRepository requestRepository, RequestRoleRepository requestRoleRepository,
			RequestStatusRepository requestStatusRepository, UserRepository userRepository,
			RequestRateLimiterService rateLimiterService) {
		this.requestRepository = requestRepository;
		this.requestRoleRepository = requestRoleRepository;
		this.requestStatusRepository = requestStatusRepository;
		this.userRepository = userRepository;
		this.rateLimiterService = rateLimiterService;
	}

	@Transactional
	public void createLandlordRequestForCustomer(int userId, String rentalDetails) {
		RequestRole requestRole = requestRoleRepository.findById(1).orElse(null);
		RequestStatus requestStatus = requestStatusRepository.findById(1).orElse(null);
		UserEntity user = userRepository.findById(userId).orElse(null);
		// 2 request phải cách nhau 72h
		rateLimiterService.checkRateLimit(userId, 72);
		if (requestRole != null && requestStatus != null && user != null) {
			// Kiểm tra xem người dùng có vai trò "LANDLORD" không
			boolean isLandlord = user.getRoles().stream().anyMatch(role -> role.getName().equals("LANDLORD"));

			if (!isLandlord) {
				Request rentalRequest = new Request();
				String timeStamp = setCreatedAtNow();
				rentalRequest.setUser(user);
				rentalRequest.setRequestRole(requestRole);
				rentalRequest.setRequestStatus(requestStatus);
				rentalRequest.setDescription(rentalDetails);
				rentalRequest.setTimeStamp(timeStamp);
				requestRepository.save(rentalRequest);
			} else {
				throw new RuntimeException("Người dùng có vai trò LANDLORD không thể tạo yêu cầu này.");
			}
		} else {
			throw new RuntimeException("Không tìm thấy dữ liệu tương ứng.");
		}
	}

	@Transactional
	public void rejectLandlordRequest(int userId, int requestId) {
	    UserEntity adminUser = userRepository.findById(userId).orElse(null);
	    RequestStatus rejectedStatus = requestStatusRepository.findById(2).orElse(null);
	    Request requestToReject = requestRepository.findById(requestId).orElse(null);
	    boolean isAdmin = adminUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
	    
	    if (adminUser != null && isAdmin) {
	        if (requestToReject != null) {
	            if (rejectedStatus != null) {
	                requestToReject.setRequestStatus(rejectedStatus);
	                requestRepository.save(requestToReject);
	            } else {
	                throw new RuntimeException("Trạng thái 'Từ chối' không tồn tại.");
	            }
	        } else {
	            throw new RuntimeException("Yêu cầu không tồn tại.");
	        }
	    } else {
	        throw new RuntimeException("Chỉ người dùng có vai trò ADMIN mới có quyền từ chối yêu cầu.");
	    }
	}

    public List<RequestDto> getAllRequestDtos() {
        List<Request> requests = requestRepository.findAll();
        List<RequestDto> requestDtos = new ArrayList<>();
        for (Request request : requests) {
            RequestDto requestDto = new RequestDto();
            requestDto.setId(request.getId());
            requestDto.setRequestRole(request.getRequestRole().getName());
            if(request.getRequestStatus().getName().equals("Từ chối")) {
            requestDto.setRequestStatus("Đã từ chối");
            } if (request.getRequestStatus().getName().equals("Thông qua")) {
            	requestDto.setRequestStatus("Được thông qua");
			} if(request.getRequestStatus().getName().equals("Hủy bỏ")) {
				requestDto.setRequestStatus("Đã bị hủy");
			} if(request.getRequestStatus().getName().equals("Chờ xử lý")) {
				requestDto.setRequestStatus("Chờ xét duyệt");
			}
            requestDto.setDescription(request.getDescription());
            requestDto.setUsername(request.getUser().getUsername());
            requestDto.setTimeStamp(request.getTimeStamp());
            requestDtos.add(requestDto);
        }
        return requestDtos;
    }
	
	public String setCreatedAtNow() {
		Date currentTime = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String formattedTime = dateFormat.format(currentTime);
		return formattedTime;
	}
}
