package com.backend.restapi.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.restapi.dto.RentalRequestDto;
import com.backend.restapi.dto.RequestDto;
import com.backend.restapi.models.PropertyEntity;
import com.backend.restapi.models.Request;
import com.backend.restapi.models.RequestRole;
import com.backend.restapi.models.RequestStatus;
import com.backend.restapi.models.RoomEntity;
import com.backend.restapi.models.User;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.PropertyRepository;
import com.backend.restapi.repository.RequestRepository;
import com.backend.restapi.repository.RequestRoleRepository;
import com.backend.restapi.repository.RequestStatusRepository;
import com.backend.restapi.repository.RoomRepository;
import com.backend.restapi.repository.UserRepository;

@Service
public class RequestService {
	private final RequestRepository requestRepository;
	private final RequestRoleRepository requestRoleRepository;
	private final RequestStatusRepository requestStatusRepository;
	private final UserRepository userRepository;
	private final RequestRateLimiterService rateLimiterService;
	private final RoomRepository roomRepository;
	private final PropertyRepository propertyRepository;
	private final EmailService emailService;

	@Autowired
	public RequestService(RequestRepository requestRepository, RequestRoleRepository requestRoleRepository,
			RequestStatusRepository requestStatusRepository, UserRepository userRepository,
			RequestRateLimiterService rateLimiterService, RoomRepository roomRepository,
			PropertyRepository propertyRepository, EmailService emailService) {
		this.requestRepository = requestRepository;
		this.requestRoleRepository = requestRoleRepository;
		this.requestStatusRepository = requestStatusRepository;
		this.userRepository = userRepository;
		this.rateLimiterService = rateLimiterService;
		this.roomRepository = roomRepository;
		this.propertyRepository = propertyRepository;
		this.emailService = emailService;

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

	@Transactional // y/c thuê
	public void createRentalRequestForCustomer(int userId, int roomId, String duarationTime) {
		RequestRole requestRole = requestRoleRepository.findById(2).orElse(null);
		RequestStatus requestStatus = requestStatusRepository.findById(1).orElse(null);
		RoomEntity room = roomRepository.findById(roomId).orElse(null);
		UserEntity user = userRepository.findById(userId).orElse(null);
		// 2 request phải cách nhau 24h
//		rateLimiterService.checkRateLimit(userId, 24);
		if (requestRole != null && requestStatus != null && user != null) {
			Request rentalRequest = new Request();
			String timeStamp = setCreatedAtNow();
			rentalRequest.setUser(user);
			rentalRequest.setRequestRole(requestRole);
			rentalRequest.setRequestStatus(requestStatus);
			rentalRequest
					.setDescription("Thuê phòng " + room.getRoomName() + "-" + room.getProperty().getPropertyName());
			rentalRequest.setTimeStamp(timeStamp);
			rentalRequest.setRoom(room);
			rentalRequest.setDuarationTime(duarationTime);
			requestRepository.save(rentalRequest);
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

	@Transactional
	public void rejectRentalRequest(int requestId) {
		RequestStatus rejectedStatus = requestStatusRepository.findById(2).orElse(null);
		Request requestToReject = requestRepository.findById(requestId).orElse(null);
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
	}

	@Transactional
	public void acceptLandlordRequest(int userId, int requestId) {
		UserEntity adminUser = userRepository.findById(userId).orElse(null);
		RequestStatus acceptedStatus = requestStatusRepository.findById(3).orElse(null);
		Request requestToAccept = requestRepository.findById(requestId).orElse(null);
		boolean isAdmin = adminUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));

		if (adminUser != null && isAdmin) {
			if (requestToAccept != null) {
				if (acceptedStatus != null) {
					if ("Chờ xử lý".equals(requestToAccept.getRequestStatus().getName())) {
						requestToAccept.setRequestStatus(acceptedStatus);
						requestRepository.save(requestToAccept);

					} else {
						throw new RuntimeException("Không thể chấp nhận yêu cầu với trạng thái hiện tại.");
					}
				} else {
					throw new RuntimeException("Trạng thái 'Đã được chấp nhận' không tồn tại.");
				}
			} else {
				throw new RuntimeException("Yêu cầu không tồn tại.");
			}
		} else {
			throw new RuntimeException("Chỉ người dùng có vai trò ADMIN mới có quyền chấp nhận yêu cầu.");
		}
	}

	@Transactional
	public void acceptRentalRequest(int requestId, String contractLink) {
		RequestStatus acceptedStatus = requestStatusRepository.findById(3).orElse(null);
		Request requestToAccept = requestRepository.findById(requestId).orElse(null);
//	    UserEntity recipient = requestToAccept.getUser();
//	    String recipientUserName = recipient.getUsername();
//	    String recipientEmail = recipient.getEmail();
		if (requestToAccept != null) {
			if (acceptedStatus != null) {
				if ("Chờ xử lý".equals(requestToAccept.getRequestStatus().getName())) {
					requestToAccept.setRequestStatus(acceptedStatus);
					requestRepository.save(requestToAccept);

				} else {
					throw new RuntimeException("Không thể chấp nhận yêu cầu với trạng thái hiện tại.");
				}
			} else {
				throw new RuntimeException("Trạng thái 'Đã được chấp nhận' không tồn tại.");
			}
		} else {
			throw new RuntimeException("Yêu cầu không tồn tại.");
		}
	}

	@Transactional
	public List<RequestDto> getAllRequestDtos() {
		List<Request> requests = requestRepository.findAll();
		List<RequestDto> requestDtos = new ArrayList<>();
		sortByTimestampDescending(requests);
		for (Request request : requests) {
			RequestDto requestDto = new RequestDto();
			requestDto.setId(request.getId());
			requestDto.setRequestRole(request.getRequestRole().getName());
			String requestStatus = mapRequestStatus(request.getRequestStatus().getName());
			requestDto.setRequestStatus(requestStatus);
			requestDto.setDescription(request.getDescription());
			requestDto.setUsername(request.getUser().getUsername());
			requestDto.setUser_id(request.getUser().getUser_id());
			requestDto.setTimeStamp(request.getTimeStamp());
			requestDtos.add(requestDto);
		}
		return requestDtos;
	}

	@Transactional
	public List<RentalRequestDto> getAllRentalRequestForPropertyOwner(int owner_id) {
		UserEntity owner = userRepository.findById(owner_id).orElse(null);
		List<PropertyEntity> properties = propertyRepository.findByOwner(owner);
		List<RentalRequestDto> requestDtos = new ArrayList<>();
		for (PropertyEntity property : properties) {
			List<RoomEntity> rooms = property.getRooms();
			for (RoomEntity room : rooms) {
				List<Request> roomRequests = room.getRequests();
				for (Request request : roomRequests) {
					RentalRequestDto requestDto = new RentalRequestDto();
					requestDto.setId(request.getId());
					requestDto.setUser_id(request.getUser().getUser_id());
					requestDto.setRoom_id(request.getRoom().getId());
					requestDto.setDuarationTime(request.getDuarationTime());
					requestDto.setUsername(request.getUser().getUsername());
					requestDto.setDescription(request.getDescription());
					String requestStatus = mapRequestStatus(request.getRequestStatus().getName());
					requestDto.setRequestStatus(requestStatus);
					requestDto.setTimeStamp(request.getTimeStamp());

					requestDtos.add(requestDto);
				}
			}
		}
		sortByTimestampDescendingFDTO(requestDtos);
		return requestDtos;
	}

	@Transactional
	public List<RentalRequestDto> getAllRentalRequestForRoom(int room_id, int user_id) {
		RoomEntity room = roomRepository.findById(room_id).orElse(null);
		UserEntity adminUser = userRepository.findById(user_id).orElse(null);
		boolean isAdmin = adminUser.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
		int owner_id = room.getProperty().getOwner().getUser_id();
		if (owner_id != user_id && !isAdmin) {
			return new ArrayList<>();
		}
		List<Request> roomRequests = room.getRequests();
		sortByTimestampDescending(roomRequests);
		List<RentalRequestDto> requestDtos = new ArrayList<>();
		for (Request request : roomRequests) {
			RentalRequestDto requestDto = new RentalRequestDto();

			requestDto.setId(request.getId());
			requestDto.setUser_id(request.getUser().getUser_id());
			requestDto.setRoom_id(request.getRoom().getId());
			requestDto.setUsername(request.getUser().getUsername());
			requestDto.setDescription(request.getDescription());
			String requestStatus = mapRequestStatus(request.getRequestStatus().getName());
			requestDto.setRequestStatus(requestStatus);
			requestDto.setTimeStamp(request.getTimeStamp());
			requestDtos.add(requestDto);

		}
		return requestDtos;
	}

	@Transactional
	public List<RentalRequestDto> getAllRequestForSender(int user_id) {
		UserEntity sender = userRepository.findById(user_id).orElse(null);
		List<Request> roomRequests = requestRepository.findByUser(sender);
		sortByTimestampDescending(roomRequests);
		List<RentalRequestDto> requestDtos = new ArrayList<>();
		for (Request request : roomRequests) {
			RentalRequestDto requestDto = new RentalRequestDto();
			if (request.getRequestRole().getId() == 1) {
				requestDto.setRoom_id(0);
			} else {
				requestDto.setRoom_id(request.getRoom().getId());
			}
			requestDto.setId(request.getId());
			requestDto.setUser_id(request.getUser().getUser_id());
			requestDto.setUsername(request.getUser().getUsername());
			requestDto.setDescription(request.getDescription());
			String requestStatus = mapRequestStatus(request.getRequestStatus().getName());
			requestDto.setRequestStatus(requestStatus);
			requestDto.setTimeStamp(request.getTimeStamp());
			requestDto.setRequestRole(request.getRequestRole().getName());
			requestDtos.add(requestDto);

		}
		return requestDtos;
	}

	@Transactional
	public RentalRequestDto getOneRentalReuqest(int requestId) {
		Request rentalRequest = requestRepository.findById(requestId).orElse(null);
		RentalRequestDto requestDto = new RentalRequestDto();
		requestDto.setId(rentalRequest.getId());
		requestDto.setUser_id(rentalRequest.getUser().getUser_id());
		requestDto.setRoom_id(rentalRequest.getRoom().getId());
		requestDto.setProperty_id(rentalRequest.getRoom().getProperty().getPropertyId());
		requestDto.setDescription(rentalRequest.getDescription());
		String requestStatus = mapRequestStatus(rentalRequest.getRequestStatus().getName());
		requestDto.setRequestStatus(requestStatus);
		requestDto.setTimeStamp(rentalRequest.getTimeStamp());
		requestDto.setDuarationTime(rentalRequest.getDuarationTime());
		return requestDto;
	}

	@Transactional
	public void cancelRequest(int userId, int requestId) {
		UserEntity user = userRepository.findById(userId).orElse(null);
		Request requestToCancel = requestRepository.findById(requestId).orElse(null);

		if (user != null && requestToCancel != null) {
			boolean canCancel = user.equals(requestToCancel.getUser())
					|| user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));

			if (canCancel) {
				if ("Chờ xử lý".equals(requestToCancel.getRequestStatus().getName())) {
					RequestStatus cancelledStatus = requestStatusRepository.findById(4).orElse(null);
					if (cancelledStatus != null) {
						requestToCancel.setRequestStatus(cancelledStatus);
						requestRepository.save(requestToCancel);
					} else {
						throw new RuntimeException("Trạng thái 'Hủy bỏ' không tồn tại.");
					}
				} else {
					throw new RuntimeException("Không thể hủy yêu cầu với trạng thái hiện tại.");
				}
			} else {
				throw new RuntimeException("Bạn không có quyền hủy yêu cầu này.");
			}
		} else {
			throw new RuntimeException("Không tìm thấy dữ liệu tương ứng.");
		}
	}

	@Transactional
	public void DeleteRequestWithRoom(int roomId, int userId) {
		RoomEntity room = roomRepository.findById(roomId).orElse(null);
		UserEntity user = userRepository.findById(userId).orElse(null);
		if (room != null && user != null) {
            List<Request> requests = requestRepository.findByRoomAndUser(room, user);
            requestRepository.deleteAll(requests);
        }
	}
	
	@Transactional
	public void DeleteRequestAfterJoinRoom(int requestId) {
		Request request = requestRepository.findById(requestId).orElse(null);
		if (request != null) {
            requestRepository.delete(request);
        }
	}

	private String mapRequestStatus(String statusName) {
		switch (statusName) {
		case "Từ chối":
			return "Đã từ chối";
		case "Thông qua":
			return "Được thông qua";
		case "Hủy bỏ":
			return "Đã bị hủy";
		case "Chờ xử lý":
			return "Chờ xét duyệt";
		default:
			return statusName;
		}
	}

	public String setCreatedAtNow() {
		Date currentTime = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String formattedTime = dateFormat.format(currentTime);
		return formattedTime;
	}

	public static void sortByTimestampDescending(List<Request> requests) {
		Comparator<Request> timestampComparator = (request1, request2) -> {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date1 = dateFormat.parse(request1.getTimeStamp());
				Date date2 = dateFormat.parse(request2.getTimeStamp());
				return date2.compareTo(date1);
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}
		};
		Collections.sort(requests, timestampComparator);
	}

	public static void sortByTimestampDescendingFDTO(List<RentalRequestDto> requests) {
		Comparator<RentalRequestDto> timestampComparator = (request1, request2) -> {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date date1 = dateFormat.parse(request1.getTimeStamp());
				Date date2 = dateFormat.parse(request2.getTimeStamp());
				return date2.compareTo(date1);
			} catch (ParseException e) {
				e.printStackTrace();
				return 0;
			}
		};
		Collections.sort(requests, timestampComparator);
	}
}
