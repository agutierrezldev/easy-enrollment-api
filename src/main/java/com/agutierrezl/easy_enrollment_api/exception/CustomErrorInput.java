package com.agutierrezl.easy_enrollment_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Data
@ToString
@AllArgsConstructor
public class CustomErrorInput {
    private String field;
    private String message;

    public List<CustomErrorInput> getErrors(String error) {

        String regex = "CustomErrorInput\\(field=(.*?), message=(.*?)\\)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(error);

        List<CustomErrorInput> errorList = new ArrayList<>();

        while (matcher.find()){
            String field = matcher.group(1);
            String message = matcher.group(2);
            errorList.add(new CustomErrorInput(field,message));
        }

        return errorList;
    }
}
