package com.mingquan.yuejian.auth;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/10/18
 */

public class YueJianAppCountryBean implements Serializable {

    private String name;
    private List<ProvinceBean> province;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProvinceBean> getProvince() {
        return province;
    }

    public void setProvince(List<ProvinceBean> province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "YueJianAppCountryBean{" +
                "name='" + name + '\'' +
                ", province=" + province +
                '}';
    }

    public static class ProvinceBean {
        private String name;
        private List<String> city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getCity() {
            return city;
        }

        public void setCity(List<String> city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "ProvinceBean{" +
                    "name='" + name + '\'' +
                    ", city=" + city +
                    '}';
        }
    }
}
