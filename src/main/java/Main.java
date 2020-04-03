import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StringBuilder sql = new StringBuilder();

        while (input.hasNextLine()) {
            if (sql.length() != 0) {
                sql.append('\n');
            }
            sql.append(input.nextLine());
        }
        try {
            Converter converter = new Converter();
            System.out.println(converter.convertToMongoDB(sql.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
