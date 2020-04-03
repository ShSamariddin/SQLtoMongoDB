public class FromOperationArgument extends OperationArguments {
    String arg;


    public FromOperationArgument(String str) {
        this.arg = checkCharacter(removeWrapper(str));
    }

    public String toString() {
        return arg;
    }
}
