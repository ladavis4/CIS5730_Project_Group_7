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


}
