package sale_server.reponse;

import java.io.Serializable;

public abstract class AbstractReponse implements Serializable {

    private final String statusMessage;
    private final boolean success;

    public AbstractReponse() {
        this.statusMessage = "OK";
        this.success = true;
    }

    public AbstractReponse(String statusMessage, boolean success) {
        this.statusMessage = statusMessage;
        this.success = success;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isSuccess() {
        return success;
    }
}
