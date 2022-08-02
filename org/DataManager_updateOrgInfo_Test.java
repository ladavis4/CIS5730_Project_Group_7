import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataManager_updateOrgInfo_Test {
    @Test
    public void testSuccessfulUpdateNameAndDescription() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }
        });
        String response = dm.updateOrgInfo("783t4238b07", "Penn", "Something", 2);
        assertEquals("success", response);
    }

    @Test
    public void testSuccessfulUpdateName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }
        });
        String response = dm.updateOrgInfo("783t4238b07", "Penn", "", 0);
        assertEquals("success", response);
    }

    @Test
    public void testSuccessfulUpdateDescription() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }
        });
        String response = dm.updateOrgInfo("783t4238b07", "Penn", "new description", 1);
        assertEquals("success", response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.updateOrgInfo(null, "Penn", "Something", 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalWhichValue() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.updateOrgInfo("asdkdfdkj3939", "Penn", "Something", 10);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullClient() {
        DataManager dm = new DataManager(null);
        dm.updateOrgInfo("asdkdfdkj3939", "Penn", "Something", 2);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.updateOrgInfo("asdkdfdkj3939", "Penn", "Something", 2);
    }

    @Test
    public void testErrorResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        String response = dm.updateOrgInfo("asdkdfdkj3939", "Penn", "Something", 2);
        assertEquals("error", response);
    }

    @Test(expected = IllegalStateException.class)
    public void testParseException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "not_a_json";
            }
        });
        dm.updateOrgInfo("asdkdfdkj3939", "Penn", "Something", 2);
    }

    @Test
    public void testGenericException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) throws RuntimeException {
                throw new RuntimeException();
            }
        });
        String response = dm.updateOrgInfo("asdkdfdkj3939", "Penn", "Something", 2);
        assertEquals("error", response);
    }
}
