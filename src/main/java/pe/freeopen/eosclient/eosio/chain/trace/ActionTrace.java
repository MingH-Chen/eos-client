package pe.freeopen.eosclient.eosio.chain.trace;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionTrace extends BaseActionTrace {
	@JsonProperty("inline_traces")
	private List<ActionTrace> inlineTraces;

	@JsonProperty("console")
	private String console;

	public List<ActionTrace> getInlineTraces() {
		return inlineTraces;
	}

	public void setInlineTraces(List<ActionTrace> inlineTraces) {
		this.inlineTraces = inlineTraces;
	}

	public void setConsole(String console){
		this.console = console;
	}

	public String getConsole(){
		return console;
	}

}
