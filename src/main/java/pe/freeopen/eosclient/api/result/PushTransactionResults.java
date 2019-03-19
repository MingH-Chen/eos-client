package pe.freeopen.eosclient.api.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import pe.freeopen.eosclient.eosio.chain.trace.ActionTrace;
import pe.freeopen.eosclient.eosio.chain.trace.TransactionTrace;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PushTransactionResults {

	@JsonProperty("transaction_id")
	private String transactionId;

	@JsonProperty("processed")
	private TransactionTrace processed;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public TransactionTrace getProcessed() {
		return processed;
	}

	public void setProcessed(TransactionTrace processed) {
		this.processed = processed;
	}

	public ArrayList<String> getConsole(){
		List<ActionTrace> actionTraceList = this.getProcessed().getActionTraces();
		ArrayList<String> al =new ArrayList<>();
		getActionTraceList(al,actionTraceList);
		return al;
	}

	private void getActionTraces(ArrayList<String> al,ActionTrace actionTrace) {
		List<ActionTrace> actionTraceList = actionTrace.getInlineTraces();
		getActionTraceList(al,actionTraceList);
	}
	private void getActionTraceList(ArrayList<String> al,List<ActionTrace> actionTraceList) {
		ActionTrace subActionTrace;
		String consoleStr;
		for (int i = 0, j = actionTraceList.size(); i < j; i++) {
			subActionTrace = actionTraceList.get(i);
			consoleStr = subActionTrace.getConsole();
			if (consoleStr != "") {
				al.add(consoleStr);
			}
			getActionTraces(al, subActionTrace);
		}
	}
}
