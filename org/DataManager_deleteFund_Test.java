import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataManager_deleteFund_Test {
    @Test
    public void testSuccessfulDeletion() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/deleteFund") && queryParams.get("id").equals("62cd937gfyurgfo734")) {
                    return "{\"status\":\"success\"}";
                }
                return "{\"status\":\"error\"}";
            }
        });
        String response = dm.deleteFund("62cd937gfyurgfo734");
        assertEquals("success", response);
    }

    @Test
    public void testUnsuccessfulDeletion() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        String response = dm.deleteFund("62cd937gfyurgfo734");
        assertEquals("error", response);
    }

    @Test
    public void testWhenWebClientIsNotConnected() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        String response = dm.deleteFund("62cd937gfyurgfo734");
        assertEquals("error", response);
    }

    @Test
    public void testWhenWebClientReturnsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        String response = dm.deleteFund("62cd937gfyurgfo734");
        assertEquals("error", response);
    }

    @Test
    public void testWhenFundIdIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "Something";
            }
        });
        String response = dm.deleteFund(null);
        assertEquals("error", response);
    }

    @Test
    public void testWhenWebClientIsNull() {
        DataManager dm = new DataManager(null);
        String response = dm.deleteFund("83qwertyuiop987");
        assertEquals("error", response);
    }

    @Test
    public void testWhenResponseIsMalformed() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "Something";
            }
        });
        String response = dm.deleteFund("82qwerty74r5u5vr");
        assertEquals("error", response);
    }
}
