package com.agutierrezl.matricula_facil_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private String id;

    @NotNull
    @Size(min = 2, max = 20)
    private String nameCourse;

    @NotNull
    @Size(min = 1, max = 10)
    private String codeCourse;

    private boolean statusCourse;

}
