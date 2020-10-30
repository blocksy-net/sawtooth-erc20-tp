package com.symag.sawtooth;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.symag.sawtooth.exceptions.InstanceAlreadyInitializedException;
import com.symag.sawtooth.exceptions.ReadWriteStateException;
import com.symag.sawtooth.exceptions.RuleException;
import com.symag.sawtooth.exceptions.TransactionOwnerException;
import sawtooth.sdk.processor.Context;
import java.math.BigInteger;

/**
 * Optional functions from the ERC20 standard.
 */
public class ERC20DetailedService extends ERC20Service {
    ERC20DetailedService(String transactionFamilyNameSpace, String instanceName, String signerPublicKey, Context state) {
        super(transactionFamilyNameSpace, instanceName, signerPublicKey, state);
    }

    @JsonRpcMethod
    public void init(@JsonRpcParam("amount") BigInteger amount, @JsonRpcParam("name") String name, @JsonRpcParam("symbol") String symbol, @JsonRpcParam("decimals") Integer decimals) throws TransactionOwnerException, InstanceAlreadyInitializedException, ReadWriteStateException, RuleException {
        //logger.info("Call ERC20DetailedService.init : amount=" + amount.toString() + " name=" + name + " symbol=" + symbol + " decimals=" + decimals );
        super.init(amount);
        tokenState.setName(name);
        tokenState.setSymbol(symbol);
        tokenState.setDecimals(decimals);
    }

    @Override
    public void transfer(String recipient, BigInteger amount) throws Exception {
        super.transfer(recipient, amount);
    }

    @Override
    public void increaseAllowance(String spender, BigInteger addedValue) throws ReadWriteStateException, RuleException {
        super.increaseAllowance(spender, addedValue);
    }

    @Override
    public void decreaseAllowance(String spender, BigInteger subtractedValue) throws ReadWriteStateException, RuleException {
        super.decreaseAllowance(spender, subtractedValue);
    }

    @Override
    public void approve(String spender, BigInteger amount) throws Exception {
        super.approve(spender, amount);
    }

    @Override
    public void transferFrom(String sender, String recipient, BigInteger amount) throws Exception {
        super.transferFrom(sender, recipient, amount);
    }
}
