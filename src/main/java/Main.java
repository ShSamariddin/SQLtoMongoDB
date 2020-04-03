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
            Converter converter = new Converter(sql.toString());
            System.out.println(converter.convertToMongoDB());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
