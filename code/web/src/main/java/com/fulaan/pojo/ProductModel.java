package com.fulaan.pojo;

/**
 * Created by admin on 2016/10/26.
 */
public class ProductModel {
  private String imageUrl;
  private String productPrice;
  private String productDescription;
  private String url;

  public ProductModel(){
  }

  public ProductModel(String imageUrl, String productDescription, String productPrice){
    this.imageUrl=imageUrl;
    this.productDescription=productDescription;
    this.productPrice=productPrice;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(String productPrice) {
    this.productPrice = productPrice;
  }

  public String getProductDescription() {
    return productDescription;
  }

  public void setProductDescription(String productDescription) {
    this.productDescription = productDescription;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
