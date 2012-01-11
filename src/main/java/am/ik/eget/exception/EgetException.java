package am.ik.eget.exception;

@SuppressWarnings("serial")
public class EgetException extends RuntimeException {

    public EgetException() {
    }

    public EgetException(String arg0) {
        super(arg0);
    }

    public EgetException(Throwable arg0) {
        super(arg0);
    }

    public EgetException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
