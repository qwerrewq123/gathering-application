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

        public static SignInResponse of(String code, String message, String accessToken) {
            return new SignInResponse(code, message, accessToken);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SignUpResponse {

        private final String code;
        private final String message;

        public static SignUpResponse of(String code, String message){
            return new SignUpResponse(code, message);
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class NicknameCheckResponse {

        private final String code;
        private final String message;

        public static NicknameCheckResponse of(String code, String message){
            return new NicknameCheckResponse(code, message);
        }

    }

    @Builder
    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LogOutResponse {

        private String code;
        private String message;

        public static LogOutResponse of(String code, String message){
            return new LogOutResponse(code, message);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class IdCheckResponse {

        private final String code;
        private final String message;

        public static IdCheckResponse of(String code,String message){
            return new IdCheckResponse(code,message);
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class GenerateTokenResponse {
        private String code;
        private String message;
        private String accessToken;

        public static GenerateTokenResponse of(String code, String message, String accessToken){
            return new GenerateTokenResponse(code,message,accessToken);
        }


    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EmailCertificationResponse {

        private String code;
        private String message;

        public static EmailCertificationResponse of(String code, String message){
            return new EmailCertificationResponse(code,message);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CertificationCheckResponse {

        private String code;
        private String message;

        public static CertificationCheckResponse of(String code, String message){
            return new CertificationCheckResponse(code,message);
        }
    }
}
