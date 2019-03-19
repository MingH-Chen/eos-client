package pe.freeopen.eosclient.eosio.chain.authority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import pe.freeopen.eosclient.api.request.push_transaction.action.BaseActionData;
import pe.freeopen.eosclient.client.pack.Pack;
import pe.freeopen.eosclient.client.pack.PackType;
import pe.freeopen.eosclient.eosio.chain.action.PermissionLevel;
/**
 * 
 * @author wangyan
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionLevelWeight  {

	@Pack
	@JsonProperty("permission")
	private PermissionLevel permission;

	@Pack(PackType.uint16)
	@JsonProperty("weight")
	private Long weight;

	public PermissionLevel getPermission() {
		return permission;
	}

	public void setPermission(PermissionLevel permission) {
		this.permission = permission;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
	
}
