package com.symag.sawtooth.exceptions;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;

@JsonRpcError(code = -32033)
public class InstanceAlreadyInitializedException extends Exception {
    public InstanceAlreadyInitializedException(String message) {
        super(message);
    }
}