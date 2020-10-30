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
 * Extension of {ERC20Mintable} that adds a cap to the supply of tokens.
 */
public class ERC20CappedService extends ERC20MintableService {

    ERC20CappedService(String transactionFamilyNameSpace, String instanceName, String signerPublicKey, Context state) {
        super(transactionFamilyNameSpace, instanceName, signerPublicKey, state);
    }

    @JsonRpcMethod
    public void init(@JsonRpcParam("amount") BigInteger amount, @JsonRpcParam("cap") BigInteger cap) throws TransactionOwnerException, InstanceAlreadyInitializedException, ReadWriteStateException, RuleException {
        super.init(amount);
        tokenState.setCap(cap);
    }

    /**
     * @dev See {ERC20-_mint}.
     *
     * Requirements:
     *
     * - the caller must have the {MinterRole}.
     */
    @JsonRpcMethod @Override
    public void mint(@JsonRpcParam("account") String account,@JsonRpcParam("amount") BigInteger amount) throws ReadWriteStateException, RuleException {
        BigInteger totalSupply = tokenState.getTotalSupply();
        BigInteger cap = tokenState.getCap();
        if (totalSupply.add(amount).compareTo(cap) > 0) throw new RuleException("ERC20Capped: cap exceeded");
        super.mint(account, amount);
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
