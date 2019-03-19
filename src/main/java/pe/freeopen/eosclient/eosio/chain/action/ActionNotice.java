package pe.freeopen.eosclient.eosio.chain.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import pe.freeopen.eosclient.client.pack.Pack;
import pe.freeopen.eosclient.client.pack.PackType;
/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionNotice extends Action {
	@Pack(PackType.name)
	@JsonProperty("receiver")
	private String receiver;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
