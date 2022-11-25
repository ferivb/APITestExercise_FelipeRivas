package tests;

import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.models.BankTransaction;
import util.models.ResponsePOJO;
import util.test.BaseTest;

import java.util.List;

public class AccountNumberUpdateTest extends BaseTest {

    @BeforeMethod
    public void populateEndpoint(){
        ResponsePOJO response = getResponse();
        Assert.assertEquals(response.getStatusCode(), 200);

        if (response.getBody().size() < 10){
            List<BankTransaction> pojos = generatePOJO(10);
            for (BankTransaction i: pojos){
                Assert.assertEquals(postToAPI(pojoSerializer(i)),201);
            }
        }
    }

    @Test
    public void fourthTest(){
        Faker faker = new Faker();

        log.info("Updating the account number of a random Endpoint");
        int endpoint = faker.number().numberBetween(1,10);
        Assert.assertEquals(updateAccountNumberById(endpoint, 5555555), 200);
    }
}
