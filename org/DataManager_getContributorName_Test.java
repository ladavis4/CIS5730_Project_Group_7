import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_getContributorName_Test {
	
	/*
	 * This is a test class for the DataManager.createFund method.
	 * Add more tests here for this method as needed.
	 * 
	 * When writing tests for other methods, be sure to put them into separate
	 * JUnit test classes.
	 */

	@Test
	public void testSuccessful() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"success\",\"data\":\"Lenny\"}";
				
			}
			
		});
		String name = dm.getContributorName("12345");
		
		assertNotNull(name);
		assertEquals("Lenny", name);
	}

	@Test
	public void testUnsuccessful() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{\"status\":\"error\",\"data\":\"Lenny\"}";

			}

		});
		String name = dm.getContributorName("12345");

		assertNull(name);
	}

	@Test
	public void testExceptionThrown() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) throws Exception {
				throw new Exception();
			}

		});
		String name = dm.getContributorName("12345");

		assertNull(name);
	}

}
