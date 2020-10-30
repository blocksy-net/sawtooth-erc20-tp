package com.symag.sawtooth.exceptions;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcError;

@JsonRpcError(code = -32032, message = "Transaction must be signed with owner")
public class TransactionOwnerException extends Exception {
}