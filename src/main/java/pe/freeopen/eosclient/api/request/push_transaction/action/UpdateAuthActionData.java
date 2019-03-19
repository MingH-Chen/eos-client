package pe.freeopen.eosclient.api.request.push_transaction.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pe.freeopen.eosclient.client.pack.Pack;
import pe.freeopen.eosclient.client.pack.PackType;
import pe.freeopen.eosclient.eosio.chain.authority.Authority;


/**
 * 授权action使用的data
 * @Author: ChenMingHao
 * @Date: 2018-11-26 15:17
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAuthActionData extends BaseActionData {
	@Pack(PackType.name)
	@JsonProperty("account")
	private String account;

	@Pack(PackType.name)
	@JsonProperty("permission")
	private String permission;


	@Pack(PackType.name)
	@JsonProperty("parent")
	private String parent;

	@Pack
	@JsonProperty("auth")
	private Authority auth;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}


	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}


	public Authority getAuth() {
		return auth;
	}

	public void setAuth(Authority auth) {
		this.auth = auth;
	}
}
