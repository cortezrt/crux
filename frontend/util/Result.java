package frontend.util;

public class Result{
    public enum kind{
        Constant,
        Variable,
        Register;
    }
    private kind k;
    private int val;
    private int addr;
    private int register;

    public Result() {}
    public Result(kind valKind, int num) {
        k = valKind;
        switch (valKind) {
            case Constant:
                this.val = num;break;
            case Variable:
                this.addr = num;break;
            case Register:
                this.register = num;break;
            default:
                break;
        }
    }
    public int getResult() {
        switch (k) {
            case Constant:
                return this.val;
            case Variable:
                return this.addr;
            default:
                return this.register;
        }
    }
    public kind getKind() {
        return k;
    }

    public void update(int num) {
        switch (k) {
            case Constant:
                this.val = num;
            case Variable:
                this.addr = num;
            case Register:
                this.register = num;
            default:
                break;
        }
    }
    public void negate() {
        if (val == 0) val = 1;
        else val = 0;
    }


}
