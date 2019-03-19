package pe.freeopen.eosclient.api.service;

import java.util.List;
import java.util.Map;

import pe.freeopen.eosclient.api.request.*;
import pe.freeopen.eosclient.api.result.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 
 * @author wangyan
 *
 */
public interface RpcService {

	@GET("/v1/chain/get_info")
	Call<GetInfoResults> getChainInfo();

	@POST("/v1/chain/get_block")
	Call<GetBlockResult> getBlock(@Body GetBlockRequest requestFields);

	@POST("/v1/chain/get_account")
	Call<GetAccountResults> getAccount(@Body GetAccountRequest requestFields);

	@POST("/v1/chain/push_transaction")
	Call<PushTransactionResults> pushTransaction(@Body PushTransactionRequest request);

	@POST("/v1/chain/abi_json_to_bin")
    Call<AbiJsonToBinResults> abiJsonToBin(@Body AbiJsonToBinRequest abiJsonToBinRequest);

	@POST("/v1/chain/get_currency_balance")
	Call<List<String>> getCurrencyBalance(@Body GetCurrencyBalanceRequest request);


	@POST("/v1/chain/get_currency_stats")
	Call<Map<String,CurrencyStatsResult>> getCurrencyStats(@Body GetCurrencyStatsRequest request);

	@POST("/v1/history/get_transaction")
	Call<GetTransactionResult> getTransaction(@Body GetTransactionRequest request);

	@POST("/v1/history/get_key_accounts")
	Call<GetAccountsResults> getAccounts(@Body GetAccountsRequest request);

	@POST("/v1/chain/get_table_rows")
	Call<GetTableRowsResults> getTableRows(@Body GetTableRowsRequest request);


}
