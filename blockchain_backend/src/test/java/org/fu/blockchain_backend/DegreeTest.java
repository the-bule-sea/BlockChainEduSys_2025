package org.fu.blockchain_backend;

import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
// 引入生成的合约 Wrapper 类
import org.fu.blockchain_backend.contracts.DegreeStorage;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
// 可能需要引入其他类型，取决于您的 getDegrees 方法的返回值类型
// 例如：import java.math.BigInteger;
// 例如：import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray;

public class DegreeTest {

    private static final Logger logger = LoggerFactory.getLogger(DegreeTest.class);

    // 获取配置文件地址 - 确保这个路径是正确的，并且配置文件是 v3.x 兼容的
    public final String configFile = "src/main/resources/config-example.toml";
    // 合约地址 - 从 WeBASE 或部署时获取
    public final String contractAddress = "0x6849f21d1e455e9f0712b1e99fa4fcd23758e8f1";
    // 群组ID - 确认您要连接的群组ID，通常从 1 开始
    //public final int groupId = 0; // FISCO BCOS 群组 ID 通常从 1 开始，请确认 WeBASE 中的群组 ID
    // PEM 文件路径 - 确保这个路径是正确的，并且 PEM 文件存在
    public final String pemFilePath = "src/main/resources/key/test_key_0x8a38a4f53dbe8944f50f192daef2ccfa73851d98.pem";

    // 从pemAccountFilePath指定路径加载pem账户文件，并将其设置为交易发送账户
    //public void loadPemAccount(Client client, String pemAccountFilePath)
    //{
    //    // 通过client获取CryptoSuite对象
    //    CryptoSuite cryptoSuite = client.getCryptoSuite();
    //    // 加载pem账户文件
    //    cryptoSuite.loadAccount("pem", pemAccountFilePath, null);
    //}

    @Test
    public void testGetDegree() throws Exception {
        // 初始化BcosSDK对象
        BcosSDK sdk = BcosSDK.build(configFile);
        // 获取Client对象，传入正确的群组ID
        Client client = sdk.getClient();

        // 创建或加载用于交易发送的账户密钥对
        // 注意：每次调用 createKeyPair() 都会生成一个新的随机账户。
        //CryptoKeyPair keyPair = client.getCryptoSuite().createKeyPair();
        // 对于调用（call），这通常没问题。
        // 但如果要发送交易（修改链上状态），您需要加载一个固定的、已知的账户。
        // 例如，从私钥文件加载:
        // --- 正确加载 PEM 账户文件 ---
        // 1. 获取 CryptoSuite
        CryptoSuite cryptoSuite = client.getCryptoSuite();

        // 2. 使用 loadAccount 方法加载 PEM 文件
        //    第一个参数 "pem" 指定文件类型
        //    第二个参数是 PEM 文件路径
        //    第三个参数是 PEM 文件的密码，如果没有密码则为 null
        cryptoSuite.loadAccount("pem", pemFilePath, null);
        // 3. loadAccount 会将加载的账户设置为 cryptoSuite 的默认账户。
        //    我们可以通过 getCryptoKeyPair() 获取这个默认账户的 CryptoKeyPair 对象
        CryptoKeyPair keyPair = cryptoSuite.getCryptoKeyPair();
        // --- 账户加载完成 ---

        // 加载合约 Wrapper 实例
        DegreeStorage degreeStorage = DegreeStorage.load(contractAddress, client, keyPair);

        // 准备调用参数
        // 参数类型需要与 Solidity 合约中 getDegrees 函数的输入参数类型完全匹配
        // 假设 getDegrees 函数接受一个 string 或 bytes32 类型的 ID
        String degreeIdToQuery = "123456789012345678"; // 假设 Solidity 函数接受 string

        try {
            // *** 关键: 调用生成类的方法 ***
            // 假设 getDegrees 返回类型是 List<String>
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
                // 但检查 null 是个好习惯
                logger.warn("Call returned null.");
            }

        } catch (Exception e) {
            logger.error("Error calling getDegrees function: ", e);
            // 可以进一步检查异常类型，例如 ContractException 来获取更详细的错误信息
            throw e; // 重新抛出，让测试失败
        } finally {
            // 可以在这里添加资源清理代码，虽然对于简单测试 BcosSDK 可能不需要显式关闭
            // sdk.stop(); // 如果需要的话
        }
    }
}