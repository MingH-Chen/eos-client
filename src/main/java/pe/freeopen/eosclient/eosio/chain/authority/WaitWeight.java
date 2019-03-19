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
public class WaitWeight  {

	@Pack(PackType.uint32)
	@JsonProperty("wait_sec")
	private Long waitSec;

	@Pack(PackType.uint16)
	@JsonProperty("weight")
	private Long weight;

	public Long getWaitSec() {
		return waitSec;
	}

	public void setWaitSec(Long waitSec) {
		this.waitSec = waitSec;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
	
}
