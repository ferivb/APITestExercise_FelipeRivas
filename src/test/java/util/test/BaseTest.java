package util.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import util.models.BankTransaction;
import util.models.ResponsePOJO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Base class for all tests
 * @author felipe.rivas
 */
public class BaseTest {

    private String url = "https://637b930d6f4024eac211f250.mockapi.io/api/v1/bankTransactions";

    public Logger log = Logger.getLogger(BaseTest.class);

    /**
     * Cleans the endpoint given it is not already empty
     * @param response ResponsePOJO
     * @author Felipe.Rivas
     */
    public void cleanTheEndpoint(ResponsePOJO response){
        List<BankTransaction> allTransactions = response.getBody();

        if (allTransactions.size() > 0){
            for (BankTransaction i : allTransactions){
                deleteEndpoint(i.getId());
            }
        }
    }

    /**
     * Stores the get response body and status code in a POJO
     * @return ResponsePOJO class instance
     * @author Felipe.Rivas
     */
    public ResponsePOJO getResponse(){
        Response response = given().get(url);
        int statusCode = response.then().extract().statusCode();
        List<BankTransaction> body = getAllTransactions(response);
        return new ResponsePOJO(statusCode, body);
    }


    /**
     * Deserializes the body of a get request
     * @param response Response
     * @return deserialized body in a POJO list: List<BankTransaction>
     * @author Felipe.Rivas
     */
    public List<BankTransaction> getAllTransactions(Response response) {
        String jsonString = response.asPrettyString();

        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<BankTransaction> transactionList = objectMapper.readValue(jsonString, new TypeReference<>() {
            });
            return transactionList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Makes a delete request to the given endpoint
     * @param id int
     * @return statusCode int
     * @author Felipe.Rivas
     */
    public int deleteEndpoint(int id){

        String endPoint = url + "/" + id;

        Response response = given().contentType("application/json").when().delete(endPoint);
        return response.getStatusCode();
    }

    /**
     * Generates a provided amount of POJOs
     * @param amountToGenerate int
     * @return deserialized list of POJOs
     * @author Felipe.Rivas
     */
    public List<BankTransaction> generatePOJO(int amountToGenerate){
        Faker faker = new Faker();
        List<BankTransaction> randomTransactions = new ArrayList<>();

        if (amountToGenerate > 0){
            for (int i = 0; i < amountToGenerate; i++){

                String name = faker.name().firstName();
                String lastName = faker.name().lastName();
                int accountNumber = faker.number().numberBetween(1000000,9999999);
                double amount = faker.number().randomDouble(2, 1,10000);
                String transactionType = faker.options().option("withdrawal","payment","invoice","deposit");
                String email = faker.internet().emailAddress();
                boolean active = faker.bool().bool();
                String country = faker.address().country();
                String telephone = faker.phoneNumber().phoneNumber();

                BankTransaction newTransaction = new BankTransaction(
                        name, lastName, accountNumber, amount, transactionType, email, active, country, telephone
                );
                randomTransactions.add(newTransaction);
            }
        }
        return randomTransactions;
    }

    /**
     * Removes POJOs with a duplicated email address
     * @param randomTransactions List
     * @return List
     * @author Felipe.Rivas
     */
    public List<BankTransaction> duplicatedEmailTrimmer(List<BankTransaction> randomTransactions){
        List<BankTransaction> trimmedTransactions = new ArrayList<>();
        List<String> emails = new ArrayList<>();

        trimmedTransactions.add(randomTransactions.get(0));
        emails.add(randomTransactions.get(0).getEmail());

        for (int i = 1; i < randomTransactions.size(); i++){
          if (!emails.contains(randomTransactions.get(i).getEmail())){
              trimmedTransactions.add(randomTransactions.get(i));
              emails.add(randomTransactions.get(i).getEmail());
          }
        }
        return trimmedTransactions;
    }

    /**
     * Serializes a POJO into a JSON String
     * @param transaction BankTransaction POJO
     * @return JSON String
     * @author Felipe.Rivas
     */
    public String pojoSerializer(BankTransaction transaction){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(transaction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Makes a post request
     * @param transaction String
     * @return statusCode int
     * @author Felipe.Rivas
     */
    public int postToAPI(String transaction){
        Response response = given().contentType("application/json").body(transaction).when().post(url);
        return response.getStatusCode();
    }

    /**
     * Confirms if duplicated emails were found
     * @param transactions List
     * @return boolean
     * @author Felipe.Rivas
     */
    public boolean areThereDuplicatedEmails(List<BankTransaction> transactions){
        List<String> emails = new ArrayList<>();
        for (BankTransaction i : transactions){
            emails.add(i.getEmail());
        }
        return emails.stream().distinct().count() < transactions.size();
    }

    /**
     * Makes a put request updating the account number of a given endpoint
     * @param id int
     * @param accountNumber int
     * @return statusCode int
     * @author Felipe.Rivas
     */
    public int updateAccountNumberById(int id, int accountNumber){
        String endPoint = url + "/" + id;
        Map<String, Integer> account = new HashMap<String, Integer>();
        account.put("accountNumber", accountNumber);

        Response response = given().contentType("application/json").body(account).when().put(endPoint);
        return response.getStatusCode();
    }

}
