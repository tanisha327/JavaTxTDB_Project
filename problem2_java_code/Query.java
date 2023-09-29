
/**

    This program allows to interact with a simple SQL database.
    It prompts the user to enter SQL queries and executes them.
    The program supports the following SQL queries:
        CREATE TABLE
        INSERT INTO
        SELECT
        UPDATE
        DELETE
    @author: Tanisha

*/



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;


/**

This class represents a query object for a specific database.
*/

public class Query {
	
	/**
	 * The name of the database.
	 */
	
	public String db_name;
	
	/**
	 * Constructs a Query object with the given database name.
	 * 
	 * @param db_name the name of the database
	 */
	

	public Query(String db_name) {
		this.db_name = db_name;
	}
	
	/**
	 * Creates a table with the specified name and columns.
	 * 
	 * @param table the name of the table to create
	 * @param Cols a list of strings representing the columns of the table
	 */
	
	public void create_table(String table, List<String> Cols) {
		String header = String.join(":", Cols);

		
		String filePath = this.db_name+"/"+table+".txt"; // file path relative to the current working directory

        try {
        	
            /**
             * Create a new FileWriter object with append mode
             */
        	
            FileWriter fw = new FileWriter(filePath, true);

            /**
             *  Create a new BufferedWriter object
             */
            
            BufferedWriter bw = new BufferedWriter(fw);

            /**
             * Append a string to the file
             */
            
            bw.write(header);

            /**
             *  Close the BufferedWriter
             */
            
            bw.close();

            System.out.println("table created successfully.");

        } catch (IOException e) {
            System.out.println("An error occurred while creating the table.");
            e.printStackTrace();
        }
	}
	
	/**

    Inserts a new row into the specified table with the given values.

    @param table the name of the table to insert into

    @param insert_values the list of values to insert as a new row
    */
	
	
	public void insert_into_table(String table, List<String> insert_values) {
		String row = String.join(":", insert_values);
		System.out.println(row);
		
		String filePath = this.db_name+"/"+table+".txt"; // file path relative to the current working directory

		try {
			
            /*
             *  Append a new line to the file
             */
			
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write("\n"+row);
            writer.close();

            System.out.println("the record got inserted to the table.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
	}
	
	/**

    Retrieves data from the specified table for the specified columns.

    @param table the name of the table to select from

    @param select_cols the list of column names to retrieve data for
    */
	
	
	public void select_from_table(String table, String[] select_cols) {
		
		String filePath = this.db_name+"/"+table+".txt"; // file path relative to the current working directory
		String line = "";
        String delimiter = ":";
        int numRows = 0;
        int numCols = 0;
        String[][] data = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            /*
             *  count number of rows and columns in file
             */
        	
            String[] firstLine = br.readLine().split(delimiter);
            numCols = firstLine.length;
            numRows = 1;
            while ((line = br.readLine()) != null) {
                numRows++;
            }

            /*
             *  initialize 2D array with correct dimensions
             */
            
            data = new String[numRows][numCols];

            /*
             *  read in data from file and populate array
             */
            
            br.close();
            BufferedReader abr = new BufferedReader(new FileReader(filePath));
            int row = 0;
            while ((line = abr.readLine()) != null) {
                String[] values = line.split(delimiter);
                for (int col = 0; col < numCols; col++) {
                    data[row][col] = values[col];
                }
                row++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
		if(select_cols[0].indexOf("*") != -1) {
	        /*
	         *  print out contents of 2D array
	         */
			
	        for (int row = 0; row < numRows; row++) {
	            for (int col = 0; col < numCols; col++) {
	                System.out.print(data[row][col] + " ");
	            }
	            System.out.println();
	        }
			
		}
		else {
			List<Integer> myCols = new ArrayList<Integer>();
			for(String col_name: select_cols) {
	            for (int col = 0; col < numCols; col++) {
	                if(data[0][col].indexOf(col_name) != -1) {
	                	myCols.add(col);
	                }
	            }
			}

			
			for (int row = 0; row < numRows; row++) {
				for (int col : myCols) {
	                System.out.print(data[row][col] + " ");
	            }
	            System.out.println();
	        }
		}
	}
	
	/**

    Deletes records from a table based on the given column and condition.

    @param table the name of the table to delete from

    @param column_name the name of the column to check for the condition

    @param condition the value to check against in the specified column
    */
	
	
	public void delete_from_table(String table, String column_name, String condition) {
		
		String filePath = this.db_name+"/"+table+".txt"; // file path relative to the current working directory
		String line = "";
        String delimiter = ":";
        int numRows = 0;
        int numCols = 0;
        String[][] data = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            
            String[] firstLine = br.readLine().split(delimiter);
            numCols = firstLine.length;
            numRows = 1;
            while ((line = br.readLine()) != null) {
                numRows++;
            }

            
            data = new String[numRows][numCols];

            /*
             *  read in data from file and populate array
             */
            
            br.close();
            BufferedReader abr = new BufferedReader(new FileReader(filePath));
            int row = 0;
            while ((line = abr.readLine()) != null) {
                String[] values = line.split(delimiter);
                for (int col = 0; col < numCols; col++) {
                    data[row][col] = values[col];
                }
                row++;
            }
            abr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
		List<Integer> RowToDel = new ArrayList<Integer>();
		
		for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if(data[row][col].trim().equals(condition.trim())) {
                	RowToDel.add(row);
                	
                }
            }
        }
		
		
		
		try {
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        StringBuilder sb = new StringBuilder();

        try {
            int lineNumber = 0;
            String lineToRead;
            while ((lineToRead = reader.readLine()) != null) {

                if (RowToDel.contains(lineNumber)) {
                    lineNumber++;
                    continue;
                }

                sb.append(lineToRead);
                sb.append(System.lineSeparator());
                lineNumber++;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sb.toString());
            reader.close();
            writer.close();
            System.out.println("Record deleted successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
   }
	
	/**

    Updates a record in a specified table by setting the value of a specified column

    to a specified value where the value of a specified column matches a specified condition.

    The table is a text file with the data separated by colons (:).

    @param table the name of the table to update

    @param column_name the name of the column to use for the condition

    @param condition the value of the column to use as the condition

    @param column_set the name of the column to update

    @param value_set the value to set in the updated column
    */
	
	
public void update_table(String table, String column_name, String condition, String column_set, String value_set) {
		
		String filePath = this.db_name+"/"+table+".txt"; // file path relative to the current working directory
		String line = "";
        String delimiter = ":";
        int numRows = 0;
        int numCols = 0;
        String[][] data = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

           
            String[] firstLine = br.readLine().split(delimiter);
            numCols = firstLine.length;
            numRows = 1;
            while ((line = br.readLine()) != null) {
                numRows++;
            }

            
            data = new String[numRows][numCols];

            
            br.close();
            BufferedReader abr = new BufferedReader(new FileReader(filePath));
            int row = 0;
            while ((line = abr.readLine()) != null) {
                String[] values = line.split(delimiter);
                for (int col = 0; col < numCols; col++) {
                    data[row][col] = values[col];
                }
                row++;
            }
            abr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
		List<Integer> RowToDel = new ArrayList<Integer>();
		
		for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if(data[row][col].trim().equals(condition.trim())) {
                	RowToDel.add(row);
                	
                }
            }
        }
		
		
		try {
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        StringBuilder sb = new StringBuilder();

        try {
            int lineNumber = 0;
            String lineToRead;
            while ((lineToRead = reader.readLine()) != null) {

                if (RowToDel.contains(lineNumber)) {
                	List<String> update_line = new ArrayList<String>();
                	for (int col = 0; col < numCols; col++) {
                        if(data[0][col].trim().equals(column_set.trim())) {
                        	update_line.add(value_set);
                        }
                        else {
                        	update_line.add(data[lineNumber][col]);
                        }
                    }
                	lineToRead = String.join(":", update_line);
                }

                sb.append(lineToRead);
                sb.append(System.lineSeparator());
                lineNumber++;
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sb.toString());
            reader.close();
            writer.close();
            System.out.println("Record updated successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
   }
	
	
	
	
	public void input_query() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please entry your query or enter 'exit' to logout:");
        String query = scanner.nextLine().trim();
        query = query.toLowerCase();
        query = query.replace(";", "");
        
        String[] query_words = query.split(" ");
        
        
        do {

        
        if(query_words[0].indexOf("create") != -1){
        	String table_name = query_words[2];
        	
        	String[] words_to_interate = Arrays.copyOfRange(query_words, 3, query_words.length);
        	List<String> Cols = new ArrayList<String>();
        	for(String col: words_to_interate) {
        		if(col.indexOf("varchar")==-1 && col.indexOf("int")==-1 && col.indexOf("bool")==-1 && col.indexOf("(")==-1 && col.indexOf(")")==-1) {
        			Cols.add(col);
        		}
        	}
        	create_table(table_name, Cols);
        }
        
        if(query_words[0].indexOf("insert") != -1){
        	String table_name = query_words[2];
        	
        	int startIndex = query.indexOf("(");
        	int endIndex = query.indexOf(")", startIndex + 1);
        	
        	String substring = query.substring(startIndex + 1, endIndex);
        	
        	String[] insert_values = substring.split(",");
        	
        	List<String> insert_values_list = Arrays.asList(insert_values);
        
        	insert_into_table(table_name, insert_values_list);
        }
        
        
        if(query_words[0].indexOf("select") != -1){
        	String table_name = query_words[query_words.length - 1];
        	
        	String[] col_select = Arrays.copyOfRange(query_words, 1, query_words.length - 1);
        	
        	
        
        	select_from_table(table_name, col_select);
        }
        
        if(query_words[0].indexOf("delete") != -1){
        	String table_name = query_words[2];
        	String column_name = query_words[query_words.length-3];
        	String condition = query_words[query_words.length-1];
        	condition = condition.replace("\"", "");
        	condition = condition.replace("\"", "");
        
        	delete_from_table(table_name, column_name, condition);
        }
        
        if(query_words[0].indexOf("update") != -1){
        	String table_name = query_words[1];
        	String column_name = query_words[query_words.length-3];
        	String condition = query_words[query_words.length-1];
        	condition = condition.replace("\"", "");
        	condition = condition.replace("\"", "");
        	
        	String column_set = query_words[3];
        	String value_set = query_words[5];
        	value_set = value_set.replace("\"", "");
        	value_set = value_set.replace("\"", "");
        
        	update_table(table_name, column_name, condition, column_set, value_set);
        }
        
        System.out.println("Please entry your query or enter 'exit' to logout:");
        query = scanner.nextLine().trim();
        query = query.toLowerCase();
        query = query.replace(";", "");
        
        query_words = query.split(" ");
        
        } while(!query.equals("exit"));     
        
	}
	
	

}
