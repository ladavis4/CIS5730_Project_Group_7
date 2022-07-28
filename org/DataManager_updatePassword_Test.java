import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataManager_updatePassword_Test {
    @Test
    public void testSuccessfulUpdate() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }
        });
        String response = dm.updatePassword("byfibu3r89gerfy", "newPass");
        assertEquals("success", response);
    }

    @Test
    public void testUnsuccessfulUpdate() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        String response = dm.updatePassword("byfibu3r89gerfy", "newPass");
        assertEquals("error", response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgumentsGivenToDataManagerMethod() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.updatePassword(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedJSONResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "no_json";
            }
        });
        dm.updatePassword("45d6ft7dbf98", "newPass");
    }

    @Test(expected = IllegalStateException.class)
    public void testWebClientIsNull() {
        DataManager dm = new DataManager(null);
        dm.updatePassword("45d6ft7dbf98", "newPass");
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.updatePassword("45d6ft7dbf98", "newPass");
    }

    @Test
    public void testExceptionThrown() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new NullPointerException();
            }
        });
        String response = dm.updatePassword("45d6ft7dbf98", "newPass");
        assertEquals("error", response);
    }
}
