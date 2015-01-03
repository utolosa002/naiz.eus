package com.naiz.eus.model;

import android.graphics.Bitmap;


public class Berria {
private String Title;
private String Subtitle;
private Bitmap Image;
private String Saila;
private String SailLinka;
private String Link;
private String ExtraInfo;
private String Berria;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the tit
	 */
	public String getTitle() {
		return Title;
	}

	/**
	 * @param tit the tit to set
	 */
	public void setTitle(String title) {
		Title = title;
	}

	/**
	 * @return the Subtitle
	 */
	public String getSubtitle() {
		return Subtitle;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setSubtitle(String subtitle) {
		Subtitle = subtitle;
	}

	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		return Image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.Image = image;
	}

	/**
	 * @return the price
	 */
	public String getSaila() {
		return Saila;
	}

	/**
	 * @param price the price to set
	 */
	public void setSaila(String Saila) {
		this.Saila= Saila;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return Link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		Link = link;
	}

	public String getSailLinka() {
		return SailLinka;
	}

	public void setSailLinka(String sailLinka) {
		SailLinka = sailLinka;
	}

	public String getExtraInfo() {
		return ExtraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.ExtraInfo = extraInfo;
	}

	public String getBerria() {
		return Berria;
	}

	public void setBerria(String berria) {
		Berria = berria;
	}

}
