package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import util.models.ResponsePOJO;
import util.test.BaseTest;

public class DuplicatedEmailsTest extends BaseTest {

    @Test
    public void thirdTest(){

        log.info("Making the GET request");
        ResponsePOJO response = getResponse();
        Assert.assertEquals(response.getStatusCode(), 200);

        log.info("Validating that there are no duplicated emails");
        Assert.assertFalse(areThereDuplicatedEmails(response.getBody()));
    }

}
