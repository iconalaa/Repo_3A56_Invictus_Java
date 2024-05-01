package controllers.user;
import entities.User;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static Map<String, User> sessions = new HashMap<>();
    private static String lastSessionId;

    public static String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        lastSessionId = sessionId; // Store the last generated session ID
        return sessionId;
    }
    public static User getSession(String sessionId) {
        return sessions.get(sessionId);
    }
    public static void endSession() {
        sessions.clear();
    }
    public static String getLastSessionId() {
        return lastSessionId;
    }
}