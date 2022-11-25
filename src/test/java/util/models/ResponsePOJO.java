package util.models;

import java.util.List;

public class ResponsePOJO {

    int statusCode;
    List<BankTransaction> body;

    public ResponsePOJO(){}

    public ResponsePOJO(int responseCode, List<BankTransaction> body) {
        this.statusCode = responseCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<BankTransaction> getBody() {
        return body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setBody(List<BankTransaction> body) {
        this.body = body;
    }
}
