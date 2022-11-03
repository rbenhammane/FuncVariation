package com.eulith.co.variation.contracts;

import com.eulith.co.variation.FuncVariation;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Collections;

//
// Java wrapper generator does not properly handle payable functions (https://github.com/web3j/web3j/issues/1268)
// so this class was added to handle payable in the payExactToIncrement function.
//
public class PayableFuncVariation extends FuncVariation {

    protected PayableFuncVariation(String contractAddress, Web3j web3j, TransactionManager transactionManager,
                                   ContractGasProvider contractGasProvider) {
        super(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<PayableFuncVariation> deployPayable(Web3j web3j, TransactionManager transactionManager,
                                                                 ContractGasProvider contractGasProvider) {
        return deployRemoteCall(PayableFuncVariation.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    public RemoteFunctionCall<TransactionReceipt> payExactToIncrement(BigInteger val, BigInteger value) {
        final Function function = new Function(
                FUNC_PAYEXACTTOINCREMENT,
                Collections.singletonList(new Uint256(val)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function, value);
    }
}
