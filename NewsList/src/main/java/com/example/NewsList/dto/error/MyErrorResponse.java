package com.example.NewsList.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class MyErrorResponse {

    private HttpStatus httpStatus;
    private LocalDateTime dateTime;
    private List<String> errorMessages;

}
