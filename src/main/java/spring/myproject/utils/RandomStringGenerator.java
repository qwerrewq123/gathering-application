package spring.myproject.utils;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int STRING_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
