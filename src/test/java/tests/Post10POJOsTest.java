package tests;

import org.testng.annotations.Test;
import util.models.BankTransaction;
import util.test.BaseTest;

import java.util.List;

public class Post10POJOsTest extends BaseTest {

    @Test
    public void secondTest(){

        log.info("Generating the POJOs");
        List<BankTransaction> pojos = generatePOJO(10);

        log.info("Generating duplicated POJOs");
    }
}
