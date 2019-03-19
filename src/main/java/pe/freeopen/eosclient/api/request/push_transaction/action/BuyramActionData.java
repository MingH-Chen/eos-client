package pe.freeopen.eosclient.api.request.push_transaction.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pe.freeopen.eosclient.client.pack.Pack;
import pe.freeopen.eosclient.client.pack.PackType;

/**
 * 购买ramaction用到的data
 * 
 * @author chenmh
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyramActionData extends BaseActionData {

	@Pack(PackType.name)
	@JsonProperty("payer")
	private String payer;

	@Pack(PackType.name)
	@JsonProperty("receiver")
	private String receiver;

	@Pack(PackType.asset)
	@JsonProperty("quant")
	private String quant;

	public String getPayer() {
		return payer;
	}

	public void setPayer(String payer) {
		this.payer = payer;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getQuant() {
		return quant;
	}

	public void setQuant(String quant) {
		this.quant = quant;
	}
}
