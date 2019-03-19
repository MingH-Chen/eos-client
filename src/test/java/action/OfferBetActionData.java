package action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import pe.freeopen.eosclient.api.request.push_transaction.action.BaseActionData;
import pe.freeopen.eosclient.client.pack.Pack;
import pe.freeopen.eosclient.client.pack.PackType;

/**
 * 下注行为使用的data
 * 不允许用data注解
 * @Author: ChenMingHao
 * @Date: 2018-12-04 15:29
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferBetActionData extends BaseActionData {

	@Pack(PackType.name)
	@JsonProperty("player")
	private String player;

	@Pack(PackType.asset)
	@JsonProperty("bet")
	private String bet;

	@Pack(PackType.uint8)
	@JsonProperty("roll_type")
	private Long rollType;

	@Pack(PackType.uint64)
	@JsonProperty("roll_border")
	private Long rollBorder;

	@Pack(PackType.string)
	@JsonProperty("memo")
	private String memo;

	public String getPlayer(){
		return this.player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getBet() {
		return bet;
	}

	public void setBet(String bet) {

		this.bet = bet;
	}

	public Long getRollType() {
		return rollType;
	}

	public void setRollType(Long rollType) {
		this.rollType = rollType;
	}

	public Long getRollBorder() {
		return rollBorder;
	}

	public void setRollBorder(Long rollBorder) {
		this.rollBorder = rollBorder;
	}


	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
