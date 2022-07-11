import org.junit.Test;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DataManager_attemptLogin_Test {
	
	/*
	 * This is a test class for the DataManager.createFund method.
	 * Add more tests here for this method as needed.
	 * 
	 * When writing tests for other methods, be sure to put them into separate
	 * JUnit test classes.
	 */

	@Test
	public void testSuccessfulTwoFunds() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{" +
						"\"status\":\"success\"," +
						"\"data\":{" +
							"\"_id\":\"12345\",\"login\":\"username\",\"password\":\"abc123\",\"name\":\"Cool Organization\",\"description\":\"a very cool organization\",\"funds\":[" +
								"{\"target\":1000, \"_id\":\"10\",\"name\":\"cancer\",\"description\":\"cancer research\",\"org\":\"12345\",\"donations\":[" +
									"{\"_id\":\"101\",\"contributor\":\"contrib1\",\"fund\":\"10\",\"date\":\"2022-07-07T18:30:04.657Z\",\"amount\":300,\"__v\":0}]," +
									"\"__v\":0" +
								"}," +
								"{\"target\":1000, \"_id\":\"11\",\"name\":\"hunger\",\"description\":\"hunger help\",\"org\":\"12345\",\"donations\":[" +
									"{\"_id\":\"102\",\"contributor\":\"contrib1\",\"fund\":\"11\",\"date\":\"2022-07-07T18:32:04.657Z\",\"amount\":200,\"__v\":0}]," +
									"\"__v\":0" +
								"}" +
						"]," +
						"\"__v\":0}}";
			}
			
		});
		Organization org = dm.attemptLogin("12345", "cow");
		List<Fund> funds = org.getFunds();

		assertNotNull(org);
		assertEquals("12345", org.getId());
		assertEquals("Cool Organization", org.getName());
		assertEquals("a very cool organization", org.getDescription());

		//Check the first fund and its donations
		//Check fund
		assertNotNull(funds);
		assertEquals(2, funds.size());
		Fund fund1 = funds.get(0);
		assertEquals(1000, fund1.getTarget());
		assertEquals("10", fund1.getId());
		assertEquals("cancer", fund1.getName());
		assertEquals("cancer research", fund1.getDescription());
		//Check donations
		List<Donation> fund1_donations = fund1.getDonations();
		assertEquals(1, fund1_donations.size());
		Donation fund1_donation1 = fund1_donations.get(0);
		assertEquals("10", fund1_donation1.getFundId());
		assertEquals("contrib1", fund1_donation1.getContributorName());
		assertEquals(300, fund1_donation1.getAmount());
		assertEquals("2022-07-07T18:30:04.657Z", fund1_donation1.getDate());


		//Check the second fund and its donations
		Fund fund2 = funds.get(1);
		assertEquals(1000, fund2.getTarget());
		assertEquals("11", fund2.getId());
		assertEquals("hunger", fund2.getName());
		assertEquals("hunger help", fund2.getDescription());
		//Check donations
		List<Donation> fund2_donations = fund2.getDonations();
		assertEquals(1, fund2_donations.size());
		Donation fund2_donation1 = fund2_donations.get(0);
		assertEquals("11", fund2_donation1.getFundId());
		assertEquals("contrib1", fund2_donation1.getContributorName());
		assertEquals(200, fund2_donation1.getAmount());
		assertEquals("2022-07-07T18:32:04.657Z", fund2_donation1.getDate());
	}

	@Test
	public void testUnsuccessful() {
		DataManager dm = new DataManager(new WebClient("localhost", 3001) {
			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) {
				return "{" +
						"\"status\":\"error\"," +
						"\"data\":{" +
						"\"_id\":\"12345\",\"login\":\"username\",\"password\":\"abc123\",\"name\":\"Cool Organization\",\"description\":\"a very cool organization\",\"funds\":[" +
						"{\"target\":1000, \"_id\":\"10\",\"name\":\"cancer\",\"description\":\"cancer research\",\"org\":\"12345\",\"donations\":[" +
						"{\"_id\":\"101\",\"contributor\":\"contrib1\",\"fund\":\"10\",\"date\":\"2022-07-07T18:30:04.657Z\",\"amount\":300,\"__v\":0}]," +
						"\"__v\":0" +
						"}," +
						"{\"target\":1000, \"_id\":\"11\",\"name\":\"hunger\",\"description\":\"hunger help\",\"org\":\"12345\",\"donations\":[" +
						"{\"_id\":\"102\",\"contributor\":\"contrib1\",\"fund\":\"11\",\"date\":\"2022-07-07T18:32:04.657Z\",\"amount\":200,\"__v\":0}]," +
						"\"__v\":0" +
						"}" +
						"]," +
						"\"__v\":0}}";
			}

		});
		Organization org = dm.attemptLogin("12345", "cow");
		assertNull(org);
	}

	@Test
	public void testExceptionThrown() {

		DataManager dm = new DataManager(new WebClient("localhost", 3001) {

			@Override
			public String makeRequest(String resource, Map<String, Object> queryParams) throws Exception {
				throw new Exception();
			}

		});
		Organization org = dm.attemptLogin("12345", "cow");
		assertNull(org);
	}


}
