package com.symag.sawtooth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.arteam.simplejsonrpc.core.domain.ErrorResponse;
import com.github.arteam.simplejsonrpc.server.JsonRpcServer;
import sawtooth.sdk.processor.Context;
import sawtooth.sdk.processor.TransactionHandler;
import sawtooth.sdk.processor.Utils;
import sawtooth.sdk.processor.exceptions.InternalError;
import sawtooth.sdk.processor.exceptions.InvalidTransactionException;
import sawtooth.sdk.protobuf.TpProcessRequest;
import sawtooth.sdk.protobuf.TransactionHeader;

/**
 * 
 * Handler class for sawtooth processor just keep data as it is sent.
 * 
 */
public class ERC20Handler implements TransactionHandler {
	private final Logger logger = Logger.getLogger(ERC20Handler.class.getName());

	private static final String familyName = "erc20";
	private static final String version = "1.0";
	private String transactionFamilyNameSpace;
	private JsonRpcServer rpcServer;
	private ObjectMapper mapper;
	/**
	 * Constructor.
	 */
	public ERC20Handler() {
		this.transactionFamilyNameSpace = Utils.hash512(this.transactionFamilyName().getBytes(StandardCharsets.UTF_8)).substring(0, 6);
		logger.info("Namespace prefix calculated as - " + this.transactionFamilyNameSpace);
		rpcServer = new JsonRpcServer();
		mapper = new ObjectMapper();
	}

	@Override
	public String transactionFamilyName() {
		return familyName;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public Collection<String> getNameSpaces() {
		ArrayList<String> namespaces = new ArrayList<>();
		namespaces.add(this.transactionFamilyNameSpace);
		return namespaces;
	}

	/*
	 * apply()
	 *
	 * This method is invoked for each transaction the validator gets from the client
	 *
	 * @param: transactionRequest - contains the transaction
	 * @param: state - contains the state context
	 * @returns: void
	 * @throws: InvalidTransactionException, InternalError
	 *
	 */
	@Override
	public void apply(TpProcessRequest transactionRequest, Context state) throws InvalidTransactionException, InternalError {
		String signerPublicKey;
		String instanceName;
		String rpcMethod;

		if (transactionRequest.getPayload().size() == 0) {
			throw new InvalidTransactionException("JSON-RPC payload is required.");
		}

   	    TransactionHeader header = transactionRequest.getHeader();
        String payload = transactionRequest.getPayload().toStringUtf8();

		try {
			BlocksyCall payloadMap = mapper.readValue(payload, BlocksyCall.class);
			instanceName = (String) payloadMap.instance_name;
			rpcMethod = (String) payloadMap.rpc;
			signerPublicKey = header.getSignerPublicKey();
			//logger.info("instanceName=" + instanceName + " signerPublicKey=" + signerPublicKey + " rpcMethod= " + rpcMethod);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			throw new InvalidTransactionException("Failed to decode Blocksy's payload");
		}

		if (instanceName.isEmpty()) {
			throw new InvalidTransactionException("instanceName is required");
		}
		if (rpcMethod.isEmpty()) {
			throw new InvalidTransactionException("rpcMethod is required");
		}

		ERC20DetailedService erc20DetailedService = new ERC20DetailedService(transactionFamilyNameSpace, instanceName, signerPublicKey, state);

		//call the matching ERC20DetailedService method as specified in JSON-RPC document
		String response = rpcServer.handle(rpcMethod, erc20DetailedService);

		ErrorResponse errorResponse = null;
		try {
			errorResponse = mapper.readValue(response, ErrorResponse.class);
		} catch (IOException e) {
			//in case of exception, the result is not of ErrorResponse type
		}

		if (errorResponse == null) {
			//no errors
			logger.info("rpc response=" + response);
		} else {
			logger.severe("rpc response=" + response);

			//still calling in a loop the called method with InternalError, even with the fix :
			//https://github.com/hyperledger/sawtooth-core/pull/1994
			//throw new InternalError(errorResponse.getError().getMessage());
			throw new InvalidTransactionException(errorResponse.getError().getMessage());
		}
	}
}
