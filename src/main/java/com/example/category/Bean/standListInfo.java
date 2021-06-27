package com.example.category.Bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "standlist")
public class standListInfo {
    @Id
    private int id;
    @Column
    private String unnum;
    private String gpcnum;
    private String gpcname;
    private String unname;
    private String gpcdesc;
    private int gpclevel;
    private String gpclevel3;
    private String gpclevel2;
    private String gpclevel1;

    public String getGpclevel1() {
        return gpclevel1;
    }

    public void setGpclevel1(String gpclevel1) {
        this.gpclevel1 = gpclevel1;
    }

    public String getGpcdesc() {
        return gpcdesc;
    }

    public void setGpcdesc(String gpcdesc) {
        this.gpcdesc = gpcdesc;
    }

    public int getGpclevel() {
        return gpclevel;
    }

    public void setGpclevel(int gpclevel) {
        this.gpclevel = gpclevel;
    }

    public String getGpclevel3() {
        return gpclevel3;
    }

    public void setGpclevel3(String gpclevel3) {
        this.gpclevel3 = gpclevel3;
    }

    public String getGpclevel2() {
        return gpclevel2;
    }

    public void setGpclevel2(String gpclevel2) {
        this.gpclevel2 = gpclevel2;
    }
    public String getGpcname() {
        return gpcname;
    }
    public void setGpcname(String gpcname) {
        this.gpcname = gpcname;
    }
    public String getUnname() {
        return unname;
    }
    public void setUnname(String unname) {
        this.unname = unname;
    }
    public String getGpcnum() {
        return gpcnum;
    }

    public void setGpcnum(String gpcnum) {
        this.gpcnum = gpcnum;
    }
    public String getUnnum() {
        return unnum;
    }
    public void setUnnum(String unnum) {
        this.unnum = unnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
