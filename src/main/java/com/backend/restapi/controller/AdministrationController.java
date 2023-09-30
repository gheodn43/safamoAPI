package com.backend.restapi.controller;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.restapi.models.Role;
import com.backend.restapi.models.UserEntity;
import com.backend.restapi.repository.RoleRepository;
import com.backend.restapi.repository.UserRepository;

@RestController
@RequestMapping("/api/administration")
public class AdministrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // API endpoint để cấp quyền ADMIN cho một người dùng
    @PostMapping("/grantAdminRole")
    public ResponseEntity<String> grantAdminRole(
        @RequestParam("user_id") int userId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Kiểm tra xem người dùng hiện tại có quyền SUPERADMIN không
        if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            
            // Tìm người dùng dựa trên userId
            Optional<UserEntity> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
            	UserEntity user = userOptional.get();
                
                // Kiểm tra xem người dùng đã có quyền ADMIN chưa
                Optional<Role> adminRoleOptional = roleRepository.findByName("LANDLORD");
                if (adminRoleOptional.isPresent()) {
                    Role adminRole = adminRoleOptional.get();
                    
                    // Kiểm tra xem người dùng đã có quyền ADMIN chưa
                    if (!user.getRoles().contains(adminRole)) {
                        user.getRoles().add(adminRole);
                        userRepository.save(user);
                        return ResponseEntity.ok("Đã cấp quyền LANDLORD cho người dùng.");
                    } else {
                        return ResponseEntity.badRequest().body("Người dùng đã có quyền LANDLORD.");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Quyền LANDLORD không tồn tại.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
    }
}

