package pe.freeopen.eosclient.eosio.chain.authority;

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
public class KeyWeight {

	@Pack(PackType.pubkey)
	@JsonProperty("key")
	private String key;

	@Pack(PackType.uint16)
	@JsonProperty("weight")
	private Long weight;

	public KeyWeight() {

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
}
