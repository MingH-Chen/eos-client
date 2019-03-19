package pe.freeopen.eosclient.api.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


/**
 * 代币统计信息
 * @Author: ChenMingHao
 * @Date: 2018-12-04 16:01
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CurrencyStatsResult {

	@JsonProperty("supply")
	private String supply;

	@JsonProperty("max_supply")
	private String maxSupply;

	@JsonProperty("issuer")
	private String issuer;

}
