import org.junit.Test;

import java.text.ParseException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataManager_createOrg_Test {
    @Test
    public void testSuccessfulCreateOrg() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\",\"data\":{\"_id\":\"12345\",\"__v\":0}}";
            }
        });
        Organization org = dm.createOrg("davis", "abc123", "OrgName", "orgDescription");
        assertEquals("OrgName", org.getName());
        assertEquals("12345", org.getId());
        assertEquals("orgDescription", org.getDescription());
    }

    @Test(expected = IllegalStateException.class)
    public void testErrorStatus() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"error\",\"data\":{\"_id\":\"12345\",\"__v\":0}}";
            }
        });
        Organization org = dm.createOrg("davis", "abc123", "OrgName", "orgDescription");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArg() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        Organization org = dm.createOrg(null, "abc123", "OrgName", "orgDescription");
    }

    @Test(expected = IllegalStateException.class)
    public void testNullClient() {
        DataManager dm = new DataManager(null);
        Organization org = dm.createOrg("davis", "abc123", "OrgName", "orgDescription");
    }

    @Test(expected = IllegalStateException.class)
    public void testNullResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        Organization org = dm.createOrg("davis", "abc123", "OrgName", "orgDescription");
    }

    @Test(expected = IllegalStateException.class)
    public void testParseException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "not_a_json";
            }
        });
        Organization org = dm.createOrg("davis", "abc123", "OrgName", "orgDescription");
    }

    @Test
    public void testGenericException() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) throws RuntimeException {
                throw new RuntimeException();
            }
        });
        Organization org = dm.createOrg("davis", "abc123", "OrgName", "orgDescription");
        assertNull(org);
    }
}
