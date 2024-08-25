package com.agutierrezl.matricula_facil_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "courses")
public class Course {

    private String id;

    private String name;

    private String code;

    private boolean status;
}
