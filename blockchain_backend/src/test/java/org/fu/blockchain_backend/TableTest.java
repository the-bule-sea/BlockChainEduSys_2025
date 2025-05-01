package org.fu.blockchain_backend;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TableTest {

    // 获取配置文件地址
    public final String configFile = "src/main/resources/config-example.toml";

    @Test
    public void testInsert() throws Exception{
        // 初始化BcosSDK对象
        BcosSDK sdk = BcosSDK.build(configFile);
        // 获取Client对象，此处传入的群组ID为1
        Client client = sdk.getClient(Integer.valueOf(1));
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
        // 只交易不部署
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "");

        // 同步发送交易，等待交易执行结果
        // 构造函数入参
        String name = "abcdefg";
        int item_id = 2;
        String item_name = "abcdefghijk";

        List<Object> params = new ArrayList<>();
        params.add(name);params.add(item_id);params.add(item_name);

        // 发送交易
        TransactionResponse transactionResponse = transactionProcessor.sendTransactionAndGetResponseByContractLoader("TableTest", "0xc9e1920e02e7c182ff43a594a38c1cfadeddd2f2", "insert", params);
        // 返回
        List<Object> returnValue = transactionResponse.getReturnObject();
        System.out.println(returnValue);

    }

    @Test
    public void testSelect() throws Exception{
        // 初始化BcosSDK对象
        BcosSDK sdk = BcosSDK.build(configFile);
        // 获取Client对象，此处传入的群组ID为1
        Client client = sdk.getClient(Integer.valueOf(1));
        // 构造AssembleTransactionProcessor对象，需要传入client对象，CryptoKeyPair对象和abi、binary文件存放的路径。abi和binary文件需要在上一步复制到定义的文件夹中。
        CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();

        // 只交易不部署
        AssembleTransactionProcessor transactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, keyPair, "src/main/resources/abi/", "");

        //
        List<Object> params = new ArrayList<>();
        params.add("abcd"); // 这里去看webase平台，之前insert一条对应数据

        // 查询TableTest合约的『name』函数，合约地址为webase自行查询，参数为空
        CallResponse callResponse = transactionProcessor.sendCallByContractLoader("TableTest", "0xc9e1920e02e7c182ff43a594a38c1cfadeddd2f2", "select", params);

        // 返回
        List<Object> returnValue = callResponse.getReturnObject();
        System.out.println(returnValue);
    }
}
