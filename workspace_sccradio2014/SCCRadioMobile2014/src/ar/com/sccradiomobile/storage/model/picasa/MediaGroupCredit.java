package ar.com.sccradiomobile.storage.model.picasa;

import com.google.gson.annotations.SerializedName;

/**
 * MediaGroupCredit model.
 * 
 * @author Mariano Salvetti
 */
public class MediaGroupCredit {
	@SerializedName("$t")
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
