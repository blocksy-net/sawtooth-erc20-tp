package com.symag.sawtooth;

import java.math.BigInteger;

public interface IERC20 {
    /**
     * Moves `amount` tokens from the caller's account to `recipient`.
     */
    public void transfer(String recipient, BigInteger amount) throws Exception;

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
     */
    public void approve(String spender, BigInteger amount) throws Exception;

    /**
     * Moves `amount` tokens from `sender` to `recipient` using the
     * allowance mechanism. `amount` is then deducted from the caller's
     * allowance.
     *
     */
    public void transferFrom(String sender, String recipient, BigInteger amount) throws Exception;

}
