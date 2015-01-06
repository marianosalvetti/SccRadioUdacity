package ar.com.sccradiomobile.storage.model.picasa;

/**
 * PicasaPhoto model.
 * 
 * @author Mariano Salvetti
 */
public class PicasaPhoto {

	private String mTitle;
	private String mThumbnailUrl;
	private String mDescription;

	public PicasaPhoto(String title, String thumbnailUrl, String description) {
		this.mTitle = title;
		this.mThumbnailUrl = thumbnailUrl;
		this.mDescription = description;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getThumbnailUrl() {
		return mThumbnailUrl;
	}

	public void setThumnailUrl(String mThumnailUrl) {
		this.mThumbnailUrl = mThumnailUrl;
	}

	public String getmDescription() {
		return mDescription;
	}

	public void setmDescription(String mDescription) {
		this.mDescription = mDescription;
	}
}
