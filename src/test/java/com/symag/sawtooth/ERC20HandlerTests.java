package com.symag.sawtooth;

import com.github.arteam.simplejsonrpc.server.JsonRpcServer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sawtooth.sdk.processor.Context;

@RunWith(MockitoJUnitRunner.class)
public class ERC20HandlerTests extends ERC20Handler {

    @Mock
    Context state;

    @Test
    public void testRpcCallInit() {
        String rpcMethod = "{\"jsonrpc\":\"2.0\",\"method\":\"init\",\"params\":{\"symbol\":\"EUR\",\"amount\":20000,\"decimals\":18,\"name\":\"SimpleToken\"},\"id\":1}";
        JsonRpcServer rpcServer = new JsonRpcServer();
        ERC20DetailedService erc20DetailedService = new ERC20DetailedService("erc20", "erc20_dev10", "030e701fa7b833c4a75c344d34fc8c49f4c024478659cc02b0548142e03974bb6e", state);

        String response = rpcServer.handle(rpcMethod, erc20DetailedService);
        Assert.assertNotNull(response);
    }

    @Test
    public void testRpcCallTransfer() {
        String rpcMethod = "{\"jsonrpc\":\"2.0\",\"method\":\"transfer\",\"params\":{\"recipient\":\"030e701fa7b833c4a75c344d34fc8c49f4c024478659cc02b0548142e03974bb6e\",\"amount\":20000},\"id\":1}";
        JsonRpcServer rpcServer = new JsonRpcServer();
        ERC20DetailedService erc20DetailedService = new ERC20DetailedService("erc20", "erc20_dev10", "030e701fa7b833c4a75c344d34fc8c49f4c024478659cc02b0548142e03974bb6e", state);

        String response = rpcServer.handle(rpcMethod, erc20DetailedService);
        Assert.assertNotNull(response);
    }

}
