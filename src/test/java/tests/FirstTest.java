package tests;

import org.testng.annotations.Test;
import util.test.BaseTest;

public class FirstTest extends BaseTest {

    @Test
    public void firstTest(){
        log.info("Starting test");
        System.out.println(getAllTransactions().size());
        log.info("Deleting all endpoints");
        cleanTheEndpoint();
        System.out.println(getAllTransactions().size());
    }
}
