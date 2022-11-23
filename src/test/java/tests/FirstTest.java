package tests;

import org.testng.annotations.Test;
import util.test.BaseTest;

public class FirstTest extends BaseTest {

    @Test
    public void firstTest(){
        log.info("Starting test");
        getAllTransactions();
    }
}
