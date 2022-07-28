import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

//    This is a test class for the DataManager.getContributorName method.
public class DataManager_getContributorName_Test {
    @Test
    public void testSuccessfulGetContributorName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\": \"Harsh\"}";
            }
        });
        String name = dm.getContributorName("hasareen");
        assertEquals("Harsh", name);
    }

    @Test
    public void testSuccessfulGetContributorIdToName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\": \"Harsh\"}";
            }
        });
        String name = dm.getContributorName("hasareen");
        assertEquals("Harsh", name);
        String nameAgain = dm.getContributorName("hasareen");
        assertEquals("Harsh", nameAgain);
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "status:bad";
//                return "{\"stat\":\"success\",\"data\": \"Harsh\"}";
            }
        });
//        String name =
        dm.getContributorName("alex");
//        assertNull(name);
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsuccessfulGetContributorName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error5730\"}";
            }
        });
//        String name =
        dm.getContributorName("hasareen");
//        assertNull(name);
    }
    @Test
    public void testGenericException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new RuntimeException();
            }
        });
        String name = dm.getContributorName("hasareen");
        assertNull(name);
    }
}
