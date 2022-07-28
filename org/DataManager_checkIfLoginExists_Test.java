import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataManager_checkIfLoginExists_Test {
    @Test
    public void testLoginExists() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"found\"}";
            }
        });
        String response = dm.checkIfLoginExists("62cd937gfyurgfo734");
        assertEquals("found", response);
    }

    @Test
    public void testLoginDoesNotExist() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"not found\"}";
            }
        });
        String response = dm.checkIfLoginExists("62cd937gfyurgfo734");
        assertEquals("not found", response);
    }

    @Test
    public void testErrorInDatabase() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        String response = dm.checkIfLoginExists("62cd937gfyurgfo734");
        assertEquals("error", response);
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenWebClientIsNotConnected() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.checkIfLoginExists("62cd937gfyurgfo734");
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenWebClientReturnsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.checkIfLoginExists("62cd937gfyurgfo734");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenOrgIdIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.checkIfLoginExists(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenWebClientIsNull() {
        DataManager dm = new DataManager(null);
        dm.checkIfLoginExists("83qwertyuiop987");
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenResponseIsMalformed() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "Something";
            }
        });
        dm.checkIfLoginExists("82qwerty74r5u5vr");
    }

    @Test
    public void testWhenOtherExceptionIsThrown() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new NullPointerException();
            }
        });
        String response = dm.checkIfLoginExists("83qwertyuiop987");
        assertEquals("error", response);
    }
}
