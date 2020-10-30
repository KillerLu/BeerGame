package com.jnu.exception;

/**
 * @author ：Killer
 * @date ：Created in 20-10-22 下午4:49
 * @description：${description}
 * @modified By：
 * @version: version
 */
public class ServiceException extends RuntimeException {

    private String retCode;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String retCode, String message)
    {
        super(message);

        this.retCode = retCode;
    }


    public ServiceException(Throwable cause) {
        super(cause);
    }
}

