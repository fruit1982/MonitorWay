package cn.mw.monitor.api.exception;

import lombok.Data;

@Data
public class CheckInsertUserException extends RuntimeException {

    private String msg;

    private int code;

    public CheckInsertUserException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

}
