package com.naiz.eus.model;

import android.graphics.Bitmap;

public class Blog {

private String tit;
private String egilea;
private Bitmap egileImage;
private String blogLink;
private String postTit;
private String postText;
private String postLink;
private Bitmap postImage;

/**
 * @return the tit
 */
public String getTit() {
	return tit;
}
/**
 * @param tit the tit to set
 */
public void setTit(String tit) {
	this.tit = tit;
}
/**
 * @return the link
 */
public String getBlogLink() {
	return blogLink;
}
/**
 * @param link the link to set
 */
public void setBlogLink(String link) {
	blogLink = link;
}
public String getPostLink() {
	return postLink;
}
public void setPostLink(String postLink) {
	this.postLink = postLink;
}
public String getEgilea() {
	return egilea;
}
public void setEgilea(String egilea) {
	this.egilea = egilea;
}
public Bitmap getEgileImage() {
	return egileImage;
}
public void setEgileImage(Bitmap egileImage) {
	this.egileImage = egileImage;
}
public String getPostTit() {
	return postTit;
}
public void setPostTit(String postTit) {
	this.postTit = postTit;
}
public String getPostText() {
	return postText;
}
public void setPostText(String postText) {
	this.postText = postText;
}
public Bitmap getPostImage() {
	return postImage;
}
public void setPostImage(Bitmap postImage) {
	this.postImage = postImage;
}
}
