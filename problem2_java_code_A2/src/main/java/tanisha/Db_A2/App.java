
/**

    The App class is the entry point of the application.
    It creates a list of keywords and passes them to the ABC_functions class to get news related to each keyword.
    @author Tanisha
    
    */
package tanisha.Db_A2;
import java.util.*;

/**
 * The `main` method is the entry point of the application.
 * It creates a list of keywords and passes them to the `ABC_functions` class to get news related to each keyword.
 * 
 */

public class App 
{
    public static void main( String[] args )
    {
    	List<String> keywords = new ArrayList<>();
    	keywords.add("Canada");
    	keywords.add("University");
    	keywords.add("Dalhousie");
    	keywords.add("Halifax");
    	keywords.add("Canada Education");
    	keywords.add("Moncton");
    	keywords.add("hockey");
    	keywords.add("Fredericton");
    	keywords.add("celebration");
    	keywords.add("university");
    	keywords.add("dalhousie");
    	keywords.add("halifax");
    	keywords.add("Canada education");
    	keywords.add("Moncton");
    	keywords.add("Hockey");
    	keywords.add("fredericton");
    	keywords.add("Celebration");
    	
    	
    	
    	for (String str : keywords) {
    		ABC_functions ABC_functions_obj = new ABC_functions(str);
    		ABC_functions_obj.get_news();

        }
    }
}
