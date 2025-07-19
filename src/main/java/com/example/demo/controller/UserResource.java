package com.example.demo.controller;

import com.example.demo.domain.HttpResponse;
import com.example.demo.domain.User;
import com.example.demo.domain.UserPrincipal;
import com.example.demo.exception.ExceptionHandling;
import com.example.demo.exception.domain.*;
import com.example.demo.service.UserService;
import com.example.demo.utility.JWTTokenProvider;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content; 
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.example.demo.constant.FileConstant.*;
import static com.example.demo.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@CrossOrigin(origins = "http://localhost:4200")

//@SecurityScheme(
//	    name = "bearerAuth",
//	    type = SecuritySchemeType.HTTP,
//	    scheme = "bearer",
//	    bearerFormat = "JWT"
//	)
@RestController
@RequestMapping(path = { "/", "/user" })
public class UserResource extends ExceptionHandling {
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserResource(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "No image found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
//    @Operation(operationId = "login", responses = {
//    	    @ApiResponse(responseCode = "200", description = "api_key to be used in the secured-ping endpoint", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDto.class)) }),
//    	    @ApiResponse(responseCode = "401", description = "Unauthorized request", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationExceptionDto.class)) }) })
    @SecurityRequirements()
    public ResponseEntity<User> login(@Validated @RequestBody User user) {
    	System.out.println("Call come....");
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        jwtHeader.add("Authorization", "Bearer " + jwtTokenProvider.generateJwtToken(userPrincipal));
        jwtHeader.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
//        return new ResponseEntity<>(loginUser, jwtHeader, OK);
        if (loginUser != null) {
        	return new ResponseEntity<>(loginUser, jwtHeader, OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @Operation(summary = "Register a new user")
//    @SecurityRequirement(name = "bearer") // Use the name of the security scheme defined globally (e.g., "bearerAuth")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "User registered added"),
//        @ApiResponse(responseCode = "404", description = "User not found"),
//        // Add more API responses as needed
//    })
    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "No image found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @SecurityRequirements()
    public ResponseEntity<User> register(@Validated @RequestBody User user) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
//        return new ResponseEntity<>(newUser, OK);
        if (newUser != null) {
        	return new ResponseEntity<>(newUser, OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	@PostMapping("/add")
	@Operation(summary = "Add a new user", description = "")
//    @SecurityRequirement(name = "bearer", scopes = {"read", "write"}) // Use the name of the security scheme defined globally (e.g., "bearerAuth")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User successfully added", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
			@ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
			@ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
			@ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
			@ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
	// Add more API responses as needed
	})
	public ResponseEntity<User> addNewUser(
			@RequestParam("firstName") @Size(min = 3, max = 15, message = "First name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z]{3,15}$", message = "Name must consist of alphabets only and be between 3 and 15 characters.") String firstName,

			@RequestParam("lastName") @Size(min = 3, max = 15, message = "Last name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z]{3,15}$", message = "Name must consist of alphabets only and be between 3 and 15 characters.") String lastName,

			@RequestParam("username") @Size(min = 3, max = 15, message = "User name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.") String username,

			@RequestParam("email") @NotBlank @Email @Size(min = 3, max = 35, message = "Email length must be between 3 to 35 characters.") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must consist of alphanumeric characters and be between 3 and 35 characters.") String email,

			@RequestParam("role") @Size(min = 3, max = 15, message = "Role length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Z_]{3,15}$", message = "Role must consist of alphabets only and length between 3 and 15 characters.") String role,

			@RequestParam("isActive") @Size(min = 1, max = 15, message = "isActive length must be between 1 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9]$", message = "isActive must consist of alphanumeric characters and be between 1 and 15 characters.") String isActive,

			@RequestParam("isNonLocked") @Size(min = 1, max = 15, message = "isNonLocked length must be between 1 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9]$", message = "Name must consist of alphanumeric characters and be between 1 and 15 characters.") String isNonLocked,
			
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage )
			throws UserNotFoundException, UsernameExistException, EmailExistException, IOException,
			NotAnImageFileException {
		User newUser = userService.addNewUser(firstName, lastName, username, email, role,
				Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
//        return new ResponseEntity<>(newUser, OK);
		if (newUser != null) {
			return new ResponseEntity<>(newUser, OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

    @PostMapping("/update")
    @Operation(summary = "Update a user", description = "")
//    @SecurityRequirement(name = "bearer", scopes = {"read", "write"}) // Use the name of the security scheme defined globally (e.g., "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Update User successfully added", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
        // Add more API responses as needed
    })
    public ResponseEntity<User> update(
    		@RequestParam("currentUsername") @Size(min = 3, max = 15, message = "Customer name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z]{3,15}$", message = "Customer name must consist of alphabets only and be between 3 and 15 characters.") String currentUsername,
    		@RequestParam("firstName") @Size(min = 3, max = 15, message = "First name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z]{3,15}$", message = "Name must consist of alphabets only and be between 3 and 15 characters.") String firstName,
    		@RequestParam("lastName") @Size(min = 3, max = 15, message = "Last name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z]{3,15}$", message = "Name must consist of alphabets only and be between 3 and 15 characters.") String lastName,
    		@RequestParam("username") @Size(min = 3, max = 15, message = "User name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.") String username,
    		@RequestParam("email") @NotBlank @Email @Size(min = 3, max = 35, message = "Email length must be between 3 to 35 characters.") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must consist of alphanumeric characters and be between 3 and 35 characters.") String email,
    		@RequestParam("role") @Size(min = 3, max = 15, message = "Role length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Z_]{3,15}$", message = "Role must consist of alphabets only and length between 3 and 15 characters.") String role,
    		@RequestParam("isActive") @Size(min = 1, max = 15, message = "isActive length must be between 1 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9]$", message = "isActive must consist of alphanumeric characters and be between 1 and 15 characters.") String isActive,
    		@RequestParam("isNonLocked") @Size(min = 1, max = 15, message = "isNonLocked length must be between 1 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9]$", message = "Name must consist of alphanumeric characters and be between 1 and 15 characters.") String isNonLocked,
    		@RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User updatedUser = userService.updateUser(currentUsername, firstName, lastName, username,email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
//        return new ResponseEntity<>(updatedUser, OK);
        if (updatedUser != null) {
        	return new ResponseEntity<>(updatedUser, OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/find/{username}")
    @Operation(summary = "Find by user name", description = "")
//    @SecurityRequirement(name = "bearer", scopes = {"read", "write"}) // Use the name of the security scheme defined globally (e.g., "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User finded successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
        // Add more API responses as needed
    })
    public ResponseEntity<User> getUser(@PathVariable("username") 
    @Size(min = 3, max = 15, message = "User name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.")
    String username) {
        User user = userService.findUserByUsername(username);
//        return new ResponseEntity<>(user, OK);
        if (user != null) {
        	return new ResponseEntity<>(user, OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list")
//    @ApiResponses(value = {
//    	    @ApiResponse(code = 200, message = "Successfully retrieved users", response = User.class, responseContainer = "List"),
//    	    @ApiResponse(code = 404, message = "No users found")
//    	})
    @Operation(summary = "Get all users", description = "This method return all user in data base")
//    @SecurityRequirement(name = "bearer", scopes = {"read", "write"}) // Use the name of the security scheme defined globally (e.g., "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
        @ApiResponse(responseCode = "404", description = "No users found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    public ResponseEntity<List<User>> getAllUsers() {
    	System.out.println("Call come------------------------------");
        List<User> users = userService.getUsers();
//        return new ResponseEntity<>(users, OK);
        if (users != null) {
        	return new ResponseEntity<>(users, OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/resetpassword/{email}")
    @Operation(summary = "Find by user name", description = "")
//    @SecurityRequirement(name = "bearer", scopes = {"read", "write"}) // Use the name of the security scheme defined globally (e.g., "bearerAuth")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User finded successfully"),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
        @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
        // Add more API responses as needed
    })
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email")
    @NotBlank @Email @Size(min = 3, max = 35, message = "Email length must be between 3 to 35 characters.") @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must consist of alphanumeric characters and be between 3 and 35 characters.")
    String email) throws MessagingException, EmailNotFoundException {
        userService.resetPassword(email);
        return response(OK, EMAIL_SENT + email);
    }

//    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAnyAuthority('user:delete')")
//    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("id") long username) throws IOException {
//        userService.deleteUser(username);
//        return response(OK, USER_DELETED_SUCCESSFULLY);
//    }
    
    @DeleteMapping("/delete/{username}")
//    @SecurityRequirement(name = "bearer", scopes = {"read", "write"}) // Use the name of the security scheme defined globally (e.g., "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "No image found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") 
    @Size(min = 3, max = 15, message = "User name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.")
    String username) throws IOException {
        userService.deleteUser(username);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/updateProfileImage")
    @Operation(summary = "Get profile image by username and filename", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "No image found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    public ResponseEntity<User> updateProfileImage(
    		@RequestParam("username") @Size(min = 3, max = 15, message = "User name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.") String username, 
    		@RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        User user = userService.updateProfileImage(username, profileImage);
//        return new ResponseEntity<>(user, OK);
        if (user != null) {
        	return new ResponseEntity<>(user, OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    @Operation(summary = "Get profile image by username and filename", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "No image found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    public byte[] getProfileImage(
    		@PathVariable("username") @Size(min = 3, max = 15, message = "User name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.") String username, 
    		@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(USER_FOLDER + username + FORWARD_SLASH + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    @Operation(summary = "Get profile image by username and filename", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved image", content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)),
            @ApiResponse(responseCode = "404", description = "No image found", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "406", description = "Not Acceptable", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests", content = @Content(schema = @Schema(implementation = HttpResponse.class))),
            @ApiResponse(responseCode = "default", description = "Unexpected error", content = @Content(schema = @Schema(implementation = HttpResponse.class)))
    })
    public byte[] getTempProfileImage(@PathVariable("username")
    @Size(min = 3, max = 15, message = "User name length must be between 3 to 15 characters.") @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.")
    String username) throws IOException {
        URL url = new URL(TEMP_PROFILE_IMAGE_BASE_URL + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        headers.add("Authorization", "Bearer " + jwtTokenProvider.generateJwtToken(user));
        System.out.println("Bearer " + jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
