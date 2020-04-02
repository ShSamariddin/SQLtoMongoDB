package Parser;

public class Var {
    private  String arg;
    public Var(String var){
        if(!isValidVariable(var)){
            throw new IllegalArgumentException("unsupported characters in variables");
        }
        this.arg = var;
    }

    private boolean isValidVariable(String var){
        for(char c: var.toCharArray()){
            if(!isValidSymbol(c))
                return false;
        }
        return true;
    }

    private boolean isValidSymbol(char c){
        return (c == '\'' || c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }

    public String toString(){
        return arg;
    }
}
