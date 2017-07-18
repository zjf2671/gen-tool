package com.daydaycook.ddc.jdbc.conn;

/**
 * CreateConnectException
 * 
 * @author guannan.shang
 * @date 2014-12-25 17:41:51
 * @version 1.0
 */
public class CreateConnectException extends RuntimeException {
    
    private static final long serialVersionUID = 2880660731260737727L;
    
    public CreateConnectException(String message) {
        super(message);
    }
    
    public CreateConnectException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public CreateConnectException(Throwable throwable) {
        super(throwable);
    }
}
