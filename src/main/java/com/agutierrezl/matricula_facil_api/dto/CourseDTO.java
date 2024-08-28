package com.agutierrezl.matricula_facil_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private String id;
    private String nameCourse;
    private String codeCourse;
    private boolean statusCourse;

}
