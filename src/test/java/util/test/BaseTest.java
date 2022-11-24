package util.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import util.pojo.BankTransaction;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BaseTest {

    private String url = "https://637b930d6f4024eac211f250.mockapi.io/api/v1/bankTransactions";

    public Logger log = Logger.getLogger(BaseTest.class);


    public List<BankTransaction> getAllTransactions() {
        Response response = given().when().get(url);

        String jsonString = response.asPrettyString();

        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<BankTransaction> transactionList = objectMapper.readValue(jsonString, new TypeReference<List<BankTransaction>>() {});
            return transactionList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanTheEndpoint(){

        List<BankTransaction> allTransactions = getAllTransactions();

        if (allTransactions.size() > 0){
            for (BankTransaction i : allTransactions){
                System.out.println(deleteEndpoint(i.getId()));
            }
        }
    }

    public int deleteEndpoint(int id){

        String endPoint = url + "/" + id;

        Response response = given().contentType("application/json").when().delete(endPoint);
        return response.getStatusCode();
    }
}
