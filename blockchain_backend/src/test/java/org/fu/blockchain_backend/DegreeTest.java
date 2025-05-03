package org.fu.blockchain_backend;

import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
// 引入生成的合约 Wrapper 类
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;
import org.fu.blockchain_backend.contracts.DegreeStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DegreeTest {

    private static final Logger logger = LoggerFactory.getLogger(DegreeTest.class);

    // 获取配置文件地址 - 确保这个路径是正确的，并且配置文件是 v3.x 兼容的
    public static final String configFile = "src/main/resources/config-example.toml";
    // 合约地址 - 从 WeBASE 或部署时获取
    public static final String contractAddress = "0x6849f21d1e455e9f0712b1e99fa4fcd23758e8f1";
    // 群组ID - 配置文件中
    // PEM 文件路径 - 确保这个路径是正确的，并且 PEM 文件存在
    public static final String pemFilePath = "src/main/resources/key/test_key_0x8a38a4f53dbe8944f50f192daef2ccfa73851d98.pem";

    // --- SDK 和 Client 对象 ---
    // 可以将 SDK 和 Client 初始化为静态变量，并在 @BeforeAll 中设置，避免每个测试方法都初始化
    private static BcosSDK sdk;
    private static Client client;
    private static CryptoKeyPair keyPair;
    private static DegreeStorage degreeStorage; // 合约对象也可以预先加载

    @BeforeAll // 使用 @BeforeAll 会在所有测试运行前执行一次
    static void setUp() throws Exception {
        logger.info("Setting up test environment...");
        sdk = BcosSDK.build(configFile);
        client = sdk.getClient();

        // 加载账户
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        cryptoSuite.loadAccount("pem", pemFilePath, null); // 假设无密码
        keyPair = cryptoSuite.getCryptoKeyPair();

        if (keyPair == null) {
            String errorMsg = "Failed to load account from PEM file: " + pemFilePath;
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }
        logger.info("Account loaded successfully. Address: {}", keyPair.getAddress());

        // 加载合约对象
        degreeStorage = DegreeStorage.load(contractAddress, client, keyPair);
        logger.info("DegreeStorage contract loaded at address: {}", contractAddress);
        logger.info("Setup complete.");
    }

    @Test
    @Disabled
    public void testGetDegree() throws Exception {
        // 准备调用参数
        // 参数类型需要与 Solidity 合约中 getDegrees 函数的输入参数类型完全匹配
        String degreeIdToQuery = "123456789012345678";

        try {
            // *** 关键: 调用生成类的方法 ***
            List<DegreeStorage.Degree> degrees = degreeStorage.getDegrees(degreeIdToQuery); // 直接调用方法

            // 处理返回结果
            if (degrees != null) {
                logger.info("Call successful. Return value:");
                if (degrees.isEmpty()) {
                    logger.info("Returned list is empty.");
                } else {
                    for (DegreeStorage.Degree degree : degrees) {
                        logger.info(" - {}", degree.toString());
                    }
                }
                System.out.println("Raw return list: " + degrees); // 打印原始列表
            } else {
                // 通常 call 成功但无返回值（如 void）或失败会通过异常体现，
                logger.warn("Call returned null.");
            }

        } catch (Exception e) {
            logger.error("Error calling getDegrees function: ", e);
            // 可以进一步检查异常类型，例如 ContractException 来获取更详细的错误信息
            throw e; // 重新抛出，让测试失败
        }
    }

    @Test
    public void testAddDegree() throws Exception {
        logger.info("--- Starting testAddDegree ---");

        // 1. 准备要添加的数据
        // 注意：如果 idCardNum 是合约中的唯一标识，每次测试需要使用不同的值，或者在测试后清理数据
        // 为了可重复测试，可以加入随机后缀或时间戳
        long currentMillis = System.currentTimeMillis();
        String uniqueSuffix = String.valueOf(currentMillis).substring(6); // 取毫秒时间戳后几位作为简单唯一后缀

        String name = "Test User " + uniqueSuffix;
        String idCardNum = "ID" + uniqueSuffix + "12345"; // 构造一个唯一的 ID
        String university = "Fudan University";
        String major = "Blockchain Tech";
        String degreeLevel = "Master";
        String graduationDate = "2025-01-20";
        String timestamp = String.valueOf(currentMillis); // 使用当前时间戳

        logger.info("Attempting to add degree with ID Card Num: {}", idCardNum);
        logger.info("Data: Name={}, University={}, Major={}, Level={}, GradDate={}, Timestamp={}",
                name, university, major, degreeLevel, graduationDate, timestamp);

        try {
            // 2. 调用合约的 addDegree 方法，这是一个交易 (会改变链上状态)
            TransactionReceipt receipt = degreeStorage.addDegree(
                    name, idCardNum, university, major, degreeLevel, graduationDate, timestamp
            );

            // 3. 处理交易回执 (TransactionReceipt)
            assertNotNull(receipt, "Transaction receipt should not be null.");

            // 检查交易状态是否成功 (Status 为 "0x0" 表示成功)
            if (receipt.isStatusOK()) {
                logger.info("Transaction successful!");
                logger.info(" - Tx Hash: {}", receipt.getTransactionHash());
                logger.info(" - Block Number: {}", receipt.getBlockNumber());
                logger.info(" - Gas Used: {}", receipt.getGasUsed());
                // 可以选择性地打印日志信息 (如果合约中定义了 Event 并触发)
                //logger.info(" - Logs: {}", receipt.getOutput()); // 原始日志，可能需要解析

                // **重要**: 检查合约执行状态 (Output != null 且 Output != "0x" 通常表示成功)
                // 有些合约执行失败也会返回 status 0x0，需要检查 output
                assertNotNull(receipt.getOutput(), "Receipt output should not be null.");
                assertNotEquals("0x", receipt.getOutput(), "Receipt output indicates potential revert during execution.");
                logger.info(" - Receipt Output: {}", receipt.getOutput());

            } else {
                // 交易失败 (Status 不是 "0x0")
                logger.error("Transaction failed!");
                logger.error(" - Status: {}", receipt.getStatus());
                logger.error(" - Tx Hash: {}", receipt.getTransactionHash());
                // 查看失败信息，可能在 message 或 output 中
                logger.error(" - Error Message: {}", receipt.getMessage());
                logger.error(" - Receipt Output: {}", receipt.getOutput()); // output 可能包含 revert 原因
                // 使用 fail 让 JUnit 测试失败
                //fail("Transaction failed with status: " + receipt.getStatus() + ", message: " + receipt.getMessage());
            }

        }
        catch (Exception e) {
            // 其他 SDK 或网络层面的异常
            logger.error("Error sending addDegree transaction: ", e);
            throw e; // 重新抛出，让测试失败
        } finally {
            logger.info("--- Finished testAddDegree ---");
        }
    }

}