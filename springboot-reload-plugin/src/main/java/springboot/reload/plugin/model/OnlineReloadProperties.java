package springboot.reload.plugin.model;

//@Configuration
//@ConfigurationProperties(prefix="online.reload.")
public class OnlineReloadProperties {
	
	private String temppath = "D:";

	public String getTemppath() {
		return temppath;
	}
	public void setTemppath(String temppath) {
		this.temppath = temppath;
	}
 
}