import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

//    This is a test class for the DataManager.attemptLogin method.
public class DataManager_attemptLogin_Test {
    @Test
    public void testSuccessfulLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                if (resource.equals("/findContributorNameById"))
                    return "{\"status\":\"success\",\"data\":\"Harsh\"}";
                return "{\"status\":\"success\"," +
                        "\"data\":{\"_id\":\"12345\",\"name\":\"Penn\",\"description\":\"This is a university =)\"," +
                        "\"funds\":[{\"_id\": \"1\",\"name\":\"CIS\", \"description\":\"Some description\", \"target\":1000000," +
                        "\"donations\":[{\"contributor\":\"Harsh\",\"amount\":100000,\"date\":\"10 July, 2022\"}]}]}}";
            }
        });
        Organization org = dm.attemptLogin("hasareen", "password");
        assertNotNull(org);
        assertEquals("12345", org.getId());
        assertEquals("Penn", org.getName());
        assertEquals("This is a university =)", org.getDescription());
        List<Fund> funds = org.getFunds();
        Fund fund = funds.get(0);
        assertEquals("1", fund.getId());
        assertEquals("CIS", fund.getName());
        assertEquals("Some description", fund.getDescription());
        assertEquals(1000000, fund.getTarget());
        List<Donation> donations = fund.getDonations();
        Donation donation = donations.get(0);
        assertEquals("1", donation.getFundId());
        assertEquals("Harsh", donation.getContributorName());
        assertEquals(100000, donation.getAmount());
        assertEquals("10 July, 2022", donation.getDate());
    }

    @Test(expected = IllegalStateException.class)
    public void testUnsuccessfulLogin() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"login failed\"}";
            }
        });
//        Organization org =
        dm.attemptLogin("Harsh", "pass");
//        assertNull(org);
    }

    @Test(expected = IllegalStateException.class)
    public void testMalformedResponse() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "no_speak_json";
            }
        });
//        Organization org =
        dm.attemptLogin("Harsh", "pass");
//        assertNull(org);
    }
}
