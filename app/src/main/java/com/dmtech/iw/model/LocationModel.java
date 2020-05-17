package com.dmtech.iw.model;

import com.dmtech.iw.entity.Basic;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LocationModel {

    public static LocationModel fromBasic(Basic basic) {
        if (basic == null) {
            return null;
        }

        LocationModel model = new LocationModel();
        // 用basic内容填充model
        model.cid = basic.getCid();
        model.location = basic.getLocation();
        model.parent_city = basic.getParent_city();
        model.admin_area = basic.getAdmin_area();
        model.cnty = basic.getCnty();
        model.lat = basic.getLat();
        model.lon = basic.getLon();
        model.tz = basic.getTz();
        return model;
    }

    @Id
    private Long id;    // 数据表中记录的ID

    private String cid;
    private String location;
    private String parent_city;
    private String admin_area;
    private String cnty;
    private String lat;
    private String lon;
    private String tz;

    @Generated(hash = 1081533696)
    public LocationModel(Long id, String cid, String location, String parent_city,
            String admin_area, String cnty, String lat, String lon, String tz) {
        this.id = id;
        this.cid = cid;
        this.location = location;
        this.parent_city = parent_city;
        this.admin_area = admin_area;
        this.cnty = cnty;
        this.lat = lat;
        this.lon = lon;
        this.tz = tz;
    }

    @Generated(hash = 536868411)
    public LocationModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParent_city() {
        return parent_city;
    }

    public void setParent_city(String parent_city) {
        this.parent_city = parent_city;
    }

    public String getAdmin_area() {
        return admin_area;
    }

    public void setAdmin_area(String admin_area) {
        this.admin_area = admin_area;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

}
