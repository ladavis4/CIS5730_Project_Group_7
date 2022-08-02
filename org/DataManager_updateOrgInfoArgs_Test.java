import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataManager_updateOrgInfoArgs_Test {
    @Test
    public void testSuccessfulUpdateNameAndDescription() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
            }
        });
        String response = dm.updateOrgInfo("783t4238b07", new String[]{"Penn", "Something"}, 2);
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
        String response = dm.updateOrgInfo("783t4238b07", new String[]{"Penn"}, 0);
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
        String response = dm.updateOrgInfo("783t4238b07", new String[]{"new description"}, 1);
        assertEquals("success", response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArgument() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        String response = dm.updateOrgInfo(null, new String[]{"Penn", "Something"}, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStringArgument() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        String response = dm.updateOrgInfo("12321dfaslfs", new String[]{null, "Something"}, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullStringArgumentDescriptionAndName() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.updateOrgInfo("12321dfaslfs", new String[]{null, "Something"}, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalWhichValue() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.updateOrgInfo("783t4238b07", new String[]{"Penn", "Something"}, 20);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullClient() {
        DataManager dm = new DataManager(null);
        dm.updateOrgInfo("783t4238b07", new String[]{"Penn", "Something"}, 2);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.updateOrgInfo("783t4238b07", new String[]{"Penn", "Something"}, 2);
    }

    @Test
    public void testErrorResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\"}";
            }
        });
        String response = dm.updateOrgInfo("783t4238b07", new String[]{"Penn", "Something"}, 2);
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
        dm.updateOrgInfo("783t4238b07", new String[]{"Penn", "Something"}, 2);
    }

    @Test
    public void testGenericException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) throws RuntimeException {
                throw new RuntimeException();
            }
        });
        String response = dm.updateOrgInfo("783t4238b07", new String[]{"Penn", "Something"}, 2);
        assertEquals("error", response);
    }
}
