package ca.bc.gov.educ.api.assessment.util;

public class ThreadLocalStateUtil {
    private static ThreadLocal<String> transaction = new ThreadLocal<>();
    private static ThreadLocal<String> user = new ThreadLocal<>();

    //GRAD2-1929 - Refactoring/Linting
    private ThreadLocalStateUtil() {

    }

    /**
     * Set the current correlationID for this thread
     *
     * @param correlationID - correlation id
     */
    public static void setCorrelationID(String correlationID){
        transaction.set(correlationID);
    }

    /**
     * Get the current correlationID for this thread
     *
     * @return the correlationID, or null if it is unknown.
     */
    public static String getCorrelationID() {
        return transaction.get();
    }

    /**
     * Set the current user for this thread
     *
     * @param currentUser - current user
     */
    public static void setCurrentUser(String currentUser){
        user.set(currentUser);
    }

    /**
     * Get the current user for this thread
     *
     * @return the username of the current user, or null if it is unknown.
     */
    public static String getCurrentUser() {
        return user.get();
    }

    public static void clear() {
        transaction.remove();
        user.remove();
    }
}
