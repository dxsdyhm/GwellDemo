package udpsender;

import java.util.Arrays;

/**
 * UDP请求结果对象
 * 
 */
public class UDPResult {
	private String ip;
	private byte[] resultData;

	public UDPResult() {
		super();
	}

	public UDPResult(String ip, byte[] resultData) {
		super();
		this.ip = ip;
		this.resultData = resultData;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public byte[] getResultData() {
		return resultData;
	}

	public void setResultData(byte[] resultData) {
		this.resultData = resultData;
	}

	@Override
	public String toString() {
		return "UDPResult [ip=" + ip + ", resultData="
				+ Arrays.toString(resultData) + "]";
	}

}
