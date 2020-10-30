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
 * Extension of {ERC20} that adds a set of accounts with the {MinterRole},
 * which have permission to mint (create) new tokens as they see fit.
 *
 * At construction, the deployer of the contract is the only minter.
 */
public class ERC20MintableService extends ERC20Service {

    ERC20MintableService(String transactionFamilyNameSpace, String instanceName, String signerPublicKey, Context state) {
        super(transactionFamilyNameSpace, instanceName, signerPublicKey, state);
    }

    /**
     * @dev See {ERC20-_mint}.
     *
     * Requirements:
     *
     * - the caller must have the {MinterRole}.
     */
    @JsonRpcMethod
    public void mint(@JsonRpcParam("account") String account, @JsonRpcParam("amount") BigInteger amount) throws ReadWriteStateException, RuleException {
        //TODO: implement MinterRole for Sawtooth
        this._mint(account, amount);
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
