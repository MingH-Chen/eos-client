package pe.freeopen.eosclient.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 获取通证统计信息
 * @Author: ChenMingHao
 * @Date: 2018-12-04 15:49
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GetCurrencyStatsRequest {
	/**
	 * 合约地址
	 */
	String code;
	/**
	 * 代币符号
	 */
	String symbol;

	
	
}
