package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Min(value = 1, message = "ID must be greater than or equal to 1")
    @Max(value = Long.MAX_VALUE, message = "ID must be less than or equal to " + Long.MAX_VALUE)
    private Long id;
    
    @Size(min = 3, max = 15, message="UserId length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[A-Za-z0-9]{3,15}$", message = "UserId must consist of alphabets characters and be between 3 and 15 characters.")
    private String userId;
    
    @Size(min = 3, max = 15, message="Name length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[A-Za-z]{3,15}$", message = "Name must consist of alphabets characters and be between 3 and 15 characters.")
    private String firstName;
    
    @Size(min = 3, max = 15, message="Name length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[A-Za-z]{3,15}$", message = "Name must consist of alphabets characters and be between 3 and 15 characters.")
    private String lastName;
    
    @Size(min = 3, max = 15, message="Name length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[A-Za-z0-9@$\\-_]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.")
    private String username;
    
    @Size(min = 3, max = 15, message="Name length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}\\[\\]:;\"'<>,./?\\\\|`~=-]{3,15}$", message = "Name must consist of alphanumeric characters and be between 3 and 15 characters.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    
    @Size(min = 3, max = 35, message="Email length must be between 3 to 35 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must consist of alphanumeric characters and be between 3 and 35 characters.")
    private String email;
    
    @Size(min = 3, max = 150, message="profileImageUrl length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}\\[\\]:;\"'<>,./?\\\\|`~=-]{3,150}$", message = "profileImageUrl must consist of alphanumeric characters and be between 3 and 150 characters.")
    private String profileImageUrl;
    
//    @Size(min = 20, max = 38, message = "Date-time length must be between 20 and 38 characters.")
//    @Pattern(regexp = "^[0-9]{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])T(?:[01][0-9]|2[0-3]):[0-5][0-9]:(?:[0-5][0-9]|60)(?:\\.[0-9]{1,15})?(?:Z|[+\\-](?:[01][0-9]|2[0-3]):[0-5][0-9])$",
//            message = "Invalid date-time format. Expected format: yyyy-MM-dd'T'HH:mm:ss.SSSZ or yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Past(message = "Last login date must be in the past days.")
    private Date lastLoginDate;
    
//    @Size(min = 20, max = 38, message = "Date-time length must be between 20 and 38 characters.")
//    @Pattern(regexp = "^[0-9]{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])T(?:[01][0-9]|2[0-3]):[0-5][0-9]:(?:[0-5][0-9]|60)(?:\\.[0-9]{1,15})?(?:Z|[+\\-](?:[01][0-9]|2[0-3]):[0-5][0-9])$",
//            message = "Invalid date-time format. Expected format: yyyy-MM-dd'T'HH:mm:ss.SSSZ or yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Past(message = "Last login date must be in the past days.")
    private Date lastLoginDateDisplay;
    
//    @Size(min = 20, max = 38, message = "Date-time length must be between 20 and 38 characters.")
//    @Pattern(regexp = "^[0-9]{4}-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])T(?:[01][0-9]|2[0-3]):[0-5][0-9]:(?:[0-5][0-9]|60)(?:\\.[0-9]{1,15})?(?:Z|[+\\-](?:[01][0-9]|2[0-3]):[0-5][0-9])$",
//            message = "Invalid date-time format. Expected format: yyyy-MM-dd'T'HH:mm:ss.SSSZ or yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @FutureOrPresent(message = "Join date must be in the future or the present.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date joinDate;
    
    @Size(min = 3, max = 15, message="Role length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[A-Z_]{3,15}$", message = "Role must consist of alphabets only and length between 3 and 15 characters.")
    private String role; //ROLE_USER{ read, edit }, ROLE_ADMIN {delete}
    
    @Size(max = 100)
    private String[] authorities;
    
    private boolean isActive;
    private boolean isNotLocked;
}
