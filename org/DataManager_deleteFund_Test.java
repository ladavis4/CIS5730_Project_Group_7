import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DataManager_deleteFund_Test {
    @Test
    public void testSuccessfulDeletion() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "{\"status\":\"success\"}";
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

    @Test(expected = IllegalStateException.class)
    public void testWhenWebClientIsNotConnected() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.deleteFund("62cd937gfyurgfo734");
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenWebClientReturnsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return null;
            }
        });
        dm.deleteFund("62cd937gfyurgfo734");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWhenFundIdIsNull() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001));
        dm.deleteFund(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenWebClientIsNull() {
        DataManager dm = new DataManager(null);
        dm.deleteFund("83qwertyuiop987");
    }

    @Test(expected = IllegalStateException.class)
    public void testWhenResponseIsMalformed() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                return "Something";
            }
        });
        dm.deleteFund("82qwerty74r5u5vr");
    }

    @Test
    public void testWhenOtherExceptionIsThrown() {
        DataManager dm = new DataManager(new WebClient("localhost", 3001) {
            @Override
            public String makeRequest(String resource, Map<String, Object> queryParams) {
                throw new NullPointerException();
            }
        });
        String response = dm.deleteFund("83qwertyuiop987");
        assertEquals("error", response);
    }
}
