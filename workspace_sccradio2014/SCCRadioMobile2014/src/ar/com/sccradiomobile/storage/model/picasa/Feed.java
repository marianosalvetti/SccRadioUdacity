package ar.com.sccradiomobile.storage.model.picasa;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Feed model.
 * 
 * @author Mariano Salvetti
 */
public class Feed {
	@SerializedName("entry")
	private ArrayList<Entry> entries;

	public ArrayList<Entry> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<Entry> entries) {
		this.entries = entries;
	}
}
