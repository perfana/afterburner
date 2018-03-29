package nl.stokpop.afterburner.error;

public class ErrorMessage {
    private final String developerMessage;
    private final String userMessage;
    private final int errorCode;

    public ErrorMessage(final String developerMessage, final String userMessage, final int errorCode) {
        this.developerMessage = developerMessage;
        this.userMessage = userMessage;
        this.errorCode = errorCode;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
