package util.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import util.pojo.BankTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            response.then().assertThat().statusCode(HttpStatus.SC_OK);
            return transactionList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanTheEndpoint(){

        List<BankTransaction> allTransactions = getAllTransactions();

        if (allTransactions.size() > 0){
            for (BankTransaction i : allTransactions){
                deleteEndpoint(i.getId());
            }
        }
    }

    public int deleteEndpoint(int id){

        String endPoint = url + "/" + id;

        Response response = given().contentType("application/json").when().delete(endPoint);
        return response.getStatusCode();
    }

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

    public List<BankTransaction> duplicateEmailTrimmer(List<BankTransaction> randomTransactions){
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

    public String pojoSerializer(BankTransaction transaction){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(transaction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int postToAPI(String transaction){
        Response response = given().contentType("application/json").body(transaction).when().post(url);
        return response.getStatusCode();
    }

    public boolean areThereDuplicateEmails(List<BankTransaction> transactions){
        List<String> emails = new ArrayList<>();
        for (BankTransaction i : transactions){
            emails.add(i.getEmail());
        }
        return emails.stream().distinct().count() < transactions.size();
    }

    public int updateAccountNumberById(int id, int accountNumber){
        String endPoint = url + "/" + id;
        Map<String, Integer> account = new HashMap<String, Integer>();
        account.put("accountNumber", accountNumber);

        Response response = given().contentType("application/json").body(account).when().put(endPoint);
        return response.getStatusCode();
    }

}
