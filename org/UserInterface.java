import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserInterface {


    private DataManager dataManager;
    private Organization org;
    private Scanner in = new Scanner(System.in);
    private Map<Integer, String> results = new HashMap<>();

    public UserInterface(DataManager dataManager, Organization org) {
        this.dataManager = dataManager;
        this.org = org;
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

            String str = in.nextLine();

            try {


                if (Integer.parseInt(str) >= -1 && Integer.parseInt(str) <= org.getFunds().size()) {
//                    in.nextLine();
                    int option = Integer.parseInt(str);
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
                System.out.println("Error: Incorrect fund number provided. Please enter a valid number");
            }
        }

    }

    public void logout() {

        while (true) {
//            in.nextLine();
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
                System.out.println("Login failed");
            }
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
            target = in.nextInt();
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

       /*
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
            //System.out.println("* " + donation.getContributorName() + ": $" + donation.getAmount() + " on " + donation.getDate());
        }

        double percent = (double) total / fund.getTarget() * 100;

        System.out.println("Total donation amount : $" + total + " (" + percent + "% of target)");*/

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

        //System.out.println("Press the Enter key to go back to the listing of funds");
        System.out.println("Enter D or d to delete this fund");
        System.out.println("Press the Enter key to go back to the listing of funds");
        String d = in.nextLine().trim();
        if (d.length() != 0 && (d.equals("D") || d.equals("d"))) {
            System.out.println("Enter Y/y for confirmation to delete this fund");
            d = in.nextLine().trim();
            if (d.toUpperCase().equals("Y")) {
                String res = dataManager.deleteFund(fund.getId());
                if (res.equals("success")) {
                    List<Fund> funds = org.getFunds();
                    funds.removeIf(fund1 -> fund1.getId().equals(fund.getId()));
                } else {
                    System.out.println("Could not delete the fund. Please try again");
                }
            } else
                System.out.println("Fund was not deleted");
        }
//            in.nextLine();
    }


    public static void main(String[] args) {

        DataManager ds = new DataManager(new WebClient("localhost", 3001));

        String login = args[0];
        String password = args[1];


        Organization org = ds.attemptLogin(login, password);

        if (org == null) {
            System.out.println("Login failed.");
        } else {

            UserInterface ui = new UserInterface(ds, org);

            ui.start();

        }
    }

}
