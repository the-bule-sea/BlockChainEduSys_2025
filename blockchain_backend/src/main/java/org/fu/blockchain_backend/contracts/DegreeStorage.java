package org.fu.blockchain_backend.contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Bool;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicStruct;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple7;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.CallCallback;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class DegreeStorage extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061111d806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c806328c4287614610046578063983b478614610076578063b6cb1e7f146100a6575b600080fd5b610060600480360381019061005b9190610a1c565b6100d6565b60405161006d9190610b9b565b60405180910390f35b610090600480360381019061008b9190610bb6565b6102f1565b60405161009d9190610e3e565b60405180910390f35b6100c060048036038101906100bb9190610e8c565b610788565b6040516100cd9190610ef7565b60405180910390f35b6000806040518061010001604052808a81526020018981526020018881526020018781526020018681526020018581526020018481526020016001151581525090506000886040516101289190610f4e565b9081526020016040518091039020819080600181540180825580915050600190039060005260206000209060080201600090919091909150600082015181600001908051906020019061017c92919061081f565b50602082015181600101908051906020019061019992919061081f565b5060408201518160020190805190602001906101b692919061081f565b5060608201518160030190805190602001906101d392919061081f565b5060808201518160040190805190602001906101f092919061081f565b5060a082015181600501908051906020019061020d92919061081f565b5060c082015181600601908051906020019061022a92919061081f565b5060e08201518160070160006101000a81548160ff02191690831515021790555050507f133a4e13cffb5b77e72a6394cd92dbfc9d6123761440dca9f21df597e52f78e488600160008b6040516102819190610f4e565b90815260200160405180910390208054905061029d9190610f94565b856040516102ad93929190611012565b60405180910390a160016000896040516102c79190610f4e565b9081526020016040518091039020805490506102e39190610f94565b915050979650505050505050565b60606000826040516103039190610f4e565b9081526020016040518091039020805480602002602001604051908101604052809291908181526020016000905b8282101561077d57838290600052602060002090600802016040518061010001604052908160008201805461036590611086565b80601f016020809104026020016040519081016040528092919081815260200182805461039190611086565b80156103de5780601f106103b3576101008083540402835291602001916103de565b820191906000526020600020905b8154815290600101906020018083116103c157829003601f168201915b505050505081526020016001820180546103f790611086565b80601f016020809104026020016040519081016040528092919081815260200182805461042390611086565b80156104705780601f1061044557610100808354040283529160200191610470565b820191906000526020600020905b81548152906001019060200180831161045357829003601f168201915b5050505050815260200160028201805461048990611086565b80601f01602080910402602001604051908101604052809291908181526020018280546104b590611086565b80156105025780601f106104d757610100808354040283529160200191610502565b820191906000526020600020905b8154815290600101906020018083116104e557829003601f168201915b5050505050815260200160038201805461051b90611086565b80601f016020809104026020016040519081016040528092919081815260200182805461054790611086565b80156105945780601f1061056957610100808354040283529160200191610594565b820191906000526020600020905b81548152906001019060200180831161057757829003601f168201915b505050505081526020016004820180546105ad90611086565b80601f01602080910402602001604051908101604052809291908181526020018280546105d990611086565b80156106265780601f106105fb57610100808354040283529160200191610626565b820191906000526020600020905b81548152906001019060200180831161060957829003601f168201915b5050505050815260200160058201805461063f90611086565b80601f016020809104026020016040519081016040528092919081815260200182805461066b90611086565b80156106b85780601f1061068d576101008083540402835291602001916106b8565b820191906000526020600020905b81548152906001019060200180831161069b57829003601f168201915b505050505081526020016006820180546106d190611086565b80601f01602080910402602001604051908101604052809291908181526020018280546106fd90611086565b801561074a5780601f1061071f5761010080835404028352916020019161074a565b820191906000526020600020905b81548152906001019060200180831161072d57829003601f168201915b505050505081526020016007820160009054906101000a900460ff16151515158152505081526020019060010190610331565b505050509050919050565b600080836040516107999190610f4e565b90815260200160405180910390208054905082101561081457600080846040516107c39190610f4e565b908152602001604051809103902083815481106107e3576107e26110b8565b5b906000526020600020906008020160070160006101000a81548160ff02191690831515021790555060019050610819565b600090505b92915050565b82805461082b90611086565b90600052602060002090601f01602090048101928261084d5760008555610894565b82601f1061086657805160ff1916838001178555610894565b82800160010185558215610894579182015b82811115610893578251825591602001919060010190610878565b5b5090506108a191906108a5565b5090565b5b808211156108be5760008160009055506001016108a6565b5090565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610929826108e0565b810181811067ffffffffffffffff82111715610948576109476108f1565b5b80604052505050565b600061095b6108c2565b90506109678282610920565b919050565b600067ffffffffffffffff821115610987576109866108f1565b5b610990826108e0565b9050602081019050919050565b82818337600083830152505050565b60006109bf6109ba8461096c565b610951565b9050828152602081018484840111156109db576109da6108db565b5b6109e684828561099d565b509392505050565b600082601f830112610a0357610a026108d6565b5b8135610a138482602086016109ac565b91505092915050565b600080600080600080600060e0888a031215610a3b57610a3a6108cc565b5b600088013567ffffffffffffffff811115610a5957610a586108d1565b5b610a658a828b016109ee565b975050602088013567ffffffffffffffff811115610a8657610a856108d1565b5b610a928a828b016109ee565b965050604088013567ffffffffffffffff811115610ab357610ab26108d1565b5b610abf8a828b016109ee565b955050606088013567ffffffffffffffff811115610ae057610adf6108d1565b5b610aec8a828b016109ee565b945050608088013567ffffffffffffffff811115610b0d57610b0c6108d1565b5b610b198a828b016109ee565b93505060a088013567ffffffffffffffff811115610b3a57610b396108d1565b5b610b468a828b016109ee565b92505060c088013567ffffffffffffffff811115610b6757610b666108d1565b5b610b738a828b016109ee565b91505092959891949750929550565b6000819050919050565b610b9581610b82565b82525050565b6000602082019050610bb06000830184610b8c565b92915050565b600060208284031215610bcc57610bcb6108cc565b5b600082013567ffffffffffffffff811115610bea57610be96108d1565b5b610bf6848285016109ee565b91505092915050565b600081519050919050565b600082825260208201905092915050565b6000819050602082019050919050565b600081519050919050565b600082825260208201905092915050565b60005b83811015610c65578082015181840152602081019050610c4a565b83811115610c74576000848401525b50505050565b6000610c8582610c2b565b610c8f8185610c36565b9350610c9f818560208601610c47565b610ca8816108e0565b840191505092915050565b60008115159050919050565b610cc881610cb3565b82525050565b6000610100830160008301518482036000860152610cec8282610c7a565b91505060208301518482036020860152610d068282610c7a565b91505060408301518482036040860152610d208282610c7a565b91505060608301518482036060860152610d3a8282610c7a565b91505060808301518482036080860152610d548282610c7a565b91505060a083015184820360a0860152610d6e8282610c7a565b91505060c083015184820360c0860152610d888282610c7a565b91505060e0830151610d9d60e0860182610cbf565b508091505092915050565b6000610db48383610cce565b905092915050565b6000602082019050919050565b6000610dd482610bff565b610dde8185610c0a565b935083602082028501610df085610c1b565b8060005b85811015610e2c5784840389528151610e0d8582610da8565b9450610e1883610dbc565b925060208a01995050600181019050610df4565b50829750879550505050505092915050565b60006020820190508181036000830152610e588184610dc9565b905092915050565b610e6981610b82565b8114610e7457600080fd5b50565b600081359050610e8681610e60565b92915050565b60008060408385031215610ea357610ea26108cc565b5b600083013567ffffffffffffffff811115610ec157610ec06108d1565b5b610ecd858286016109ee565b9250506020610ede85828601610e77565b9150509250929050565b610ef181610cb3565b82525050565b6000602082019050610f0c6000830184610ee8565b92915050565b600081905092915050565b6000610f2882610c2b565b610f328185610f12565b9350610f42818560208601610c47565b80840191505092915050565b6000610f5a8284610f1d565b915081905092915050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b6000610f9f82610b82565b9150610faa83610b82565b925082821015610fbd57610fbc610f65565b5b828203905092915050565b600082825260208201905092915050565b6000610fe482","610c2b565b610fee8185610fc8565b9350610ffe818560208601610c47565b611007816108e0565b840191505092915050565b6000606082019050818103600083015261102c8186610fd9565b905061103b6020830185610b8c565b818103604083015261104d8184610fd9565b9050949350505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6000600282049050600182168061109e57607f821691505b602082108114156110b2576110b1611057565b5b50919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fdfea2646970667358221220be83adfe32f2ce201585bdb48fa3a65d0b5c628a5f03d6bb0e49f870de14ade264736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"string\",\"name\":\"idCardNum\",\"type\":\"string\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"string\",\"name\":\"txTime\",\"type\":\"string\"}],\"name\":\"DegreeAdded\",\"type\":\"event\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"idCardNum\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"university\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"major\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"degreeLevel\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"graduationDate\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"timestamp\",\"type\":\"string\"}],\"name\":\"addDegree\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"idCardNum\",\"type\":\"string\"}],\"name\":\"getDegrees\",\"outputs\":[{\"components\":[{\"internalType\":\"string\",\"name\":\"name\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"idCardNum\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"university\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"major\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"degreeLevel\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"graduationDate\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"timestamp\",\"type\":\"string\"},{\"internalType\":\"bool\",\"name\":\"valid\",\"type\":\"bool\"}],\"internalType\":\"struct DegreeStorage.Degree[]\",\"name\":\"\",\"type\":\"tuple[]\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"idCardNum\",\"type\":\"string\"},{\"internalType\":\"uint256\",\"name\":\"index\",\"type\":\"uint256\"}],\"name\":\"invalidateDegree\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_ADDDEGREE = "addDegree";

    public static final String FUNC_GETDEGREES = "getDegrees";

    public static final String FUNC_INVALIDATEDEGREE = "invalidateDegree";

    public static final Event DEGREEADDED_EVENT = new Event("DegreeAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected DegreeStorage(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<DegreeAddedEventResponse> getDegreeAddedEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(DEGREEADDED_EVENT, transactionReceipt);
        ArrayList<DegreeAddedEventResponse> responses = new ArrayList<DegreeAddedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            DegreeAddedEventResponse typedResponse = new DegreeAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.idCardNum = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.index = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.txTime = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public TransactionReceipt addDegree(String name, String idCardNum, String university,
            String major, String degreeLevel, String graduationDate, String timestamp) {
        final Function function = new Function(
                FUNC_ADDDEGREE, 
                Arrays.<Type>asList(new Utf8String(name),
                new Utf8String(idCardNum),
                new Utf8String(university),
                new Utf8String(major),
                new Utf8String(degreeLevel),
                new Utf8String(graduationDate),
                new Utf8String(timestamp)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String getSignedTransactionForAddDegree(String name, String idCardNum, String university,
            String major, String degreeLevel, String graduationDate, String timestamp) {
        final Function function = new Function(
                FUNC_ADDDEGREE, 
                Arrays.<Type>asList(new Utf8String(name),
                new Utf8String(idCardNum),
                new Utf8String(university),
                new Utf8String(major),
                new Utf8String(degreeLevel),
                new Utf8String(graduationDate),
                new Utf8String(timestamp)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String addDegree(String name, String idCardNum, String university, String major,
            String degreeLevel, String graduationDate, String timestamp,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_ADDDEGREE, 
                Arrays.<Type>asList(new Utf8String(name),
                new Utf8String(idCardNum),
                new Utf8String(university),
                new Utf8String(major),
                new Utf8String(degreeLevel),
                new Utf8String(graduationDate),
                new Utf8String(timestamp)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple7<String, String, String, String, String, String, String> getAddDegreeInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ADDDEGREE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple7<String, String, String, String, String, String, String>(

                (String) results.get(0).getValue(), 
                (String) results.get(1).getValue(), 
                (String) results.get(2).getValue(), 
                (String) results.get(3).getValue(), 
                (String) results.get(4).getValue(), 
                (String) results.get(5).getValue(), 
                (String) results.get(6).getValue()
                );
    }

    public Tuple1<BigInteger> getAddDegreeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ADDDEGREE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public List<Degree> getDegrees(String idCardNum) throws ContractException {
        final Function function = new Function(FUNC_GETDEGREES, 
                Arrays.<Type>asList(new Utf8String(idCardNum)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Degree>>() {}));
        return executeCallWithSingleValueReturn(function, List.class);
    }

    public void getDegrees(String idCardNum, CallCallback callback) throws ContractException {
        final Function function = new Function(FUNC_GETDEGREES, 
                Arrays.<Type>asList(new Utf8String(idCardNum)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Degree>>() {}));
        asyncExecuteCall(function, callback);
    }

    public TransactionReceipt invalidateDegree(String idCardNum, BigInteger index) {
        final Function function = new Function(
                FUNC_INVALIDATEDEGREE, 
                Arrays.<Type>asList(new Utf8String(idCardNum),
                new Uint256(index)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String getSignedTransactionForInvalidateDegree(String idCardNum, BigInteger index) {
        final Function function = new Function(
                FUNC_INVALIDATEDEGREE, 
                Arrays.<Type>asList(new Utf8String(idCardNum),
                new Uint256(index)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String invalidateDegree(String idCardNum, BigInteger index,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_INVALIDATEDEGREE, 
                Arrays.<Type>asList(new Utf8String(idCardNum),
                new Uint256(index)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<String, BigInteger> getInvalidateDegreeInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_INVALIDATEDEGREE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<String, BigInteger>(

                (String) results.get(0).getValue(), 
                (BigInteger) results.get(1).getValue()
                );
    }

    public Tuple1<Boolean> getInvalidateDegreeOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_INVALIDATEDEGREE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public static DegreeStorage load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new DegreeStorage(contractAddress, client, credential);
    }

    public static DegreeStorage deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(DegreeStorage.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }

    public static class Degree extends DynamicStruct {
        public String name;

        public String idCardNum;

        public String university;

        public String major;

        public String degreeLevel;

        public String graduationDate;

        public String timestamp;

        public Boolean valid;

        public Degree(Utf8String name, Utf8String idCardNum, Utf8String university,
                Utf8String major, Utf8String degreeLevel, Utf8String graduationDate,
                Utf8String timestamp, Bool valid) {
            super(name,idCardNum,university,major,degreeLevel,graduationDate,timestamp,valid);
            this.name = name.getValue();
            this.idCardNum = idCardNum.getValue();
            this.university = university.getValue();
            this.major = major.getValue();
            this.degreeLevel = degreeLevel.getValue();
            this.graduationDate = graduationDate.getValue();
            this.timestamp = timestamp.getValue();
            this.valid = valid.getValue();
        }

        public Degree(String name, String idCardNum, String university, String major,
                String degreeLevel, String graduationDate, String timestamp, Boolean valid) {
            super(new Utf8String(name),new Utf8String(idCardNum),new Utf8String(university),new Utf8String(major),new Utf8String(degreeLevel),new Utf8String(graduationDate),new Utf8String(timestamp),new Bool(valid));
            this.name = name;
            this.idCardNum = idCardNum;
            this.university = university;
            this.major = major;
            this.degreeLevel = degreeLevel;
            this.graduationDate = graduationDate;
            this.timestamp = timestamp;
            this.valid = valid;
        }

        @Override
        public String toString() {
            return "Degree{" +
                    "name='" + name + '\'' +
                    ", idCardNum='" + idCardNum + '\'' +
                    ", university='" + university + '\'' +
                    ", major='" + major + '\'' +
                    ", degreeLevel='" + degreeLevel + '\'' +
                    ", graduationDate='" + graduationDate + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", valid=" + valid +
                    '}';
        }

        public String getName() {
            return name;
        }

        public String getIdCardNum() {
            return idCardNum;
        }

        public String getUniversity() {
            return university;
        }

        public String getMajor() {
            return major;
        }

        public String getDegreeLevel() {
            return degreeLevel;
        }

        public String getGraduationDate() {
            return graduationDate;
        }
    }

    public static class DegreeAddedEventResponse {
        public TransactionReceipt.Logs log;

        public String idCardNum;

        public BigInteger index;

        public String txTime;
    }
}
