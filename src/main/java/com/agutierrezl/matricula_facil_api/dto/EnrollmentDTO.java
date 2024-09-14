package com.agutierrezl.matricula_facil_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private LocalDateTime enrollmentDate;
    @NotNull
    private StudentDTO student;
    @NotNull
    private List<EnrollmentDetailDTO> items;
    private boolean status;
}
