package ro.ase.csie.gabriel.pintea.ebuss.exceptions;

public class StartMenuException extends Exception {
    private int errCode;
    public StartMenuException() {
    }

    public StartMenuException(String message, int errCode) {
        super(message);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
