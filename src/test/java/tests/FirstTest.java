package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import util.pojo.BankTransaction;
import util.test.BaseTest;

import java.util.List;

public class FirstTest extends BaseTest {

    @Test
    public void firstTest(){
        log.info("Starting test");
        System.out.println(getAllTransactions().size());
        log.info("Deleting all endpoints");
        cleanTheEndpoint();
        System.out.println(getAllTransactions().size());
        log.info("generating POJOs");
        List<BankTransaction> randomTransactions = generatePOJO(5);
        for (BankTransaction i: randomTransactions) {
            pojoSerializer(i);
        }
        log.info("Generating duplicated emails");
        BankTransaction duplicated1 = new BankTransaction(
                "dup", "plicated", 123, 123.90, "whatever",
                "abc@email.com", true, "Peru", "123456"
        );
        randomTransactions.add(duplicated1);
        BankTransaction duplicated2 = new BankTransaction(
                "dup", "plicated", 123, 123.90, "whatever",
                "abc@email.com", true, "Peru", "123456"
        );
        randomTransactions.add(duplicated2);
        log.info("Trimming duplicated emails");
        List<BankTransaction> transactions = duplicateEmailTrimmer(randomTransactions);
        for (BankTransaction i: transactions) {
            pojoSerializer(i);
        }
        log.info("Posting to mockapi");
        for (BankTransaction i: transactions){
            Assert.assertEquals(postToAPI(pojoSerializer(i)),201);
        }
        log.info("New get request");
        Assert.assertFalse(areThereDuplicateEmails(getAllTransactions()));
        log.info("Checking if put works");
        Assert.assertEquals(updateAccountNumberById(2, 666), 200);
    }
}
