package utilities;

import java.security.SecureRandom;
import java.util.Random;

import configuration.TestConfig;

public class PasswordUtils extends TestConfig {
	
	static char[] SYMBOLS = (new String("^$*.[]{}()?-\"!@#%&/\\,><':;|_~`")).toCharArray();
    static char[] LOWERCASE = (new String("abcdefghijklmnopqrstuvwxyz")).toCharArray();
    static char[] UPPERCASE = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ")).toCharArray();
    static char[] NUMBERS = (new String("0123456789")).toCharArray();
    static char[] ALL_CHARS = (new String("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`")).toCharArray();
    static Random rand = new SecureRandom();

    /**
     * Generates a random password with the following criteria:
     * <li>At least one UPPER case character
     * <li>At least one lower case character
     * <li>At least one number
     * <li>At least one special character
     * 
     * @param length
     * @return String
     */
    public static String generateRandomPassword(int length) {
        assert length >= 4;
        char[] password = new char[length];

        //get the requirements out of the way
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        //populate rest of the password with random chars
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        //shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }

        return new String(password);
    }

}
