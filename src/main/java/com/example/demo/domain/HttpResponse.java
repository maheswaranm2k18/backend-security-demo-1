package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {
	
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "America/New_York")
    private Date timeStamp;
    
    @Min(value = 1, message = "ID must be greater than or equal to 1")
    @Max(value = Integer.MAX_VALUE, message = "ID must be less than or equal to " + Integer.MAX_VALUE)
    private int httpStatusCode; // 200, 201, 400, 500
    
    private HttpStatus httpStatus;
    
    @Size(min = 3, max = 300, message="UserId length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[A-Za-z0-9]{3,300}$", message = "UserId must consist of alphabets characters and be between 3 and 15 characters.")
    private String reason;
    
    @Size(min = 3, max = 300, message="UserId length must be between 3 to 15 characters.")
    @Pattern(regexp = "^[A-Za-z0-9]{3,300}$", message = "UserId must consist of alphabets characters and be between 3 and 15 characters.")
    private String message;
    
    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.timeStamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
    }
}
