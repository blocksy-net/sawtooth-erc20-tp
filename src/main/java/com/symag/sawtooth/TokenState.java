package com.symag.sawtooth;

import com.google.protobuf.ByteString;
import com.symag.sawtooth.exceptions.InstanceAlreadyInitializedException;
import com.symag.sawtooth.exceptions.ReadWriteStateException;
import sawtooth.sdk.processor.Context;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TokenState {
    private static final String PREFIX_ADR_INSTANCE = "instance";
    private static final String PREFIX_ADR_BALANCE = "balance";
    private static final String PREFIX_ADR_ALLOWANCE = "allowance";
    private String totalSupplyAddress;
    private String capAddress;
    private String nameAddress;
    private String decimalsAddress;
    private String symbolAddress;
    private Context state;
    private String transactionFamilyNameSpace;
    private String instanceName;

    public TokenState(Context state, String transactionFamilyNameSpace, String instanceName) {
        this.state = state;
        this.transactionFamilyNameSpace = transactionFamilyNameSpace;
        this.instanceName = instanceName;
        //Instance relative addresses are set to null because initialized later at first use
        this.totalSupplyAddress = null;
        this.capAddress = null;
        this.nameAddress = null;
        this.decimalsAddress = null;
        this.symbolAddress = null;
    }

    public void initInstance(String ownerAccount) throws ReadWriteStateException, InstanceAlreadyInitializedException {
        //Check if instance is already initialized ?
        String ownerPublicKeyAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_INSTANCE, "ownerId");
        Map<String, ByteString> result = null;
        result = SawtoothHelper.getState(state, Collections.singletonList(ownerPublicKeyAddress));
        if (! result.get(ownerPublicKeyAddress).isEmpty() ) {
            throw new InstanceAlreadyInitializedException("Instance " + instanceName + " is already initialized.");
        }
        //Set instance as initialized
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_INSTANCE, "ownerId"), ownerAccount));
        SawtoothHelper.setState(state, addressValues);
    }

    private void initCapAddress() {
        if (capAddress == null)
            capAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_INSTANCE, "cap");
    }

    public BigInteger getCap() throws ReadWriteStateException {
        BigInteger cap = BigInteger.ZERO;
        initCapAddress();
        Map<String, ByteString> result = SawtoothHelper.getState(state, Collections.singletonList(capAddress));
        if (! result.get(capAddress).isEmpty())  {
            cap = new BigInteger(SawtoothHelper.decodeState(result));
        }
        return cap;
    }

    public void setCap(BigInteger cap) throws ReadWriteStateException {
        initCapAddress();
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(capAddress, String.valueOf(cap)));
        SawtoothHelper.setState(state, addressValues);
    }

    private void initTotalSupplyAddress() {
        if (totalSupplyAddress == null)
            totalSupplyAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_INSTANCE, "totalSupply");
    }

    public BigInteger getTotalSupply() throws ReadWriteStateException {
        BigInteger totalSupply = BigInteger.ZERO;
        initTotalSupplyAddress();
        Map<String, ByteString> result = SawtoothHelper.getState(state, Collections.singletonList(totalSupplyAddress));
        if (! result.get(totalSupplyAddress).isEmpty())  {
            totalSupply = new BigInteger(SawtoothHelper.decodeState(result));
        }
        return totalSupply;
    }

    public void setTotalSupply(BigInteger amount) throws ReadWriteStateException {
        initTotalSupplyAddress();
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(totalSupplyAddress, String.valueOf(amount)));
        SawtoothHelper.setState(state, addressValues);
    }

    private void initDecimalsAddress() {
        if (decimalsAddress == null)
            decimalsAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_INSTANCE, "decimals");
    }

    public Integer getDecimals() throws ReadWriteStateException {
        int decimals = 0;
        initDecimalsAddress();
        Map<String, ByteString> result = SawtoothHelper.getState(state, Collections.singletonList(decimalsAddress));
        if (! result.get(decimalsAddress).isEmpty())  {
            decimals = Integer.parseInt(SawtoothHelper.decodeState(result));
        }
        return decimals;
    }

    public void setDecimals(Integer decimals) throws ReadWriteStateException {
        initDecimalsAddress();
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(decimalsAddress, String.valueOf(decimals)));
        SawtoothHelper.setState(state, addressValues);
    }

    private void initNameAddress() {
        if (nameAddress == null)
            nameAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_INSTANCE, "name");
    }
    public String getName() throws ReadWriteStateException {
        String name = "";
        initNameAddress();
        Map<String, ByteString> result = SawtoothHelper.getState(state, Collections.singletonList(nameAddress));
        if (! result.get(nameAddress).isEmpty())  {
            name = SawtoothHelper.decodeState(result);
        }
        return name;
    }

    public void setName(String name) throws ReadWriteStateException {
        initNameAddress();
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(nameAddress, String.valueOf(name)));
        SawtoothHelper.setState(state, addressValues);
    }

    private void initSymbolAddress() {
        if (symbolAddress == null)
            symbolAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_INSTANCE, "symbol");
    }

    public String getSymbol() throws ReadWriteStateException {
        String symbol = "";
        initSymbolAddress();
        Map<String, ByteString> result = SawtoothHelper.getState(state, Collections.singletonList(symbolAddress));
        if (! result.get(symbolAddress).isEmpty())  {
            symbol = SawtoothHelper.decodeState(result);
        }
        return symbol;
    }

    public void setSymbol(String symbol) throws ReadWriteStateException {
        initSymbolAddress();
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(symbolAddress, String.valueOf(symbol)));
        SawtoothHelper.setState(state, addressValues);
    }

    public BigInteger getBalance(String account) throws ReadWriteStateException {
        String balanceAccountAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_BALANCE, account);

        BigInteger balance = BigInteger.ZERO;
        Map<String, ByteString> result = SawtoothHelper.getState(state, Collections.singletonList(balanceAccountAddress));
        if (! result.get(balanceAccountAddress).isEmpty())  {
            balance = new BigInteger(SawtoothHelper.decodeState(result));
        }
        return balance;
    }

    public void setBalance(String account, BigInteger amount) throws ReadWriteStateException {
        String balanceAccountAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_BALANCE, account);
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(balanceAccountAddress, String.valueOf(amount)));
        SawtoothHelper.setState(state, addressValues);
    }

    public BigInteger getAllowance(String ownerAccount, String spenderAccount) throws ReadWriteStateException {
        String allowanceAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_ALLOWANCE, ownerAccount + "|" + spenderAccount);

        BigInteger amount = BigInteger.ONE.negate();
        Map<String, ByteString> result = SawtoothHelper.getState(state, Collections.singletonList(allowanceAddress));
        if (! result.get(allowanceAddress).isEmpty())  {
            amount = new BigInteger(SawtoothHelper.decodeState(result));
        }
        return amount;
    }

    public void setAllowance(String ownerAccount, String spenderAccount, BigInteger amount) throws ReadWriteStateException {
        String allowanceAddress = SawtoothHelper.getUniqueAddress(transactionFamilyNameSpace, instanceName, PREFIX_ADR_ALLOWANCE, ownerAccount + "|" + spenderAccount);
        Collection<Map.Entry<String, ByteString>> addressValues = Collections.singletonList(SawtoothHelper.encodeState(allowanceAddress, String.valueOf(amount)));
        SawtoothHelper.setState(state, addressValues);
    }

}
