package pe.freeopen.eosclient.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTableRowsRequest {

	@JsonProperty("code")
	private String code = "eosio";

	@JsonProperty("scope")
	private String scope;

	@JsonProperty("table")
	private String table;

	@JsonProperty("json")
	private Boolean json=true;

	/**
	 * 不填默认也是10
	 */
	@JsonProperty("limit")
	private int limit = 10;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Boolean getJson() {
		return json;
	}

	public void setJson(Boolean json) {
		this.json = json;
	}

}
