package com.agutierrezl.matricula_facil_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "enrollments")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Enrollment {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Field
    private LocalDateTime enrollmentDate;

    @Field
    private Student student;

    @Field
    private List<Course> courses;

    @Field
    private boolean status;
}
