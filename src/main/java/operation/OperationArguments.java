/**
 * CLass OperationArguments
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для хранения  аргументов FROM и SELECT
 */
public class OperationArguments {


    /**
     * Purpose: Удаляет обертку аргумента
     *
     * @param str
     * @return Возвращает аргумент без обертки
     */
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


}
