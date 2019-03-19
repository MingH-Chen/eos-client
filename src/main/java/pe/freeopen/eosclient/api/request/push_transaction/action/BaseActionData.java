package pe.freeopen.eosclient.api.request.push_transaction.action;

import pe.freeopen.eosclient.client.pack.PackUtils;
import pe.freeopen.eosclient.utils.ByteBuffer;
import pe.freeopen.eosclient.utils.Hex;

public class BaseActionData {

	public String toString() {
		ByteBuffer bb = new ByteBuffer();
		PackUtils.packObj(this, bb);
		return Hex.bytesToHexString(bb.getBuffer());
	}
}
