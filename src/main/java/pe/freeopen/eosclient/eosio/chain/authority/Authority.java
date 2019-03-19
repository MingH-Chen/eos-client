package pe.freeopen.eosclient.eosio.chain.authority;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pe.freeopen.eosclient.api.request.push_transaction.action.BaseActionData;
import pe.freeopen.eosclient.client.pack.Pack;
import pe.freeopen.eosclient.client.pack.PackType;

/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Authority  {

	@Pack(PackType.uint32)
	@JsonProperty("threshold")
	private Long threshold;

	@Pack
	@JsonProperty("keys")
	private List<KeyWeight> keys;

	@Pack
	@JsonProperty("accounts")
	private List<PermissionLevelWeight> accounts;

	@Pack
	@JsonProperty("waits")
	private List<WaitWeight> waits;
	
	public List<PermissionLevelWeight> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<PermissionLevelWeight> accounts) {
		this.accounts = accounts;
	}

	public List<KeyWeight> getKeys() {
		return keys;
	}

	public void setKeys(List<KeyWeight> keys) {
		this.keys = keys;
	}

	public Long getThreshold() {
		return threshold;
	}

	public void setThreshold(Long threshold) {
		this.threshold = threshold;
	}

	public List<WaitWeight> getWaits() {
		return waits;
	}

	public void setWaits(List<WaitWeight> waits) {
		this.waits = waits;
	}
	
}
