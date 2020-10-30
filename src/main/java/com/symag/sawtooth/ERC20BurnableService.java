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
 * Extension of {ERC20} that allows token holders to destroy both their own
 * tokens and those that they have an allowance for, in a way that can be
 * recognized off-chain (via event analysis).
 */
public class ERC20BurnableService extends ERC20Service {

    ERC20BurnableService(String transactionFamilyNameSpace, String instanceName, String signerPublicKey, Context state) {
        super(transactionFamilyNameSpace, instanceName, signerPublicKey, state);
    }

    /**
     * Destroys `amount` tokens from the caller.
     *
     * See {ERC20-_burn}.
     */
    @JsonRpcMethod
    public void burn(@JsonRpcParam("amount")  BigInteger amount) throws ReadWriteStateException, RuleException {
        this._burn(this.signerPublicKey, amount);
    }

    /**
     * Destroys `amount` tokens from `account`.`amount` is then deducted
     * from the caller's allowance.
     *
     * See {ERC20-_burnFrom}.
     */
    @JsonRpcMethod
    public void burnFrom(@JsonRpcParam("account") String account,@JsonRpcParam("amount")  BigInteger amount) throws ReadWriteStateException, RuleException {
        this._burnFrom(account, amount);
    }

    @Override
    protected void init(BigInteger amount) throws TransactionOwnerException, InstanceAlreadyInitializedException, ReadWriteStateException, RuleException {
        super.init(amount);
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
