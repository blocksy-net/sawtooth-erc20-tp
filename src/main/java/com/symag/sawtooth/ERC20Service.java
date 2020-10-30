package com.symag.sawtooth;

import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcMethod;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcParam;
import com.github.arteam.simplejsonrpc.core.annotation.JsonRpcService;
import com.symag.sawtooth.exceptions.InstanceAlreadyInitializedException;
import com.symag.sawtooth.exceptions.ReadWriteStateException;
import com.symag.sawtooth.exceptions.RuleException;
import com.symag.sawtooth.exceptions.TransactionOwnerException;
import sawtooth.sdk.processor.Context;

import java.math.BigInteger;
import java.util.logging.Logger;

@JsonRpcService
class ERC20Service implements IERC20 {
    protected String signerPublicKey;
    protected TokenState tokenState;

    protected final Logger logger = Logger.getLogger(ERC20Service.class.getName());

    ERC20Service(String transactionFamilyNameSpace, String instanceName, String signerPublicKey, Context state) {
        this.signerPublicKey = signerPublicKey;
        tokenState = new TokenState(state, transactionFamilyNameSpace, instanceName);
    }

    @JsonRpcMethod
    protected void init(@JsonRpcParam("amount") BigInteger amount) throws TransactionOwnerException, InstanceAlreadyInitializedException, ReadWriteStateException,RuleException {
        logger.info("called init !");

        //create an unique instance or throw an exception if instance is already initialized
        tokenState.initInstance(this.signerPublicKey);

        //Init token
        _mint(this.signerPublicKey, amount);
    }

    /**
     * Moves `amount` tokens from the caller's account to `recipient`.
     *
     * @param recipient
     * @param amount
     */
    @JsonRpcMethod @Override
    public void transfer(@JsonRpcParam("recipient") String recipient, @JsonRpcParam("amount") BigInteger amount) throws Exception {
        _transfer(this.signerPublicKey, recipient, amount);
    }

    /**
     * Sets `amount` as the allowance of `spender` over the caller's tokens.
     *
     * IMPORTANT: Beware that changing an allowance with this method brings the risk
     * that someone may use both the old and the new allowance by unfortunate
     * transaction ordering. One possible solution to mitigate this race
     * condition is to first reduce the spender's allowance to 0 and set the
     * desired value afterwards:
     * https://github.com/ethereum/EIPs/issues/20#issuecomment-263524729
     *
     * @param spender
     * @param amount
     */
    @JsonRpcMethod @Override
    public void approve(@JsonRpcParam("spender") String spender, @JsonRpcParam("amount") BigInteger amount) throws Exception {
        _approve(this.signerPublicKey, spender, amount);
    }

    /**
     * Moves `amount` tokens from `sender` to `recipient` using the
     * allowance mechanism. `amount` is then deducted from the caller's
     * allowance.
     *
     * @param sender
     * @param recipient
     * @param amount
     */
    @JsonRpcMethod @Override
    public void transferFrom(@JsonRpcParam("sender") String sender, @JsonRpcParam("recipient") String recipient, @JsonRpcParam("amount") BigInteger amount) throws Exception {
        _transfer(sender, recipient, amount);

        BigInteger allowance = tokenState.getAllowance(sender,this.signerPublicKey);
        if (amount.compareTo(allowance) > 0) throw new RuleException("ERC20: transfer amount exceeds allowance");
        _approve(sender, this.signerPublicKey, allowance.subtract(amount));
    }

    /**
     * Atomically increases the allowance granted to `spender` by the caller.
     *
     * This is an alternative to {approve} that can be used as a mitigation for
     * problems described in {IERC20-approve}.
     *
     * Requirements:
     *
     * - `spender` cannot be the zero address.
     */
    @JsonRpcMethod
    public void increaseAllowance(@JsonRpcParam("spender") String spender, @JsonRpcParam("addedValue") BigInteger addedValue) throws ReadWriteStateException, RuleException {
        BigInteger allowance = tokenState.getAllowance(this.signerPublicKey,spender);
        _approve(this.signerPublicKey, spender, allowance.add(addedValue));
    }

    /**
     * Atomically decreases the allowance granted to `spender` by the caller.
     *
     * This is an alternative to {approve} that can be used as a mitigation for
     * problems described in {IERC20-approve}.
     *
     * Requirements:
     *
     * - `spender` cannot be the zero address.
     * - `spender` must have allowance for the caller of at least `subtractedValue`.
     */
    @JsonRpcMethod
    public void decreaseAllowance(@JsonRpcParam("spender") String spender,@JsonRpcParam("subtractedValue") BigInteger subtractedValue) throws ReadWriteStateException, RuleException {
        BigInteger allowance = tokenState.getAllowance(this.signerPublicKey,spender);
        if (subtractedValue.compareTo(allowance) > 0) throw new RuleException("ERC20: decreased allowance below zero");
        _approve(this.signerPublicKey, spender, allowance.subtract(subtractedValue));
    }

    /** Creates `amount` tokens and assigns them to `account`, increasing
     * the total supply.
     *
     * Requirements
     *
     * - `account` cannot be the zero address.
     */
    protected void _mint(String account, BigInteger amount) throws RuleException, ReadWriteStateException {
        //logger.info("Call _mint : account=" + account + "amount=" + amount.toString() );
        if (account == null || account.equals("0"))
            throw new RuleException("ERC20: mint to the zero address");

        BigInteger totalSupply = tokenState.getTotalSupply();
        tokenState.setTotalSupply(totalSupply.add(amount));

        BigInteger accountBalance = tokenState.getBalance(account);
        tokenState.setBalance(account, accountBalance.add(amount));
    }

    /**
     * Moves tokens `amount` from `sender` to `recipient`.
     *
     * This is internal function is equivalent to {transfer}, and can be used to
     * e.g. implement automatic token fees, slashing mechanisms, etc.
     *
     * Requirements:
     *
     * - `sender` cannot be the zero address.
     * - `recipient` cannot be the zero address.
     * - `sender` must have a balance of at least `amount`.
     */
    protected void _transfer(String sender, String recipient, BigInteger amount) throws RuleException, ReadWriteStateException {
        if (sender == null || sender.equals("0")) throw new RuleException("ERC20: transfer from the zero address");
        if (recipient == null || recipient.equals("0")) throw new RuleException("ERC20: transfer to the zero address");

        BigInteger balanceSender = tokenState.getBalance(sender);
        BigInteger balanceRecipient = tokenState.getBalance(recipient);

        if (amount.compareTo(balanceSender) > 0) throw new RuleException("ERC20: transfer amount exceeds balance");

        tokenState.setBalance(sender,balanceSender.subtract(amount));
        tokenState.setBalance(recipient,balanceRecipient.add(amount));
    }

    /**
     * Sets `amount` as the allowance of `spender` over the `owner`s tokens.
     *
     * This is internal function is equivalent to `approve`, and can be used to
     * e.g. set automatic allowances for certain subsystems, etc.
     *
     * Requirements:
     *
     * - `owner` cannot be the zero address.
     * - `spender` cannot be the zero address.
     */
    protected void _approve(String owner, String spender, BigInteger amount) throws RuleException, ReadWriteStateException {
        if (owner == null || owner.equals("0")) throw new RuleException("ERC20: approve from the zero address");
        if (spender == null || spender.equals("0")) throw new RuleException("ERC20: approve to the zero address");

        tokenState.setAllowance(owner, spender, amount);
    }

    /**
     * Destroys `amount` tokens from `account`, reducing the
     * total supply.
     *
     * Requirements
     *
     * - `account` cannot be the zero address.
     * - `account` must have at least `amount` tokens.
     */
    protected void _burn(String account, BigInteger amount) throws RuleException, ReadWriteStateException {
        if (account == null || account.equals("0")) throw new RuleException("ERC20: burn from the zero address");
        BigInteger accountBalance = tokenState.getBalance(account);
        if (amount.compareTo(accountBalance) > 0) throw new RuleException("ERC20: burn amount exceeds balance");
        tokenState.setBalance(account,accountBalance.subtract(amount));
        BigInteger totalSupply = tokenState.getTotalSupply();
        tokenState.setTotalSupply(totalSupply.subtract(amount));
    }

    /**
     * Destroys `amount` tokens from `account`.`amount` is then deducted
     * from the caller's allowance.
     *
     * See {_burn} and {_approve}.
     */
    protected void _burnFrom(String account, BigInteger amount) throws ReadWriteStateException, RuleException {
        _burn(account, amount);
        BigInteger allowance = tokenState.getAllowance(account,this.signerPublicKey);
        if (amount.compareTo(allowance) > 0) throw new RuleException("ERC20: burn amount exceeds allowance");
        _approve(account, this.signerPublicKey, allowance.subtract(amount));
    }
}