package com.example.category.Bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "goodslist")
public class GoodsListInfo {
    @Id
    private String id;

    public String getUnname() {
        return unname;
    }

    public void setUnname(String unname) {
        this.unname = unname;
    }

    @Column

    private String brand;
    private String goodname;
    private String gpcname;
    private String unnum;
    private String gpcnum;
    private String unname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public String getGpcname() {
        return gpcname;
    }

    public void setGpcname(String gpcname) {
        this.gpcname = gpcname;
    }

    public String getUnnum() {
        return unnum;
    }

    public void setUnnum(String unnum) {
        this.unnum = unnum;
    }

    public String getGpcnum() {
        return gpcnum;
    }

    public void setGpcnum(String gpcnum) {
        this.gpcnum = gpcnum;
    }
}
