package com.agutierrezl.matricula_facil_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnrollmentDTO {
    private String id;
    private LocalDateTime enrollmentDate;
    private StudentDTO student;
    private List<CourseDTO> courses;
    private boolean status;

}
