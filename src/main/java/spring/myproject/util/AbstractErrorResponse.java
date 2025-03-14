package spring.myproject.util;

import spring.myproject.common.dto.response.ErrorResponse;

import java.util.ArrayList;

public class AbstractErrorResponse {

    public static ErrorResponse getErrorResponse(String code, String message){
        return ErrorResponse.builder()

                .code(code)
                .message(message)
                .build();
    }


}
