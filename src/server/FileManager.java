public class AdminService {

    private static final String ADMIN_SECRET = "admin123";

    public boolean isAdmin(String message) {
        return message.startsWith("ADMIN|");
    }

    public boolean validate(String secret) {
        return ADMIN_SECRET.equals(secret);
    }
}