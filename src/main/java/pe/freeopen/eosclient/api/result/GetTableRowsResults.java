package pe.freeopen.eosclient.api.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetTableRowsResults {

	@JsonProperty("more")
	private Boolean more;

	@JsonProperty("rows")
	private List<Map> rows;

	public Boolean getMore() {
		return more;
	}

	public void setMore(Boolean more) {
		this.more = more;
	}

	public List<Map> getRows() {
		return rows;
	}

	public void setRows(List<Map> rows) {
		this.rows = rows;
	}
	
}
