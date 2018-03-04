package com.dawoo.gamebox.bean;

import java.util.List;

/**
 * Created by benson on 17-12-27.
 */

public class Banner {

    private List<BannerBean> banner;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class BannerBean {
        /**
         * carousel_id : 10428
         * end_time : 1514736000000
         * link : www.tianmao.com
         * language : zh_CN
         * type : carousel_type_phone
         * cover : gb/21/carousel/10428/1513911701668.jpg
         * start_time : 1513928106000
         * update_time : 1513928115502
         * name : 叁拾年
         * id : 10242
         * order_num : 1
         * status : true
         */

        private int carousel_id;
        private long end_time;
        private String link;
        private String language;
        private String type;
        private String cover;
        private long start_time;
        private long update_time;
        private String name;
        private int id;
        private int order_num;
        private boolean status;

        public int getCarousel_id() {
            return carousel_id;
        }

        public void setCarousel_id(int carousel_id) {
            this.carousel_id = carousel_id;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public long getStart_time() {
            return start_time;
        }

        public void setStart_time(long start_time) {
            this.start_time = start_time;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getOrder_num() {
            return order_num;
        }

        public void setOrder_num(int order_num) {
            this.order_num = order_num;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
