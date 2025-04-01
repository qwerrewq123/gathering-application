package spring.myproject.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserResponseDto {

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignInResponse {

        private String code;
        private String message;
        private String accessToken;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignUpResponse {

        private final String code;
        private final String message;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NicknameCheckResponse {

        private final String code;
        private final String message;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LogOutResponse {

        private String code;
        private String message;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IdCheckResponse {

        private final String code;
        private final String message;

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GenerateTokenResponse {
        private String code;
        private String message;
        private String accessToken;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmailCertificationResponse {

        private String code;
        private String message;
    }
}
