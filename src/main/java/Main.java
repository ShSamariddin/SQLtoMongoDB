import javax.xml.transform.SourceLocator;
import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringBuilder sql = new StringBuilder();

        while(input.hasNextLine()){
            if(sql.length() != 0){
                sql.append('\n');
            }
            sql.append(input.nextLine());
        }
        try {
            Tree tree = new Tree();
            System.out.println(tree.sol(sql.toString()));
        } catch (Exception  e){
            e.printStackTrace();
        }
    }
}
