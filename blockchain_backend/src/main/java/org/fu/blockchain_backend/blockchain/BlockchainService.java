package org.fu.blockchain_backend.blockchain;

import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fu.blockchain_backend.contracts.DegreeStorage;
import org.fu.blockchain_backend.model.Degree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlockchainService {
    private static final Logger logger = LoggerFactory.getLogger(BlockchainService.class);

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

    public void init() {
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

    public String uploadDegreeToBlockchain(Degree degree, String timestamp) {
        init();
        //long currentMillis = System.currentTimeMillis();
        //String timestamp = String.valueOf(currentMillis);
        try{
            TransactionReceipt receipt = degreeStorage.addDegree(
                    degree.getName(), degree.getIdCardNum(), degree.getUniversity(), degree.getMajor(), degree.getDegreeLevel(),  degree.getGraduationDate().toString(), timestamp
            );
            return receipt.getTransactionHash();
        } catch (Exception e){
            logger.error("Error sending addAndSaveDegree transaction: ", e);
            throw new RuntimeException(e.toString());
        }
    }
}
