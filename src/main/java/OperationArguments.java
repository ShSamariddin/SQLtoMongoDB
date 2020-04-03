public class OperationArguments {
    protected boolean isLegalCharacter(char c) {
        return (c == '_' || (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }

    protected String removeWrapper(String str) {
        StringBuilder arg = new StringBuilder(str);
        if (arg.length() <= 2) {
            return arg.toString();
        }
        for (Wrapper wrapper : Wrapper.values()) {
            if (arg.charAt(0) == wrapper.openWrapper() && arg.charAt(arg.length() - 1) == wrapper.closeWrapper()) {
                arg.deleteCharAt(0);
                arg.deleteCharAt(arg.length() - 1);
                return arg.toString();
            }
        }
        return arg.toString();
    }

    protected String checkCharacter(String str) {
        for (char c : str.toCharArray()) {
            if (!isLegalCharacter(c)) {
                throw new IllegalArgumentException("SELECT arguments has Illegal character");
            }
        }
        return str;
    }
}
