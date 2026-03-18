package com.Sura.CandidateService.SecurityController;

import com.Sura.CandidateService.DTO.UserResponseDTO;
import com.Sura.CandidateService.Entity.Users;
import com.Sura.CandidateService.Service.JwtService;
import com.Sura.CandidateService.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users/")
@Tag(name="User API's")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Register a new user", description = "Registers a user and returns basic user details")
    @ApiResponse(responseCode = "200", description = "User registered successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody Users users) {
        Users userResponse = userService.register(users);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userResponse.getUserId(), userResponse.getUsername(), userResponse.getPassword());
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all users", description = "Fetch list of all registered users")
    @ApiResponse(responseCode = "200", description = "List of users",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @GetMapping("getAllUsers")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<Users> userResponse = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        for (Users users : userResponse) {
            userResponseDTOList.add(new UserResponseDTO(users.getUserId(), users.getUsername(), users.getPassword()));
        }
        return new ResponseEntity<>(userResponseDTOList, HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID", description = "Fetch user details by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("getUserById/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id) {
        Users userResponse = userService.getUserById(id);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userResponse.getUserId(), userResponse.getUsername(), userResponse.getPassword());
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get user by username", description = "Fetch user details by username")
    @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @GetMapping("getUserByUsername/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable("username") String username) {
        Users userResponse = userService.getUserByUsername(username);
        return new ResponseEntity<>(new UserResponseDTO(userResponse.getUserId(), userResponse.getUsername(), userResponse.getPassword()), HttpStatus.OK);
    }

    @Operation(summary = "Update user", description = "Update user details")
    @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @PutMapping("updateUser")
    public ResponseEntity<UserResponseDTO> updateUser(@Valid @RequestBody Users users) {
        Users userResponse = userService.updateUser(users);
        UserResponseDTO userResponseDTO = new UserResponseDTO(userResponse.getUserId(), userResponse.getUsername(), userResponse.getPassword());
        return new ResponseEntity<>(userResponseDTO, HttpStatus.OK);
    }

    @Operation(summary = "Delete user", description = "Delete user by ID")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }

    @Operation(summary = "Generate JWT token", description = "Generates JWT token for authenticated user")
    @ApiResponse(responseCode = "200", description = "Token generated successfully",
            content = @Content(schema = @Schema(implementation = Map.class)))
    @GetMapping("/token")
    public Map<String, String> token(Authentication authentication) {
        String username = authentication.getName();
        String token = jwtService.generateToken(username);
        return Map.of("token", token);
    }
}
