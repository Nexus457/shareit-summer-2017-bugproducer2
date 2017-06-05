package edu.hm.bugproducer.Status;



public class StatusMgnt {

    private MediaServiceResult result;
    private String msg;
    private int code;

    public StatusMgnt() {
        result= MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;
        msg= "Its over!!!";
        code= result.getCode();

    }

    public StatusMgnt(MediaServiceResult result, String msg) {
        this.result = result;
        this.msg = msg;
        this.code = result.getCode();
    }

    public MediaServiceResult getResult() {
        return result;
    }

    public void setResult(MediaServiceResult result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusMgnt)) return false;

        StatusMgnt that = (StatusMgnt) o;

        if (getCode() != that.getCode()) return false;
        if (getResult() != that.getResult()) return false;
        return getMsg().equals(that.getMsg());
    }

    @Override
    public int hashCode() {
        int result1 = getResult().hashCode();
        result1 = 31 * result1 + getMsg().hashCode();
        result1 = 31 * result1 + getCode();
        return result1;
    }
}
