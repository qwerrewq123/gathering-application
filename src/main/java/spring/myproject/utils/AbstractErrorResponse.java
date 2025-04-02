package spring.myproject.utils;

import spring.myproject.dto.response.ErrorResponse;

public class AbstractErrorResponse {

    public static ErrorResponse getErrorResponse(String code, String message){
        return ErrorResponse.builder()

                .code(code)
                .message(message)
                .build();
    }


}
