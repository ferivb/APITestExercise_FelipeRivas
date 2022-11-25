package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.models.ResponsePOJO;
import util.test.BaseTest;

public class EmptyEndpointTest extends BaseTest {

    /**
     * Emptying the endpoint before test
     * @author Felipe.Rivas
     */
    @BeforeMethod
    public void emptyEndpoint(){
        ResponsePOJO response = getResponse();
        cleanTheEndpoint(response);
    }

    /**
     * Get test, empty endpoint validation
     * @author Felipe.Rivas
     */
    @Test
    public void firstTest(){

        log.info("Starting test");

        log.info("Getting the data from the endpoint");
        ResponsePOJO response = getResponse();
        Assert.assertEquals(response.getStatusCode(),200);

        log.info("Validating if the Endpoint is empty");
        Assert.assertEquals(response.getBody().size(), 0);

    }
}
