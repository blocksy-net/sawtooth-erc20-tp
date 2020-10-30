package com.symag.sawtooth;

import sawtooth.sdk.processor.TransactionProcessor;

import java.util.logging.Logger;

public class ERC20TransactionProcessor {
	private final static Logger logger = Logger.getLogger(ERC20TransactionProcessor.class.getName());
	/**
	 * the method that runs a Thread with a TransactionProcessor in it.
	 */
	public static void main(String[] args) {
		//logger.info("Arg 0 = " + args[0]);
		TransactionProcessor transactionProcessor = new TransactionProcessor(args[0]);
		transactionProcessor.addHandler(new ERC20Handler());
		Thread thread = new Thread(transactionProcessor);
		thread.start();
	}
}