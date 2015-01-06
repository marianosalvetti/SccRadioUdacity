package ar.com.sccradiomobile.storage.model.picasa;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * EntryMediaGroup model.
 * 
 * @author Mariano Salvetti
 */
public class EntryMediaGroup {
	@SerializedName("media$content")
	private ArrayList<MediaGroupContent> mediaGroupContents;

	@SerializedName("media$credit")
	private ArrayList<MediaGroupCredit> mediaGroupCredits;

	@SerializedName("media$description")
	private MediaGroupDescription mediaGroupDescription;

	public ArrayList<MediaGroupContent> getMediaGroupContents() {
		return mediaGroupContents;
	}

	public void setMediaGroupContents(
			ArrayList<MediaGroupContent> mediaGroupContents) {
		this.mediaGroupContents = mediaGroupContents;
	}

	public ArrayList<MediaGroupCredit> getMediaGroupCredits() {
		return mediaGroupCredits;
	}

	public void setMediaGroupCredits(
			ArrayList<MediaGroupCredit> mediaGroupCredits) {
		this.mediaGroupCredits = mediaGroupCredits;
	}

	public MediaGroupDescription getMediaGroupDescription() {
		return mediaGroupDescription;
	}

	public void setMediaGroupDescription(MediaGroupDescription mediaGroupDescription) {
		this.mediaGroupDescription = mediaGroupDescription;
	}
}
