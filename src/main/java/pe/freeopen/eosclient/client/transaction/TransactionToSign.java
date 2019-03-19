package pe.freeopen.eosclient.client.transaction;

import pe.freeopen.eosclient.client.pack.Pack;
import pe.freeopen.eosclient.client.pack.PackType;
import pe.freeopen.eosclient.eosio.chain.transaction.Transaction;

/**
 * 交易签名用
 * @author wangyan
 *
 */
public class TransactionToSign {
	
	@Pack(PackType.hexString)
	private String chainId;
	
	@Pack
	private Transaction transaction;
	
	public TransactionToSign() {

	}

	public TransactionToSign(String chainId, Transaction transaction) {
		this.chainId = chainId;
		this.transaction = transaction;
	}

	public String getChainId() {
		return chainId;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

}
