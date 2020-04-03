import java.util.ArrayList;

public class SelectOperationArguments extends OperationArguments {
    ArrayList<String> listArguments = new ArrayList<>();

    public SelectOperationArguments(String args) {
        separatedByComma(args);
    }


    private void separatedByComma(String str) {
        String[] argList = str.split(",");
        if (argList.length > 1 || (argList.length == 1 && !argList[0].equals("*"))) {
            for (String s : argList) {
                listArguments.add(checkCharacter(removeWrapper(s)));
            }
        }
    }

    public String toString() {
        StringBuilder ans = new StringBuilder();

        if (listArguments.size() != 0) {
            ans.append(", {");
            for (String arg : listArguments) {
                ans.append(arg).append(": 1, ");
            }
            ans.deleteCharAt(ans.length() - 1);
            ans.deleteCharAt(ans.length() - 1);
            ans.append("}");
        }
        return ans.toString();

    }

}
