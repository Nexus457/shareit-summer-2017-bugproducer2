package edu.hm.bugproducer.Status;


/**
 * StatusMgnt Class.
 * @author Mark Tripolt
 * @author Johannes Arzt
 * @author Tom Maier
 * @author Patrick Kuntz
 */
public class StatusMgnt {

    /**
     * MediaServiceResult variable for result
     */
    private MediaServiceResult result;
    /**
     * message variable
     */
    private String msg;
    /**
     * code variable
     */
    private int code;

    /**
     * StatusMgnt Constructor.
     *
     */
    public StatusMgnt() {
        result = MediaServiceResult.MSR_INTERNAL_SERVER_ERROR;
        msg = "Its over!!!";
        code = result.getCode();

    }

    /**
     * StatusMgnt Constructor.
     * @param result a MediaServiceResult
     * @param msg message
     */
    public StatusMgnt(MediaServiceResult result, String msg) {
        this.result = result;
        this.msg = msg;
        this.code = result.getCode();
    }

    /**
     * getResult method.
     * gets the MediaServiceResult
     * @return result
     */
    public MediaServiceResult getResult() {
        return result;
    }

    /**
     * setResult method.
     * sets the result
     * @param result MediaServiceResult
     */
    public void setResult(MediaServiceResult result) {
        this.result = result;
    }

    /**
     * getMsg method.
     * gets the message
     * @return message
     */
    public String getMsg() {
        return msg;
    }

    /**
     * setMsg method.
     * sets the message
     * @param msg message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * getCode method.
     * gets the code
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * setCode method.
     * sets the code
     * @param code specific code
     */
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatusMgnt)) {
            return false;
        }

        StatusMgnt that = (StatusMgnt) o;

        if (getCode() != that.getCode()) {
            return false;
        }
        if (getResult() != that.getResult()) {
            return false;
        }
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
