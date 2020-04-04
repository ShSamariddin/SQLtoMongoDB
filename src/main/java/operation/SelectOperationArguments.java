import parser.Var;

import java.util.ArrayList;

/**
 * CLass SelectOperationArguments
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для хранения  аргументов оператора SELECT
 */

public class SelectOperationArguments extends OperationArguments {
    ArrayList<Var> listArguments = new ArrayList<>();


    /**
     * Purpose: Разделяет запятыми и хранит все аргументы в ArrayList
     *
     * @param args
     * @throws IllegalArgumentException
     */
    public SelectOperationArguments(String args) throws IllegalArgumentException {
        args = args.replaceAll(" ", "");
        if(args.length() == 0 || args.charAt(0) == ',' || args.charAt(args.length() -  1) == ','){
            throw  new IllegalArgumentException("SELECT has an invalid argument");
        }
        String[] argList = args.split(",");
        if (argList.length > 1 || (argList.length == 1 && !argList[0].equals("*"))) {
            for (String s : argList) {
                listArguments.add(new Var(removeWrapper(s)));
            }
        }
    }

    /**
     * Purpose: Преврщает аргументы в MongoDB формат
     *
     * @return Возвращает аргументы в виде MongoDB формате
     */
    public String toMongoDB() {
        StringBuilder ans = new StringBuilder();

        if (listArguments.size() != 0) {
            ans.append(", {");
            for (Var arg : listArguments) {
                ans.append(arg.toString()).append(": 1, ");
            }
            ans.deleteCharAt(ans.length() - 1);
            ans.deleteCharAt(ans.length() - 1);
            ans.append("}");
        }
        return ans.toString();

    }

}
