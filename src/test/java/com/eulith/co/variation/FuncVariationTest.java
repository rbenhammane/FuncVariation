package com.eulith.co.variation;

import com.eulith.co.variation.contracts.PayableFuncVariation;
import org.junit.jupiter.api.Test;
import org.web3j.EVMTest;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EVMTest
public class FuncVariationTest {

    @Test
    public void getCurrentCountTest(Web3j web3j, TransactionManager transactionManager,
                                    ContractGasProvider gasProvider) throws Exception {
        FuncVariation funcVariation = FuncVariation.deploy(web3j, transactionManager, gasProvider).send();
        BigInteger count = funcVariation.get().send();
        assertEquals(BigInteger.ZERO, count);
    }

    @Test
    public void incrementCountByOneTest(Web3j web3j, TransactionManager transactionManager,
                                        ContractGasProvider gasProvider) throws Exception {
        FuncVariation funcVariation = FuncVariation.deploy(web3j, transactionManager, gasProvider).send();
        funcVariation.inc().send();
        BigInteger count = funcVariation.get().send();
        assertEquals(BigInteger.ONE, count);
    }

    @Test
    public void decrementCountByOneWithZeroCountTest(Web3j web3j, TransactionManager transactionManager,
                                                     ContractGasProvider gasProvider) throws Exception {
        FuncVariation funcVariation = FuncVariation.deploy(web3j, transactionManager, gasProvider).send();
        TransactionException exception = assertThrows(TransactionException.class, () -> funcVariation.dec().send());
        assertNotNull(exception);

        TransactionReceipt transactionReceipt = exception.getTransactionReceipt().orElse(null);
        assertNotNull(transactionReceipt);
        assertTrue(exception.getMessage().contains("Transaction " + transactionReceipt.getTransactionHash() + " has " +
                "failed with status: 0x0."));
    }

    @Test
    public void decrementCountByOneTest(Web3j web3j, TransactionManager transactionManager,
                                        ContractGasProvider gasProvider) throws Exception {
        FuncVariation funcVariation = FuncVariation.deploy(web3j, transactionManager, gasProvider).send();
        funcVariation.inc().send();
        funcVariation.inc().send();
        BigInteger count = funcVariation.get().send();
        assertEquals(BigInteger.TWO, count);

        funcVariation.dec().send();
        count = funcVariation.get().send();
        assertEquals(BigInteger.ONE, count);
    }

    @Test
    public void incrementWithTest(Web3j web3j, TransactionManager transactionManager,
                                  ContractGasProvider gasProvider) throws Exception {
        FuncVariation funcVariation = FuncVariation.deploy(web3j, transactionManager, gasProvider).send();
        funcVariation.incWith(BigInteger.TEN).send();
        BigInteger count = funcVariation.get().send();
        assertEquals(BigInteger.TEN, count);
    }

    @Test
    public void payMeToIncrementTest(Web3j web3j, TransactionManager transactionManager,
                                     ContractGasProvider gasProvider) throws Exception {
        FuncVariation funcVariation = FuncVariation.deploy(web3j, transactionManager, gasProvider).send();
        funcVariation.payMeToIncrement(BigInteger.TEN).send();
        BigInteger count = funcVariation.get().send();
        assertEquals(BigInteger.TEN, count);
    }

    @Test
    public void payExactToIncrementTest(Web3j web3j, TransactionManager transactionManager,
                                        ContractGasProvider gasProvider) throws Exception {
        PayableFuncVariation funcVariation = PayableFuncVariation.deployPayable(web3j, transactionManager,
                gasProvider).send();
        funcVariation.payExactToIncrement(BigInteger.TEN, new BigInteger("10000000000000000")).send();
        BigInteger count = funcVariation.get().send();
        assertEquals(BigInteger.TEN, count);
    }

    @Test
    public void payExactToIncrementFailTest(Web3j web3j, TransactionManager transactionManager,
                                            ContractGasProvider gasProvider) throws Exception {
        PayableFuncVariation funcVariation = PayableFuncVariation.deployPayable(web3j, transactionManager,
                gasProvider).send();
        TransactionException exception = assertThrows(TransactionException.class,
                () -> funcVariation.payExactToIncrement(BigInteger.TEN, BigInteger.TEN).send());
        assertNotNull(exception);

        TransactionReceipt transactionReceipt = exception.getTransactionReceipt().orElse(null);
        assertNotNull(transactionReceipt);
        assertTrue(exception.getMessage().contains("Transaction " + transactionReceipt.getTransactionHash() + " has " +
                "failed with status: 0x0."));

        BigInteger count = funcVariation.get().send();
        assertEquals(BigInteger.ZERO, count);
    }
}
