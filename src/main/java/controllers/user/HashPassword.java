package controllers.user;
import org.mindrot.jbcrypt.BCrypt;

public class HashPassword {
    private static final String SALT = BCrypt.gensalt(10);
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, SALT);
    }
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
