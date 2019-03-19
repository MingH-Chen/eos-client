package pe.freeopen.eosclient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pe.freeopen.eosclient.api.request.*;
import pe.freeopen.eosclient.api.result.*;
import pe.freeopen.eosclient.api.service.RpcService;
import pe.freeopen.eosclient.client.exception.ApiException;
import pe.freeopen.eosclient.client.exception.InvalidParameterException;
import pe.freeopen.eosclient.client.http.Generator;
import pe.freeopen.eosclient.client.pack.PackUtils;
import pe.freeopen.eosclient.client.transaction.SignedTransactionToPush;
import pe.freeopen.eosclient.client.transaction.TransactionBuilder;
import pe.freeopen.eosclient.crypto.util.Sha;
import pe.freeopen.eosclient.eosio.chain.action.Action;
import pe.freeopen.eosclient.eosio.chain.transaction.Transaction;
import pe.freeopen.eosclient.utils.ByteBuffer;
import pe.freeopen.eosclient.utils.Hex;

/**
 * EOS client
 * 
 * @author wuwei
 *
 */
public class EosClient {

	private RpcService rpcService;

	private TransactionBuilder txBuilder;

	public EosClient(String baseUrl) {
		this(baseUrl, null);
	}

	public EosClient(String baseUrl, String host) {
		rpcService = Generator.createService(RpcService.class, baseUrl, host);
		txBuilder = TransactionBuilder.newInstance(this);
	}

	/**
	 * 获取交易构造器
	 *
	 * @return
	 */
	public TransactionBuilder txBuilder() {
		return txBuilder;
	}

	/**
	 * 根据交易体计算交易id
	 *
	 * @param transaction
	 * @return
	 */
	public String calcTransactionId(Transaction transaction) {
		ByteBuffer bf = new ByteBuffer();
		PackUtils.packObj(transaction, bf);
		return Hex.bytesToHexString(Sha.SHA256(bf.getBuffer()));
	}

	/**
	 * 创建账户
	 *
	 * @param pk         私钥
	 * @param creator    创建者
	 * @param newAccount 新账户
	 * @param owner      公钥
	 * @param active     公钥
	 * @param buyRam     ram
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws Exception
	 */
	public PushTransactionResults createAccount(String pk, String creator, String newAccount, String owner,
												String active, Long buyRam) throws ApiException, IOException {
		return createAccount(pk, creator, newAccount, owner, active, buyRam, null, null, null);
	}

	/**
	 * 创建账户
	 *
	 * @param pk               私钥
	 * @param creator          创建者
	 * @param newAccount       新账户
	 * @param owner            公钥
	 * @param active           公钥
	 * @param buyRam           购买空间数量
	 * @param stakeNetQuantity 网络抵押
	 * @param stakeCpuQuantity cpu抵押
	 * @param transfer         抵押资产是否转送给对方，0自己所有，1对方所有
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws Exception
	 */
	public PushTransactionResults createAccount(String pk, String creator, String newAccount, String owner,
												String active, Long buyRam, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)
			throws ApiException, IOException {
		return pushTransaction(txBuilder.buildNewAccountRawTx(pk, creator, newAccount, owner, active, buyRam,
				stakeNetQuantity, stakeCpuQuantity, transfer));
	}

	/**
	 * 获得账户信息
	 *
	 * @param accountName 账户名称
	 * @return
	 * @throws IOException
	 */
	public GetAccountResults getAccount(String accountName) throws IOException {
		GetAccountRequest req = new GetAccountRequest();
		req.setAccountName(accountName);
		try {
			return Generator.executeSync(rpcService.getAccount(req));
		} catch (ApiException e) {
			return null;
		}
	}

	/**
	 * 获取账户信息
	 *
	 * @param publicKey
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public List<String> getAccounts(String publicKey) throws ApiException, IOException {
		GetAccountsRequest request = new GetAccountsRequest();
		request.setPublicKey(publicKey);
		return Generator.executeSync(rpcService.getAccounts(request)).getAccountNames();
	}

	/**
	 * 获取合约表数据信息
	 *
	 * @Author: ChenMingHao
	 * @Date: 2018-11-29 11:11
	 * @Param: contractAccount 合约账号
	 * @Param: tableName 表名
	 * @Return java.util.List<java.util.Map>
	 */
	public List<Map> getTableRows(String contractAccount, String tableName) throws ApiException, IOException {
		GetTableRowsRequest request = new GetTableRowsRequest();
		request.setCode(contractAccount);
		request.setScope(contractAccount);
		request.setTable(tableName);
		return Generator.executeSync(rpcService.getTableRows(request)).getRows();
	}

	/**
	 * 获得区块信息
	 *
	 * @param blockNumOrId 区块ID或者高度
	 * @return
	 * @throws IOException
	 */
	public GetBlockResult getBlock(String blockNumOrId) throws IOException {
		GetBlockRequest req = new GetBlockRequest();
		req.setBlockNumOrId(blockNumOrId);
		try {
			return Generator.executeSync(rpcService.getBlock(req));
		} catch (ApiException e) {
			return null;
		}
	}

	/**
	 * 获得链信息
	 *
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 */
	public GetInfoResults getChainInfo() throws ApiException, IOException {
		return Generator.executeSync(rpcService.getChainInfo());
	}

	/**
	 * 获取账户余额
	 *
	 * @param account 指定账户
	 * @param code    合约地址
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 */
	public Map<String, BigDecimal> getCurrencyBalance(String account, String code) throws ApiException, IOException {
		if (account == null || code == null) {
			throw new InvalidParameterException("account or code cannot be null");
		}
		GetCurrencyBalanceRequest req = new GetCurrencyBalanceRequest();
		req.setAccount(account);
		req.setCode(code);
		List<String> list = Generator.executeSync(rpcService.getCurrencyBalance(req));
		Map<String, BigDecimal> ret = new HashMap<String, BigDecimal>();
		if (list != null) {
			list.forEach(it -> {
				String[] s = it.split(" ");
				String coinTypeCode = s[1];
				BigDecimal value = new BigDecimal(s[0]);
				ret.put(coinTypeCode, value);
			});
		}
		return ret;
	}


	/**
	 * 获取指定账户account在合约账户code下的symbol代币余额
	 *
	 * @param account 账户
	 * @param code    合约地址
	 * @param symbol  代币
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public BigDecimal getCurrencyBalance(String account, String code, String symbol) throws ApiException, IOException {
		if (account == null || code == null) {
			throw new InvalidParameterException("account or code cannot be null");
		}
		if (symbol == null) {
			throw new InvalidParameterException("symbol cannot be null");
		}
		GetCurrencyBalanceRequest req = new GetCurrencyBalanceRequest();
		req.setAccount(account);
		req.setCode(code);
		req.setSymbol(symbol);
		List<String> list = Generator.executeSync(rpcService.getCurrencyBalance(req));
		if (list == null || list.isEmpty()) {
			return null;
		}
		return new BigDecimal(list.get(0).split(" ")[0]);
	}

	/**
	 * 获取代币信息
	 *
	 * @Author: ChenMingHao
	 * @Date: 2018-12-04 16:07
	 * @Param: code
	 * @Param: symbol
	 * @Return pe.freeopen.eosclient.api.result.CurrencyStatsResult
	 */
	public CurrencyStatsResult getCurrencyStats(String code, String symbol) throws ApiException, IOException {
		if (symbol == null || code == null) {
			throw new InvalidParameterException("symbol or code cannot be null");
		}
		GetCurrencyStatsRequest req = new GetCurrencyStatsRequest();
		req.setCode(code);
		req.setSymbol(symbol);
		Map<String, CurrencyStatsResult> ret = Generator.executeSync(rpcService.getCurrencyStats(req));
		if (ret != null) {
			return ret.get(symbol);
		}
		return null;
	}

	/**
	 * 获取交易信息
	 *
	 * @param transactionId
	 * @return
	 * @throws IOException
	 */
	public GetTransactionResult getTransaction(String transactionId) throws IOException {
		if (transactionId == null) {
			throw new InvalidParameterException("transactionId cannot be null");
		}
		GetTransactionRequest req = new GetTransactionRequest();
		req.setId(transactionId);
		try {
			GetTransactionResult t = Generator.executeSync(rpcService.getTransaction(req));
			if (t == null || t.getTrx() == null || !transactionId.equals(t.getId())) {
				return null;
			}
			return t;
		} catch (ApiException e) {
			return null;
		}
	}

	/**
	 * 发送交易请求
	 *
	 * @param pushTransaction
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public PushTransactionResults pushTransaction(SignedTransactionToPush pushTransaction)
			throws ApiException, IOException {
		return pushTransaction(pushTransaction.getCompression(), pushTransaction.getTransaction(),
				pushTransaction.getSignatures());
	}

	/**
	 * 发送交易请求
	 *
	 * @param compression     压缩
	 * @param pushTransaction 交易
	 * @param signatures      签名
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws Exception
	 */
	public PushTransactionResults pushTransaction(String compression, Transaction pushTransaction, String[] signatures)
			throws ApiException, IOException {
		return Generator.executeSync(
				rpcService.pushTransaction(new PushTransactionRequest(compression, pushTransaction, signatures)));
	}

	/**
	 * 转账
	 *
	 * @param pk              私钥
	 * @param contractAccount 合约账户
	 * @param from            从
	 * @param to              到
	 * @param quantity        币种金额
	 * @param memo            留言
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws Exception
	 */
	public PushTransactionResults transfer(String pk, String contractAccount, String from, String to, String quantity,
										   String memo) throws ApiException, IOException {
		return pushTransaction(txBuilder.buildTransferRawTx(pk, contractAccount, from, to, quantity, memo));
	}

	/**
	 * 投票
	 *
	 * @param pk
	 * @param voter
	 * @param proxy
	 * @param producers
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public PushTransactionResults voteProducer(String pk, String voter, String proxy, List<String> producers)
			throws ApiException, IOException {
		return pushTransaction(txBuilder.buildVoteProducerRawTx(pk, voter, proxy, producers));
	}

	/**
	 * 购买内存
	 * @Author: ChenMingHao
	 * @Date: 2018-12-16 22:38
	 * @Param: pk
	 * @Param: creator
	 * @Param: receiver
	 * @Param: quant
	 * @Return pe.freeopen.eosclient.api.result.PushTransactionResults
	 */
	public PushTransactionResults buyram(String pk, String creator, String receiver, String quant)
			throws ApiException, IOException {
		return pushTransaction(txBuilder.buildBuyramRawTx(pk, creator, receiver, quant));
	}

	/**
	 * 抵用CPU和带宽
	 * @Author: ChenMingHao
	 * @Date: 2018-12-17 18:03
	 * @Param: pk
	 * @Param: from
	 * @Param: receiver
	 * @Param: stakeNetQuantity
	 * @Param: stakeCpuQuantity
	 * @Param: transfer 0则是
	 * @Return pe.freeopen.eosclient.api.result.PushTransactionResults
	 */
	public PushTransactionResults delegatebw(String pk, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)
			throws ApiException, IOException {
		return pushTransaction(txBuilder.buildDelegatebwRawTx(pk, from, receiver, stakeNetQuantity,stakeCpuQuantity,transfer));
	}

	/**
	 * 更新用户权限
	 * @Author: ChenMingHao
	 * @Date: 2019-03-19 14:44
	 * @Param: pk
	 * @Param: actor 用户
	 * @Param: kind owner/active
	 * @Param: newPublicKey 更新后的key
	 * @Return pe.freeopen.eosclient.api.result.PushTransactionResults
	 */
	public PushTransactionResults updateUserAuthKey(String pk, String actor, String kind, String newPublicKey)
			throws ApiException, IOException {
		return pushTransaction(txBuilder.buildUpdateUserAuthKeyRawTx(pk, actor, kind, newPublicKey));
	}

	/**
	 * 通用方法
	 *
	 * @Author: ChenMingHao
	 * @Date: 2018-12-04 18:11
	 * @Param: pk
	 * @Param: actions
	 * @Param: eosCodeAuth
	 * @Return pe.freeopen.eosclient.api.result.PushTransactionResults
	 */
	public PushTransactionResults commonAction(String pk, List<Action> actions, EosCodeAuth eosCodeAuth) throws ApiException, IOException {
		return pushTransaction(txBuilder.buildTransferRawTx(pk, actions, eosCodeAuth));
	}

}
