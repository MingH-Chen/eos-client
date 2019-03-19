package pe.freeopen.eosclient.client.pack;

import java.security.InvalidParameterException;
/**
 * 
 * @author wangyan
 *
 */
public class AssetQuantity {
	/**
	 * 解析 "1.00 EOS" 到 "1.00 2,EOS@eosio.token"
	 * @param quantity 数额带符号
	 * @param contractAccount 合约账号
	 * @return
	 */
	public static String parse(String quantity, String contractAccount) {
		String[] v = quantity.split(" ");
		if(v.length != 2) {
			throw new InvalidParameterException("quantity invalid");
		}

		if(contractAccount==null){
			contractAccount = "eosio.token";
		}
		String amount = v[0];

		String ams [] = amount.split("[.]");
		int precision = 0;// precision 精度
		if(ams.length>1) {precision = ams[1].length();}

		String symbol = v[1];
		return String.format("%s %s,%s@%s", amount, precision, symbol, contractAccount);
	}
}
