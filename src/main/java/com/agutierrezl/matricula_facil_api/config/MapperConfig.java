package com.agutierrezl.matricula_facil_api.config;

import com.agutierrezl.matricula_facil_api.dto.EnrollmentDTO;
import com.agutierrezl.matricula_facil_api.dto.StudentDTO;
import com.agutierrezl.matricula_facil_api.model.Enrollment;
import com.agutierrezl.matricula_facil_api.model.Student;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean(name = "defaultMapper")
    public ModelMapper defaultMapper(){
        return new ModelMapper();
    }

    @Bean(name = "studentMapper")
    public ModelMapper studentMapper(){
        // return new ModelMapper();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Write (POST)
        mapper.createTypeMap(StudentDTO.class, Student.class)
                .addMapping( StudentDTO::getName, (destination, v ) -> destination.setFirstName( ( String ) v ))
                .addMapping( StudentDTO::getSurname, (destination, v ) -> destination.setLastName( ( String ) v ));

        // Read (GET)
        mapper.createTypeMap(Student.class, StudentDTO.class)
                //.addMapping(student -> student.getFirstName(),((destination, v ) -> destination.setName( (String) v )));
                .addMapping( Student::getFirstName, (destination, v ) -> destination.setName( ( String ) v ))
                .addMapping( Student::getLastName, (destination, v ) -> destination.setSurname( ( String ) v ));

        return mapper;

    }

    @Bean(name = "enrollmentMapper")
    public ModelMapper enrollmentMapper(){
        // return new ModelMapper();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Write (POST)
        mapper.createTypeMap( EnrollmentDTO.class, Enrollment.class)
                .addMapping( enrollmentDto -> enrollmentDto.getStudent().getName(), (destination, v ) -> destination.getStudent().setFirstName(( String ) v))
                .addMapping( enrollmentDto -> enrollmentDto.getStudent().getSurname(), (destination, v ) -> destination.getStudent().setLastName(( String ) v));

        // Read (GET)
        mapper.createTypeMap(Enrollment.class, EnrollmentDTO.class)
                .addMapping( enrollment -> enrollment.getStudent().getFirstName(), (destination, v ) -> destination.getStudent().setName(( String ) v))
                .addMapping( enrollment -> enrollment.getStudent().getLastName(), (destination, v ) -> destination.getStudent().setSurname(( String ) v));

        return mapper;

    }
}
