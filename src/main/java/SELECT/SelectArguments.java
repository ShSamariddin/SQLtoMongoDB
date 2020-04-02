package SELECT;

import java.text.ParseException;
import java.util.ArrayList;

public class SelectArguments {
    ArrayList<String> listArguments = new ArrayList<>();

    public SelectArguments(String args) {
        separatedByComma(args);
    }

    private boolean isLegalCharacter(char c) {
        return (c == '\'' || c == '\"' || c == '-' || c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    private String checkCharacter(String str) {
        for (char c : str.toCharArray()) {
            if (!isLegalCharacter(c)) {
                throw new IllegalArgumentException("SELECT arguments has Illegal character");
            }
        }
        return str;
    }

    private void separatedByComma(String str) {
        StringBuilder arg = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == ',') {
                listArguments.add(checkCharacter(arg.toString()));
                arg = new StringBuilder();
            } else {
                arg.append(c);
            }
        }
        if (arg.length() != 0 && !arg.toString().equals("*")) {
            listArguments.add(arg.toString());
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
