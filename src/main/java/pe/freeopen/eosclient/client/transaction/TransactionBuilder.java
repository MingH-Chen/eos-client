package pe.freeopen.eosclient.client.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.freeopen.eosclient.EosClient;
import pe.freeopen.eosclient.api.request.EosCodeAuth;
import pe.freeopen.eosclient.api.request.push_transaction.action.*;
import pe.freeopen.eosclient.api.result.GetBlockResult;
import pe.freeopen.eosclient.api.result.GetInfoResults;
import pe.freeopen.eosclient.client.exception.ApiException;
import pe.freeopen.eosclient.client.pack.AssetQuantity;
import pe.freeopen.eosclient.crypto.EccTool;
import pe.freeopen.eosclient.enums.AuthRightKindEnum;
import pe.freeopen.eosclient.eosio.chain.action.Action;
import pe.freeopen.eosclient.eosio.chain.action.PermissionLevel;
import pe.freeopen.eosclient.eosio.chain.authority.Authority;
import pe.freeopen.eosclient.eosio.chain.authority.KeyWeight;
import pe.freeopen.eosclient.eosio.chain.authority.PermissionLevelWeight;
import pe.freeopen.eosclient.eosio.chain.authority.WaitWeight;
import pe.freeopen.eosclient.eosio.chain.transaction.SignedTransaction;
import pe.freeopen.eosclient.eosio.chain.transaction.Transaction;
import pe.freeopen.eosclient.utils.eoskey.EosKeyUtils;

/**
 * 交易构造器
 * 
 * @author wuwei
 *
 */
public class TransactionBuilder {

	private EosClient eosClient;

	private TransactionBuilder(EosClient eosClient) {
		this.eosClient = eosClient;
	}

	public static TransactionBuilder newInstance(EosClient eosClient) {
		return new TransactionBuilder(eosClient);
	}

	/**
	 * 组装原始转账交易
	 * 
	 * @param pk
	 * @param creator
	 * @param newAccount
	 * @param owner
	 * @param active
	 * @param buyRam
	 * @param stakeNetQuantity
	 * @param stakeCpuQuantity
	 * @param transfer
	 * @return
	 * @throws IOException
	 */
	public SignedTransactionToPush buildNewAccountRawTx(String pk, String creator, String newAccount, String owner,
			String active, Long buyRam, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)
			throws IOException {
		return buildRawTx(pk, () -> {
			// actions
			List<Action> actions = new ArrayList<>();
			// newaccount
			NewAccountActionData createMap = new NewAccountActionData();
			createMap.setCreator(creator);
			createMap.setName(newAccount);
			createMap.setOwner(owner);
			createMap.setActive(active);
			Action createAction = new Action(creator, "eosio", "newaccount", createMap);
			actions.add(createAction);
			// buyrambytes
			BuyrambytesActionData buyMap = new BuyrambytesActionData();
			buyMap.setPayer(creator);
			buyMap.setReceiver(newAccount);
			buyMap.setBytes(buyRam);
			Action buyAction = new Action(creator, "eosio", "buyrambytes", buyMap);
			actions.add(buyAction);
			// delegatebw
			if (stakeNetQuantity != null && stakeCpuQuantity != null && transfer != null) {
				DelegatebwActionData delMap = new DelegatebwActionData();
				delMap.setFrom(creator);
				delMap.setReceiver(newAccount);
				delMap.setStakeNetQuantity(AssetQuantity.parse(stakeNetQuantity, "eosio.token"));
				delMap.setStakeCpuQuantity(AssetQuantity.parse(stakeCpuQuantity, "eosio.token"));
				delMap.setTransfer(transfer);
				Action delAction = new Action(creator, "eosio", "delegatebw", delMap);
				actions.add(delAction);
			}
			return actions;
		});
	}

	/**
	 * 组织购买内存交易
	 * @Author: ChenMingHao
	 * @Date: 2018-12-16 22:36
	 * @Param: pk
     * @Param: creator
     * @Param: receiver
     * @Param: quant
	 * @Return pe.freeopen.eosclient.client.transaction.SignedTransactionToPush
	 */
	public SignedTransactionToPush buildBuyramRawTx(String pk, String creator, String receiver, String quant)throws IOException {
		return buildRawTx(pk, () -> {
			// actions
			List<Action> actions = new ArrayList<>();
			// buyram
			BuyramActionData buyMap = new BuyramActionData();
			buyMap.setPayer(creator);
			buyMap.setReceiver(receiver);
			buyMap.setQuant(AssetQuantity.parse(quant,null));
			Action buyAction = new Action(creator, "eosio", "buyram", buyMap);
			actions.add(buyAction);
			return actions;
		});
	}

	/**
	 * 组织购买cpu和带宽交易
	 * @Author: ChenMingHao
	 * @Date: 2018-12-17 18:01
	 * @Param: pk
	 * @Param: from
	 * @Param: receiver
	 * @Param: stakeNetQuantity
	 * @Param: stakeCpuQuantity
	 * @Param: transfer
	 * @Return pe.freeopen.eosclient.client.transaction.SignedTransactionToPush
	 */
	public SignedTransactionToPush buildDelegatebwRawTx(String pk, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)throws IOException {
		return buildRawTx(pk, () -> {
			// actions
			List<Action> actions = new ArrayList<>();

			DelegatebwActionData delegatebwActionData = new DelegatebwActionData();
			delegatebwActionData.setFrom(from);
			delegatebwActionData.setReceiver(receiver);
			delegatebwActionData.setStakeNetQuantity(AssetQuantity.parse(stakeNetQuantity, "eosio.token"));
			delegatebwActionData.setStakeCpuQuantity(AssetQuantity.parse(stakeCpuQuantity, "eosio.token"));
			delegatebwActionData.setTransfer(transfer);
			Action buyAction = new Action(from, "eosio", "delegatebw", delegatebwActionData);
			actions.add(buyAction);
			return actions;
		});
	}

	/**
	 * 组装原始交易
	 * 
	 * @param pk
	 * @param actionCollector
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public SignedTransactionToPush buildRawTx(String pk, ActionCollector actionCollector)
			throws ApiException, IOException {
		// get chain info
		GetInfoResults info = eosClient.getChainInfo();
		// get block info
		GetBlockResult block = eosClient.getBlock(info.getHeadBlockNum().toString());
		// tx
		Transaction tx = new Transaction();
		tx.setExpiration(new Date(info.getHeadBlockTime().getTime() + 60000));
		tx.setRefBlockNum(block.getBlockNum());
		tx.setRefBlockPrefix(block.getRefBlockPrefix());
		tx.setNetUsageWords(0l);
		tx.setMaxCpuUsageMs(0l);
		tx.setDelaySec(0l);
		// add actions
		List<Action> actions = actionCollector.collectActions();
		tx.setActions(actions);
		// 签名
		String sign = EccTool.signTransaction(pk, new TransactionToSign(info.getChainId(), tx));
		// 计算txId
		String txId = eosClient.calcTransactionId(tx);
		// reset action data
		for (Action action : actions) {
			action.setData(action.getData().toString());
		}
		return new SignedTransactionToPush(txId, "none", tx, new String[] { sign });
	}

	/**
	 * 组装转账原始交易
	 * 
	 * @param pk
	 * @param contractAccount
	 * @param from
	 * @param to
	 * @param quantity
	 * @param memo
	 * @return
	 * @throws IOException
	 * @throws ApiException
	 * @throws Exception
	 */
	public SignedTransactionToPush buildTransferRawTx(String pk, String contractAccount, String from, String to,
			String quantity, String memo) throws ApiException, IOException {
		return buildRawTx(pk, () -> {
			List<Action> actions = new ArrayList<>();
			// data
			TransferActionData dataMap = new TransferActionData();
			dataMap.setFrom(from);
			dataMap.setTo(to);
			dataMap.setQuantity(AssetQuantity.parse(quantity, contractAccount));
			dataMap.setMemo(memo);
			// action
			Action action = new Action(from, contractAccount, "transfer", dataMap);
			actions.add(action);
			return actions;
		});
	}

	/**
	 * 组装投票原始交易
	 * 
	 * @param pk
	 * @param voter
	 * @param proxy
	 * @param producers
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public SignedTransactionToPush buildVoteProducerRawTx(String pk, String voter, String proxy, List<String> producers)
			throws ApiException, IOException {
		return buildRawTx(pk, () -> {
			producers.sort((h1, h2) -> h1.compareTo(h2));
			List<Action> actions = new ArrayList<>();
			// data
			VoteProducerActionData data = new VoteProducerActionData();
			data.setVoter(voter);
			data.setProxy(proxy);
			data.setProducers(producers);
			// action
			Action action = new Action(voter, "eosio", "voteproducer", data);
			actions.add(action);
			return actions;
		});
	}


	/**
	 * 组装合约交易
	 *
	 * @param pk
	 * @param actions
	 * @param eosCodeAuth
	 * @return
	 * @throws ApiException
	 * @throws IOException
	 */
	public SignedTransactionToPush buildTransferRawTx(String pk, List<Action> actions, EosCodeAuth eosCodeAuth) throws ApiException, IOException {
		return buildRawTx(pk, () -> {
			if (eosCodeAuth != null && eosCodeAuth.isAuth()) { //添加合约调用合约权限，eosio.code
				String publicKey = EosKeyUtils.getPublicKey(pk);
				String contractAccount = eosCodeAuth.getContractAccount();
				String[] arr = contractAccount.split(",");//支持授权多个
				for(int i=arr.length-1;i>-1;i--) {
					actions.add(0, getEosioCodeAuth(arr[i], eosCodeAuth.getActor(), publicKey));
				}
			}
			return actions;
		});
	}

	/**
	 * 获取合约执行权限
	 * @Author: ChenMingHao
	 * @Date: 2018-12-04 14:39
	 * @Param: contractAccount 授权合约
 	 * @Param: actor 授权账号
 	 * @Param: publicKey 账号公钥
	 * @Return pe.freeopen.eosclient.eosio.chain.action.Action
	 * 示例：cleos set account permission user active '{"threshold": 1,"keys": [{"key": "EOS6MRkAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV","weight": 1}],
	 * "accounts": [{"permission":{"actor":"hello","permission":"eosio.code"},"weight":1}]}' owner -p user
	 * getEosioCodeAuth("hello","user","EOS6MRkAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV")
	 */
	private Action getEosioCodeAuth(String contractAccount, String actor, String publicKey) {
		// auth
		UpdateAuthActionData data = new UpdateAuthActionData();
		data.setAccount(actor);
		data.setPermission("active");
		data.setParent("owner");

		Authority authority = new Authority();
		authority.setThreshold(1l);

		List<PermissionLevelWeight> al = new ArrayList<PermissionLevelWeight>();

		PermissionLevelWeight permissionLevelWeight = new PermissionLevelWeight();
		permissionLevelWeight.setWeight(1l);

		PermissionLevel permissionLevel = new PermissionLevel();
		permissionLevel.setActor(contractAccount);
		permissionLevel.setPermission("eosio.code");

		permissionLevelWeight.setPermission(permissionLevel);
		al.add(permissionLevelWeight);
		authority.setAccounts(al);

		List<KeyWeight> keys = new ArrayList<>();
		KeyWeight keyWeight = new KeyWeight();
		keyWeight.setWeight(1l);
		keyWeight.setKey(publicKey);
		keys.add(keyWeight);
		authority.setKeys(keys);

		List<WaitWeight> waitWeights = new ArrayList<>();
		authority.setWaits(waitWeights);

		data.setAuth(authority);
		// action
		Action authAction = new Action(actor, "eosio", "updateauth", data);
		return authAction;
	}

	/**
	 * 组装更新用户权限key的交易
	 * @Author: ChenMingHao
	 * @Param: pk
     * @Param: actor 用户
	 * @Param: kind owner/active
     * @Param: newPublicKey 更新后的key
	 * @Return pe.freeopen.eosclient.client.transaction.SignedTransactionToPush
	 */
	public SignedTransactionToPush buildUpdateUserAuthKeyRawTx(String pk, String actor, String kind, String newPublicKey)
			throws ApiException, IOException {
		return buildRawTx(pk, () -> {
			List<Action> actions = new ArrayList<>();

			// auth
			UpdateAuthActionData data = new UpdateAuthActionData();
			data.setAccount(actor);
			String permission;
			if (kind.equals(AuthRightKindEnum.OWNER.getCode())) {
				data.setPermission("owner");
				data.setParent("");
				permission = "owner";
			} else {
				data.setPermission("active");
				data.setParent("owner");
				permission = "active";
			}

			Authority authority = new Authority();
			authority.setThreshold(1l);


			List<PermissionLevelWeight> al = new ArrayList<PermissionLevelWeight>();
			authority.setAccounts(al);

			List<KeyWeight> keys = new ArrayList<>();
			KeyWeight keyWeight = new KeyWeight();
			keyWeight.setWeight(1l);
			keyWeight.setKey(newPublicKey);
			keys.add(keyWeight);
			authority.setKeys(keys);

			List<WaitWeight> waitWeights = new ArrayList<>();
			authority.setWaits(waitWeights);

			data.setAuth(authority);
			// action
			Action action = new Action(actor, "eosio", "updateauth", data, permission);

			actions.add(action);
			return actions;
		});
	}


}
