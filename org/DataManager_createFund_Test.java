import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_createFund_Test {

    /*
     * This is a test class for the DataManager.createFund method.
     * Add more tests here for this method as needed.
     *
     * When writing tests for other methods, be sure to put them into separate
     * JUnit test classes.
     */

    @Test
    public void testSuccessfulCreation() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";

            }

        });


        Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);

        assertNotNull(f);
        assertEquals("this is the new fund", f.getDescription());
        assertEquals("12345", f.getId());
        assertEquals("new fund", f.getName());
        assertEquals(10000, f.getTarget());

    }

    /*
     * If status is not "success" then fund should be null
     */
    @Test(expected = IllegalStateException.class)
    public void testUnSuccessfulCreation() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":{\"_id\":\"12345\",\"name\":\"new fund\",\"description\":\"this is the new fund\",\"target\":10000,\"org\":\"5678\",\"donations\":[],\"__v\":0}}";

            }

        });


//        Fund f =
        dm.createFund("12345", "new fund", "this is the new fund", 10000);

//        assertNull(f); // Since the status response is error then this fund should be null

    }

    /*
     * Should cover the Exception thrown code
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionThrown() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new IllegalArgumentException();
            }

        });


//        Fund f =
        dm.createFund("12345", "new fund", "this is the new fund", 10000);

//        assertNull(f); // Since the status response is error then this fund should be null

    }

    @Test
    public void testGenericExceptionThrown() {

        DataManager dm = new DataManager(new WebClient("localhost", 3001) {

            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException();
            }

        });
        Fund f = dm.createFund("12345", "new fund", "this is the new fund", 10000);
        assertNull(f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOrgIdIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"login\"}";
            }
        });
        dm.createFund(null, "new fund", "this is the new fund", 10000);
    }

    @Test(expected = IllegalStateException.class)
    public void testWebClientIsNull() {
        DataManager dm = new DataManager(null);
        dm.createFund("12345", "new fund", "this is the new fund", 10000);
    }

    @Test(expected = IllegalStateException.class)
    public void testResponseFromWebClientIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.createFund("12345", "new fund", "this is the new fund", 10000);
    }

    @Test(expected = IllegalStateException.class)
    public void testResponseFromWebClientIsMalformed() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "not_json";
            }
        });
        dm.createFund("12345", "new fund", "this is the new fund", 10000);
    }
}
