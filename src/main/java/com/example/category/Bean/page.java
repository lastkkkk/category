package com.example.category.Bean;

public class page {
    private int pagenum;
    private int total;
    private int pagesize;
    private String username;
    private String goodname;
    private String unnum;
    private String activeName;

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getGpcnum() {
        return gpcnum;
    }

    public void setGpcnum(String gpcnum) {
        this.gpcnum = gpcnum;
    }

    private String gpcnum;

    public String getUnnum() {
        return unnum;
    }

    public void setUnnum(String unnum) {
        this.unnum = unnum;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
