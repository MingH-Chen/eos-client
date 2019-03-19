package pe.freeopen.eosclient.client.transaction;

import java.util.List;

import pe.freeopen.eosclient.eosio.chain.action.Action;

public interface ActionCollector {
	public List<Action> collectActions();
}
