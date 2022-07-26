import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class DataManager {

    private final WebClient client;
    private Map<String, String> idToName = new HashMap<>();

    public DataManager(WebClient client) {
        this.client = client;
    }

    /**
     * Attempt to log the user into an Organization account using the login and password.
     * This method uses the /findOrgByLoginAndPassword endpoint in the API
     *
     * @return an Organization object if successful; null if unsuccessful
     */
    public Organization attemptLogin(String login, String password) {

        try {
            Map<String, Object> map = new HashMap<>();
            if (login == null || password == null)
                throw new IllegalArgumentException("Check login and password");
            map.put("login", login);
            map.put("password", password);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/findOrgByLoginAndPassword", map);
            if (response == null || response.length() == 0)
                throw new IllegalStateException("WebClient response is null or empty.");
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (status.equals("success")) {
                JSONObject data = (JSONObject) json.get("data");
                String fundId = (String) data.get("_id");
                String name = (String) data.get("name");
                String description = (String) data.get("description"); //Spelling mistake
                Organization org = new Organization(fundId, name, description);

                JSONArray funds = (JSONArray) data.get("funds");
                Iterator it = funds.iterator();
                while (it.hasNext()) {
                    JSONObject fund = (JSONObject) it.next();
                    fundId = (String) fund.get("_id");
                    name = (String) fund.get("name");
                    description = (String) fund.get("description");
                    long target = (Long) fund.get("target");

                    Fund newFund = new Fund(fundId, name, description, target);

                    JSONArray donations = (JSONArray) fund.get("donations");
                    List<Donation> donationList = new LinkedList<>();
                    Iterator it2 = donations.iterator();
                    while (it2.hasNext()) {
                        JSONObject donation = (JSONObject) it2.next();
                        String contributorId = (String) donation.get("contributor");
                        String contributorName = this.getContributorName(contributorId);
                        long amount = (Long) donation.get("amount");
                        String date = (String) donation.get("date");
                        donationList.add(new Donation(fundId, contributorName, amount, date));
                    }

                    newFund.setDonations(donationList);

                    org.addFund(newFund);

                }

                return org;
            } else throw new IllegalStateException("response status : error");
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }


    /**
     * Look up the name of the contributor with the specified ID.
     * This method uses the /findContributorNameById endpoint in the API.
     *
     * @return the name of the contributor on success; null if no contributor is found
     */
    public String getContributorName(String id) {

        try {
            if (id == null) {
                throw new IllegalArgumentException("Id is null");
            }
            if (idToName.containsKey(id)) {
                return idToName.get(id);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/findContributorNameById", map);
            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                String name = (String) json.get("data");
                if (name.length() > 0) {
                    idToName.put(id, name);
                }
                return name;
            } else throw new IllegalStateException("response status was error");


        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method creates a new organization in the database using the /createOrg endpoint in the API
     *
     * @return a new Organization object if successful; null if unsuccessful
     */
    public Organization createOrg(String login, String password, String name, String description) {
        int stat = 1;
        try {
            if (login == null || password == null || name == null || description == null) {
                throw new IllegalArgumentException("Check function arguments. One or more arguments are null.");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            map.put("password", password);
            map.put("name", name);
            map.put("description", description);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/createOrg", map);
            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                JSONObject org = (JSONObject) json.get("data");
                String orgId = (String) org.get("_id");
                return new Organization(orgId, name, description);
            } else throw new IllegalStateException("Status returned error");

        } catch (IllegalStateException e) {
            stat = 0;
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            stat = 0;
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            stat = 0;
            throw new IllegalStateException();
        } catch (Exception e) {
            stat = 0;
//            e.printStackTrace();
            return null;
        } finally {
            if (stat == 0) {
                System.out.println("Error encountered in creating organization.");
            }
        }
    }

    /**
     * This method creates a new fund in the database using the /createFund endpoint in the API
     *
     * @return a new Fund object if successful; null if unsuccessful
     */
    public Fund createFund(String orgId, String name, String description, long target) {
        int stat = 1;
        try {
            if (orgId == null || name == null || description == null) {
                throw new IllegalArgumentException("Check function arguments. One or more arguments are null.");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("orgId", orgId);
            map.put("name", name);
            map.put("description", description);
            map.put("target", target);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/createFund", map);
            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                JSONObject fund = (JSONObject) json.get("data");
                String fundId = (String) fund.get("_id");
                return new Fund(fundId, name, description, target);
            } else throw new IllegalStateException("Status returned error");

        } catch (IllegalStateException e) {
            stat = 0;
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            stat = 0;
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            stat = 0;
            throw new IllegalStateException();
        } catch (Exception e) {
            stat = 0;
//            e.printStackTrace();
            return null;
        } finally {
            if (stat == 0) {
                System.out.println("Error encountered in creating fund.");
            }
        }
    }

    public String deleteFund(String fundID) {
        try {
            if (fundID == null) {
                throw new IllegalArgumentException("fundId is null");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", fundID);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/deleteFund", map);
            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");

            if (status.equals("success")) {
                return "success";
            } else
                return "error";
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
            return "error";
        }
    }

    public String checkIfLoginExists(String login) {
        try {
            if (login == null) {
                throw new IllegalArgumentException("login is null");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("login", login);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/checkOrgByLogin", map);
            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (!status.equals("error")) {
                return status;
            } else
                return "error";
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
            return "error";
        }
    }

    public String checkIfPasswordForOrgIsCorrect(String id, String password) {
        try {
            if (id == null || password == null) {
                throw new IllegalArgumentException("Null arguments");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("password", password);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/checkIfPasswordCorrect", map);
            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (!status.equals("error")) {
                return status;
            } else
                return "error";
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
            return "error";
        }
    }

//    public String updatePassword(String login, String password, String newPassword) {
//        try {
//            if (login == null || password == null || newPassword == null) {
//                throw new IllegalArgumentException("Null arguments");
//            }
//            Map<String, Object> map = new HashMap<>();
//            map.put("login", login);
//            map.put("password", password);
//            map.put("passwordNew", newPassword);
//            if (client == null) {
//                throw new IllegalStateException("WebClient is null");
//            }
//            String response = client.makeRequest("/updatePassword", map);
//            if (response == null) {
//                throw new IllegalStateException("response was null");
//            }
//            JSONParser parser = new JSONParser();
//            JSONObject json = (JSONObject) parser.parse(response);
//            String status = (String) json.get("status");
//            if (!status.equals("error")) {
//                return status;
//            } else
//                return "error";
//        } catch (IllegalStateException e) {
//            throw new IllegalStateException();
//        } catch (IllegalArgumentException e) {
//            throw new IllegalArgumentException();
//        } catch (ParseException e) {
//            throw new IllegalStateException();
//        } catch (Exception e) {
//            return "error";
//        }
//    }

    public String updatePassword(String id, String newPassword) {
        try {
            if (id == null || newPassword == null) {
                throw new IllegalArgumentException("Null arguments");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("passwordNew", newPassword);
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/updatePassword", map);
            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (!status.equals("error")) {
                return status;
            } else
                return "error";
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
            return "error";
        }
    }

    public String updateOrgInfo(String id, String name, String description, int which) {
        try {
            if (id == null || name == null || description == null) {
                throw new IllegalArgumentException("Null arguments");
            }
            if (which < 0 || which > 2) {
                throw new IllegalArgumentException("Problem with which argument");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            if (which == 0) {
                map.put("name", name);
            } else if (which == 1) {
                map.put("description", description);
            } else {
                map.put("name", name);
                map.put("description", description);
            }
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/updateOrgInfo", map);

            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (status.equals("error")) {
                return "error";
            } else
                return "success";
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
            return "error";
        }
    }

    public String updateOrgInfo(String id, String[] args, int which) {
        try {
            if (id == null || args == null) {
                throw new IllegalArgumentException("Null arguments");
            }
            if (which < 0 || which > 2) {
                throw new IllegalArgumentException("Problem with which argument");
            }
            if ((which == 0 || which == 1) && args[0] == null)
                throw new IllegalArgumentException("Null string argument given.");
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            if (which == 0) {
                map.put("name", args[0]);
            } else if (which == 1) {
                map.put("description", args[0]);
            } else {
                if (args[0] == null || args[1] == null)
                    throw new IllegalArgumentException("Null string argument given.");
                map.put("name", args[0]);
                map.put("description", args[1]);
            }
            if (client == null) {
                throw new IllegalStateException("WebClient is null");
            }
            String response = client.makeRequest("/updateOrgInfo", map);

            if (response == null) {
                throw new IllegalStateException("response was null");
            }
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            String status = (String) json.get("status");
            if (status.equals("error")) {
                return "error";
            } else
                return "success";
        } catch (IllegalStateException e) {
            throw new IllegalStateException();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        } catch (ParseException e) {
            throw new IllegalStateException();
        } catch (Exception e) {
            return "error";
        }
    }

}
