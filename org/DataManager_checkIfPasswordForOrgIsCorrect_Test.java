import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataManager_checkIfPasswordForOrgIsCorrect_Test {
    @Test
    public void testPasswordForOrgIsCorrect() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"true\"}";
            }
        });
        String response = dm.checkIfPasswordForOrgIsCorrect("6dhe87r9", "pass");
        assertEquals("true", response);
    }

    @Test
    public void testPasswordForOrgIsIncorrect() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"false\"}";
            }
        });
        String response = dm.checkIfPasswordForOrgIsCorrect("6dhe87r9", "pass");
        assertEquals("false", response);
    }

    @Test
    public void testWebClientReturnsError() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        String response = dm.checkIfPasswordForOrgIsCorrect("6dhe87r9", "pass");
        assertEquals("error", response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgumentsGivenToDataManagerMethod() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        dm.checkIfPasswordForOrgIsCorrect(null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedJSONResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "no_json";
            }
        });
        dm.checkIfPasswordForOrgIsCorrect("6dhe87r9", "pass");
    }

    @Test(expected = IllegalStateException.class)
    public void testWebClientIsNull() {
        DataManager dm = new DataManager(null);
        dm.checkIfPasswordForOrgIsCorrect("6dhe87r9", "pass");
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.checkIfPasswordForOrgIsCorrect("6dhe87r9", "pass");
    }

    @Test
    public void testExceptionThrown() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new NullPointerException();
            }
        });
        String response = dm.checkIfPasswordForOrgIsCorrect("6dhe87r9", "pass");
        assertEquals("error", response);
    }
}
