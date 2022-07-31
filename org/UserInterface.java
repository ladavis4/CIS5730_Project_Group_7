import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserInterface {


    private DataManager dataManager;
    private Organization org;
    private Scanner in = new Scanner(System.in);
    private Map<Integer, String> results = new HashMap<>();
    private String currentLogin;
    private static String log;

    public UserInterface(DataManager dataManager, Organization org) {
        this.dataManager = dataManager;
        this.org = org;
    }

    public UserInterface(DataManager dataManager, Organization org, String currentLogin) {
        this.dataManager = dataManager;
        this.org = org;
        this.currentLogin = currentLogin;
    }

    /*
     * Error handling for incorrect fund name being provided (1.8)
     * Also displayed proportion of target met (1.3)
     */
    public void start() {

        while (true) {
            System.out.println("\n\n");
            if (org.getFunds().size() > 0) {
                System.out.println("There are " + org.getFunds().size() + " funds in this organization:");

                int count = 1;
                for (Fund f : org.getFunds()) {

                    System.out.println(count + ": " + f.getName());

                    count++;
                }
                System.out.println("Enter the fund number to see more information.");
            }
            System.out.println("Enter 0 to create a new fund");
            System.out.println("Enter -1 to logout of this user");
            System.out.println("Enter C/c to change password");
            System.out.println("Enter E/e to edit account information");
            String str = in.nextLine().trim();
            if (str.equalsIgnoreCase("c")) {
                changePassword();
            } else if (str.equalsIgnoreCase("e")) {
                editAccountInfo();
            } else {
                int opt = -2;
                try {
                    if (Integer.parseInt(str) >= -1 && Integer.parseInt(str) <= org.getFunds().size()) {
                        int option = Integer.parseInt(str);
                        opt = option;
                        if (option == 0) {
                            createFund();
                        } else if (option == -1) {
                            logout();
                        } else {
                            displayFund(option);
                        }
                    } else {
                        System.out.println("Error: Incorrect fund number provided. Please enter a valid number");
                    }
                } catch (Exception e) {
                    if (opt == -2)
                        System.out.println("Error: Incorrect fund number provided. Please enter a valid number");
                }
            }
        }

    }

    public void logout() {
        main(new String[]{});
        while (true) {
            System.out.print("Enter the login : ");
            String login = in.nextLine().trim();

            while (login.length() == 0) {
                System.out.println("Error: Blank login provided! Provide another login");
                System.out.print("Enter the login: ");
                login = in.nextLine().trim();
            }

            System.out.print("Enter the password : ");
            String password = in.nextLine().trim();

            while (password.length() == 0) {
                System.out.println("Error: Blank password provided! Provide another password");
                System.out.print("Enter the password: ");
                password = in.nextLine().trim();
            }
            try {
                Organization organization = this.dataManager.attemptLogin(login, password);
                this.org = organization;
                break;
            } catch (Exception e) {
                System.out.println("Login failed, please try again");
            }
        }

    }

    public void changePassword() {
        try {
//        System.out.println("Enter 1 to abort password change");
            System.out.println("Enter your current password");
            String currentPassword = in.nextLine().trim();
//        if (currentPassword.equalsIgnoreCase("n")) {
//
//        }
            if (currentPassword.length() == 0 || currentPassword == null) {
                System.out.println("Wrong password. Please try changing password again.");
            } else {
                String checkPassword = this.dataManager.checkIfPasswordForOrgIsCorrect(this.org.getId(), currentPassword);
                if (checkPassword.equals("error")) {
                    System.out.println("There was an error in verifying password. Please try again.");
                } else if (checkPassword.equals("false")) {
                    System.out.println("Input current password is incorrect. Please try again.");
                } else {
                    System.out.println("Press enter to abort");
                    System.out.println("Enter new password");
                    String new1 = in.nextLine().trim();
                    if (new1.length() != 0) {
                        System.out.println("Enter new password again");
                        String new2 = in.nextLine().trim();
                        if (new1.equals(new2)) {
//                            String done = this.dataManager.updatePassword(currentLogin, currentPassword, new2);
                            String done = this.dataManager.updatePassword(this.org.getId(), new2);
                            if (done.equals("success")) {
                                System.out.println("Password changed successfully");
                            } else throw new Exception();
                        } else
                            System.out.println("New passwords don't match.\nPassword was not changed.\nPlease try again.");
                    } else System.out.println("Password not changed.");
                }
            }
        } catch (Exception e) {
            System.out.println("There was an unexpected error in changing password. Please try again.");
        }
    }

    public void editAccountInfo() {
        try {
            System.out.println("Enter your current password");
            String currentPassword = in.nextLine().trim();
            if (currentPassword.length() == 0) {
                System.out.println("Wrong password. Please try again.");
            } else {
                String checkPassword = this.dataManager.checkIfPasswordForOrgIsCorrect(this.org.getId(), currentPassword);
                if (checkPassword.equals("error")) {
                    System.out.println("There was an error in verifying password. Please try again.");
                } else if (checkPassword.equals("false")) {
                    System.out.println("Input current password is incorrect. Please try again.");
                } else {
                    String name = null;
                    String description = null;
                    System.out.println("Would you like to change the name of the organization?");
                    System.out.println("Current name: " + this.org.getName());
                    System.out.println("Enter Y/y for yes, any other key for no");
                    String response = in.nextLine().trim();
                    if (response.toUpperCase().equals("Y")) {
                        System.out.println("Enter new name");
                        name = in.nextLine().trim();
                        while (name.length() == 0) {
                            System.out.println("Name cannot be empty\nEnter new name: ");
                            name = in.nextLine().trim();
                        }
                    } else {
                        System.out.println("Name will not be changed");
                    }

                    System.out.println("Would you like to change the description of the organization?");
                    System.out.println("Description: " + this.org.getDescription());
                    System.out.println("Enter Y/y for yes, any other key for no");
                    response = in.nextLine().trim();
                    if (response.toUpperCase().equals("Y")) {
                        System.out.println("Enter new description");
                        description = in.nextLine().trim();
                        while (description.length() == 0) {
                            System.out.println("Description cannot be empty\nEnter new description: ");
                            description = in.nextLine().trim();
                        }
                    } else {
                        System.out.println("Description will not be changed");
                    }

                    String done = "failure";
                    if (name == null && description == null) {
                        System.out.println("No changes applied");
                    } else if (name != null && description == null) {
                        done = this.dataManager.updateOrgInfo(this.org.getId(), new String[]{name}, 0);
                    } else if (name == null && description != null) {
                        done = this.dataManager.updateOrgInfo(this.org.getId(), new String[]{description}, 1);
                    } else {
                        done = this.dataManager.updateOrgInfo(this.org.getId(), new String[]{name, description}, 2);
                    }
                    if (done.equals("success")) {
                        System.out.println("Updated info successfully");
                    } else throw new Exception();
                }
            }
        } catch (Exception e) {
            System.out.println("There was an unexpected error in updating information. Please try again.");
        }
    }

    /*
     * Error Handling for creating funds (1.8)
     */
    public void createFund() {

        System.out.print("Enter the fund name: ");
        String name = in.nextLine().trim();

        // Check if we have to ensure that the fund name is new
        List<Fund> funds = org.getFunds();
        List<String> fundNames = new ArrayList<String>();
        for (Fund fund : funds) {
            fundNames.add(fund.getName());
        }

        while (name.length() == 0 || fundNames.contains(name)) {
            System.out.println("Error: Incorrect fund name provided! Please provide valid fund name");
            System.out.print("Enter the fund name: ");
            name = in.nextLine().trim();
        }

        System.out.print("Enter the fund description: ");
        String description = in.nextLine().trim();

        while (description.length() == 0) {
            System.out.println("Error: Incorrect fund description provided! Please provide valid description");
            System.out.print("Enter the fund description: ");
            description = in.nextLine().trim();
        }

        long target = -5;
        do {
            System.out.print("Enter the fund target:");
            while (!in.hasNextInt()) {
                System.out.println("Error: Incorrect target amount provided! Please provide valid target amount");
                System.out.print("Enter the fund target:");
                in.next();
            }
            target = Long.parseLong(in.nextLine().trim());
        } while (target <= 0);
        try {
            Fund fund = dataManager.createFund(org.getId(), name, description, target);
            org.getFunds().add(fund);
        } catch (Exception e) {
            System.out.println("Fund could not be created please try again.");
        }

    }

    /*
     * Changed the date formatting here (1.7)
     */
    public void displayFund(int fundNumber) {
        Fund fund = org.getFunds().get(fundNumber - 1);

        System.out.println("\n\n");
        System.out.println("Here is information about this fund:");
        System.out.println("Name: " + fund.getName());
        System.out.println("Description: " + fund.getDescription());
        System.out.println("Target: $" + fund.getTarget());

        int num = -1;
        do {
            System.out.println("Enter 0 to see donations at individual level and enter 1 to see donations at aggregate level");
            System.out.print("Enter a number:");
            while (!in.hasNextInt()) {
                System.out.println("Error: Incorrect value provided. Enter 0 to see donations at individual level, 1 at aggregate level");
                System.out.print("Enter a number:");
                in.next();
            }
            num = in.nextInt();
        } while (!(num == 0 || num == 1));

        in.nextLine();

        if (num == 0) {
            List<Donation> donations = fund.getDonations();
            System.out.println("Number of donations: " + donations.size());
            long total = 0;

            for (Donation donation : donations) {

                total += donation.getAmount();

                String str = donation.getDate().substring(0, 10);
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(str);
                    DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                    String out = dateFormat.format(date);
                    System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + out);
                } catch (ParseException e) {

                }
            }

            double percent = (double) total / fund.getTarget() * 100;

            System.out.println("Total donation amount : $" + total + " (" + percent + "% of target)");


        } else {
            List<Donation> donations = fund.getDonations();
            System.out.println("Number of donations: " + donations.size());


            if (results.containsKey(fundNumber)) {
                String out = results.get(fundNumber);
                System.out.print(out);
            } else {
                long total = 0;
                HashMap<String, Long> aggregateAmount = new HashMap<>();
                HashMap<String, Integer> aggregateCount = new HashMap<>();
                for (Donation donation : donations) {

                    total += donation.getAmount();

                    String name = donation.getContributorName();
                    long amount = donation.getAmount();

                    if (aggregateAmount.containsKey(name)) {

                        long amt = aggregateAmount.get(name);
                        amt += amount;

                        int count = aggregateCount.get(name);
                        count++;

                        aggregateAmount.replace(name, amt);
                        aggregateCount.replace(name, count);
                    } else {
                        aggregateAmount.put(name, amount);
                        aggregateCount.put(name, 1);
                    }

                }

                // Create a sorted list of values in a TreeSet
                TreeSet<Long> set = new TreeSet<>();
                for (String key : aggregateAmount.keySet()) {
                    set.add(aggregateAmount.get(key));
                }

                // Iterate over TreeSet and print out aggregate donations from highest to lowest
                String memo = "";
                while (!set.isEmpty()) {
                    long highest = set.pollLast();
                    for (String key : aggregateAmount.keySet()) {
                        if (aggregateAmount.get(key) == highest) {
                            memo += key + ", " + aggregateCount.get(key) + " donations, $" + highest + " total\n";
                        }
                    }
                }

                double percent = (double) total / fund.getTarget() * 100;

                memo += "Total donation amount : $" + total + " (" + percent + "% of target)\n";
                results.put(fundNumber, memo);
                System.out.print(memo);

            }

        }

        System.out.println("Enter D or d to delete this fund");
        System.out.println("Press the Enter key to go back to the listing of funds");
        String d = in.nextLine().trim();
        if (d.length() != 0 && (d.equals("D") || d.equals("d"))) {
            System.out.println("Enter Y/y for confirmation to delete this fund");
            d = in.nextLine().trim();
            if (d.toUpperCase().equals("Y")) {
                try {
                    String res = dataManager.deleteFund(fund.getId());
                    if (res.equals("success")) {
                        List<Fund> funds = org.getFunds();
                        funds.removeIf(fund1 -> fund1.getId().equals(fund.getId()));
                        System.out.println("Fund deleted successfully");
                    } else {
                        System.out.println("Could not delete the fund. Please try again");
                    }
                } catch (Exception e) {
                    System.out.println("Could not delete the fund. Please try again");
                }
            } else
                System.out.println("Fund was not deleted");
        }
    }


//    public static Organization createOrganization(DataManager ds, Scanner scanner) {
//
//        while (true) {
//            System.out.print("Enter the login : ");
//            String login = scanner.nextLine().trim();
//            String status = ds.checkIfLoginExists(login);
//
//            while (login.length() == 0 || status.equals("found")) {
//                if (login.length() == 0) {
//                    System.out.println("Error: Blank login provided! Provide another login");
//                } else {
//                    System.out.println("Error: Login provided already exists in database! Provide another login");
//                }
//                System.out.print("Enter the login: ");
//                login = scanner.nextLine().trim();
//            }
//
//            log = login;
//
//            System.out.print("Enter the password : ");
//            String password = scanner.nextLine().trim();
//
//            while (password.length() == 0) {
//                System.out.println("Error: Blank password provided! Provide another password");
//                System.out.print("Enter the password: ");
//                password = scanner.nextLine().trim();
//            }
//
//            System.out.print("Enter the name : ");
//            String name = scanner.nextLine().trim();
//
//            while (name.length() == 0) {
//                System.out.println("Error: Blank password provided! Provide another password");
//                System.out.print("Enter the password: ");
//                password = scanner.nextLine().trim();
//            }
//
//            System.out.print("Enter the description : ");
//            String description = scanner.nextLine().trim();
//
//            while (name.length() == 0) {
//                System.out.println("Error: Blank password provided! Provide another password");
//                System.out.print("Enter the password: ");
//                password = scanner.nextLine().trim();
//            }
//
//            try {
//                Organization org = ds.createOrg(login, password, name, description);
//                //HashMap<Organization, String> map = new HashMap<>();
//                //map.put(org, login);
//                return org;
//            } catch (Exception e2) {
//                System.out.println("Creating Organization failed, please try again");
//            }
//        }
//
//    }

    public static Organization createOrganization(DataManager ds, Scanner scanner) {

        while (true) {
            System.out.print("Enter the login : ");
            String login = scanner.nextLine().trim();
            if (login.length() == 0) {
                System.out.println("Error: Blank login provided! Please try again.");
                break;
            }
            String status = ds.checkIfLoginExists(login);
            if (status.equals("not found")) {
                System.out.print("Enter the password : ");
                String password = scanner.nextLine().trim();
                while (password.length() == 0) {
                    System.out.println("Error: Blank password provided! Provide another password");
                    System.out.print("Enter the password: ");
                    password = scanner.nextLine().trim();
                }

                System.out.print("Enter the name of organization: ");
                String name = scanner.nextLine().trim();
                while (name.length() == 0) {
                    System.out.println("Error: Empty name!");
                    System.out.print("Enter the name: ");
                    name = scanner.nextLine().trim();
                }

                System.out.print("Enter the description : ");
                String description = scanner.nextLine().trim();

                while (description.length() == 0) {
                    System.out.println("Error: Description field empty!");
                    System.out.print("Enter the description: ");
                    description = scanner.nextLine().trim();
                }

                try {
                    Organization org = ds.createOrg(login, password, name, description);
                    return org;
                } catch (Exception e2) {
                    System.out.println("Creating Organization failed, please try again");
                    return null;
                }
            } else if (status.equals("found")) {
                System.out.println("An organization with this login already exists. Please try again with a different login");
            } else {
                System.out.println("An unexpected error occurred.");
                break;
            }
        }
        return null;
    }


    public static Organization login(DataManager ds, Scanner scanner) {
        while (true) {
            System.out.print("Enter the login : ");
            String login = scanner.nextLine().trim();

            while (login.length() == 0) {
                System.out.println("Error: Blank login provided! Provide another login");
                System.out.print("Enter the login: ");
                login = scanner.nextLine().trim();
            }

            log = login;

            System.out.print("Enter the password : ");
            String password = scanner.nextLine().trim();

            while (password.length() == 0) {
                System.out.println("Error: Blank password provided! Provide another password");
                System.out.print("Enter the password: ");
                password = scanner.nextLine().trim();
            }
            try {
                Organization org = ds.attemptLogin(login, password);
                //HashMap<Organization, String> map = new HashMap<>();
                //map.put(org, login);
                return org;
            } catch (Exception e2) {
                System.out.println("Login failed, please try again");
            }
        }
    }


    public static void main(String[] args) {

        try {
            DataManager ds = new DataManager(new WebClient("localhost", 3001));
            String login = args[0];
            String password = args[1];

            Organization org = ds.attemptLogin(login, password);

            if (org == null) {
                System.out.println("Login failed.");
            } else {

                UserInterface ui = new UserInterface(ds, org, login);

                ui.start();

            }
        } catch (Exception e) {

            DataManager ds = new DataManager(new WebClient("localhost", 3001));
            Organization org = null;
            while (true) {
                System.out.println("\n\nEnter 0 to provide login and password to login.");
                System.out.println("Enter 1 to create a new organization");
                Scanner scanner = new Scanner(System.in);
                String str = scanner.nextLine().trim();
                try {
                    if (Integer.parseInt(str) >= 0 && Integer.parseInt(str) <= 1) {
                        int option = Integer.parseInt(str);
                        if (option == 1) {
                            /*HashMap<Organization, String> map = createOrganization(ds, scanner);
                        	int count = 0;
                        	for(Organization key: map.keySet()) {
                        		if(count == 0) {
                        			org = key;
                        			login = map.get(key);
                        			count++;
                        			break;
                        		}
                        	}*/
                            org = createOrganization(ds, scanner);
                        } else {
                        	/*HashMap<Organization, String> map = login(ds, scanner);
                        	int count = 0;
                        	for(Organization key: map.keySet()) {
                        		if(count == 0) {
                        			org = key;
                        			login = map.get(key);
                        			count++;
                        			break;
                        		}
                        	}*/
                            org = login(ds, scanner);
                        }
                        if (org == null) {
                            throw new NullPointerException();
                        }
                        break;
                    } else {
                        System.out.println("Error: Incorrect number provided. Please enter 0 or 1 ");
                    }
                } catch (NullPointerException npe) {
                    System.out.println("Please try again");
                } catch (Exception e1) {
                    System.out.println("Error: Incorrect number provided. Please enter 0 or 1");
                }
            }
            UserInterface ui = new UserInterface(ds, org);
            ui.start();

        }
    }

}
