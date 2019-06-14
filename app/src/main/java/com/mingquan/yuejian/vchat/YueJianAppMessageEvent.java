package com.mingquan.yuejian.vchat;

public class YueJianAppMessageEvent {
        private String message;
        public  YueJianAppMessageEvent(String message){
            this.message=message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }