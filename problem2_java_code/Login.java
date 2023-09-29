import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The Login class provides user registration and login functionality.
 * User credentials are stored in a file named "userCredentials.txt" in the current directory.
 */

public class Login {
	
	   /**
     * A map of usernames to hashed passwords and security question/answer pairs.
     */
	

    private static Map<String, String> userMap = new HashMap<>();
    
    /**
     * The main method of the Login class.
     *
     * @param args The command line arguments.
     */

    public static void main(String[] args) {
        File file = new File("userCredentials.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            readUserCredentialsFromFile(file);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to register? (yes or no)");
        String registerOption = scanner.nextLine().trim().toLowerCase();
        if (registerOption.equals("yes")) {
            System.out.println("Enter username:");
            String username = scanner.nextLine().trim();
            if (userMap.containsKey(username)) {
                System.out.println("Username already exists. Please login with your credentials.");
                loginUser(scanner, username);
            } else {
                System.out.println("Enter password:");
                String password = scanner.nextLine().trim();
                String hashedPassword = hashPassword(password);
                System.out.println("Enter security question:");
                String securityQuestion = scanner.nextLine().trim();
                System.out.println("Enter answer to security question:");
                String securityAnswer = scanner.nextLine().trim();
                System.out.println("Account login successful.");
                System.out.println("Enter database name");
                String database_name = scanner.nextLine().trim();
                userMap.put(username, hashedPassword + ":" + securityQuestion + ":" + securityAnswer+":"+database_name);

                appendUserCredentialsToFile(file, username, hashedPassword, securityQuestion, securityAnswer,database_name);

                String currentDir = System.getProperty("user.dir");

                /**
                 *  create a new file object with the folder name and current directory
                 */
                
                File folder = new File(currentDir + File.separator + database_name);

                /**
                 *  create the folder if it doesn't already exist
                 */
                
                if (!folder.exists()) {
                    boolean success = folder.mkdir();
                    if (success) {
                        System.out.println("Database created successfully");
                        
                    } else {
                        System.out.println("Database creation failed");
                    }
                } else {
                    System.out.println("Database already exists");
                }
            }
        } else if (registerOption.equals("no")) {
            System.out.println("Enter username:");
            String username = scanner.nextLine().trim();
            if (!userMap.containsKey(username)) {
            	System.out.println(userMap);
                System.out.println("Username not found. Please register.");
            } else {
                loginUser(scanner, username);
            }
        } else {
            System.out.println("Invalid option.");
        }
    }
    
    /**
     * Reads user credentials from the specified file and adds them to the userMap.
     *
     * @param file The file containing the user credentials.
     */

    private static void readUserCredentialsFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split(":");
                if (parts.length == 5) {
                    userMap.put(parts[0], parts[1] + ":" + parts[2] + ":" + parts[3]+":"+parts[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method appends the given user credentials to the specified file.
     *
     * @param file            the file to append the credentials to
     * @param username        the username of the user to be added
     * @param hashedPassword  the hashed password of the user to be added
     * @param securityQuestion the security question of the user to be added
     * @param securityAnswer  the security answer of the user to be added
     * @param database_name the name of the database to be used for the user
     */
    
    private static void appendUserCredentialsToFile(File file, String username, String hashedPassword,
                                                     String securityQuestion, String securityAnswer,String database_name) {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(username + ":" + hashedPassword + ":" + securityQuestion + ":" + securityAnswer +":"+database_name + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * This method hashes the given password using the MD5 algorithm and returns the hash.
     *
     * @param password the password to be hashed
     * @return the hashed password
     */
    

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method prompts the user to enter their password and security answer, and checks if they match the
     * stored values. If they do, it logs the user in and creates a Query object for them to interact with the database.
     *
     * @param scanner  the scanner object to read input from the user
     * @param username the username of the user to be logged in
     */
    
    
    private static void loginUser(Scanner scanner, String username) {
        String userCredentials = userMap.get(username);
        String[] parts = userCredentials.split(":");
        String hashedPassword = parts[0];
        String securityQuestion = parts[1];
        String securityAnswer = parts[2];

        System.out.println("Enter password:");
        String password = scanner.nextLine();
        String hashedPasswordInput = hashPassword(password);
        if (!hashedPasswordInput.equals(hashedPassword)) {
            System.out.println("Incorrect password. Please try again.");
        } else {
            System.out.println(securityQuestion);
            String securityAnswerInput = scanner.nextLine().trim();
            if (!securityAnswerInput.equals(securityAnswer)) {
                System.out.println("Incorrect security answer. Please try again.");
            } else {
            
            	String database_name = parts[3];
                
            	System.out.println("Login successful!");
            	
            	Query my_query = new Query(database_name);
            	
            	my_query.input_query();
            	
            }
        }
    }

}
