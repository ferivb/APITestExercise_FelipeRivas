package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import util.models.BankTransaction;
import util.test.BaseTest;

import java.util.List;

public class Post10POJOsTest extends BaseTest {

    /**
     * Post test on mockapi endpoint
     * @author Felipe.Rivas
     */
    @Test
    public void secondTest(){

        log.info("Generating the POJOs");
        List<BankTransaction> pojos = generatePOJO(10);

        log.info("Generating duplicated POJOs");
        BankTransaction duplicated1 = new BankTransaction(
                "duppp", "plicated", 123333, 125553.90, "whatever",
                "abc@email.com", true, "Peru", "12345666"
        );
        pojos.add(duplicated1);
        BankTransaction duplicated2 = new BankTransaction(
                "this", "shouldn't post", 122223, 127773.90, "duplication",
                "abc@email.com", false, "Japan", "12553456"
        );
        pojos.add(duplicated2);

        log.info("Removing duplicates");
        List<BankTransaction> pojosTrimmed = duplicatedEmailTrimmer(pojos);

        log.info("Serializing POJOs and posting to mockAPI");
        for (BankTransaction i: pojosTrimmed){
            Assert.assertEquals(postToAPI(pojoSerializer(i)),201);
        }
    }
}
