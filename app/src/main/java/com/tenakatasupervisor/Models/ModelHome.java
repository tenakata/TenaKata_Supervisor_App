package com.tenakatasupervisor.Models;


import java.util.List;

public class ModelHome {


    /**
     * status : 200
     * message : supervisor list!!
     * result : [{"id":"35","business_name":"tewst","owner_name":"am","phone":"7836048634","supervisor_id":"1"},{"id":"36","business_name":"tewst","owner_name":"am","phone":"7836048612","supervisor_id":"1"},{"id":"37","business_name":"aaaaaaa","owner_name":"bbbbbb","phone":"9885588665","supervisor_id":"1"},{"id":"38","business_name":"tewst","owner_name":"am","phone":"7836048678","supervisor_id":"1"}]
     */

    private String status;
    private String message;
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 35
         * business_name : tewst
         * owner_name : am
         * phone : 7836048634
         * supervisor_id : 1
         */

        private String id;
        private String business_name;
        private String owner_name;
        private String phone;
        private String supervisor_id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBusiness_name() {
            return business_name;
        }

        public void setBusiness_name(String business_name) {
            this.business_name = business_name;
        }

        public String getOwner_name() {
            return owner_name;
        }

        public void setOwner_name(String owner_name) {
            this.owner_name = owner_name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSupervisor_id() {
            return supervisor_id;
        }

        public void setSupervisor_id(String supervisor_id) {
            this.supervisor_id = supervisor_id;
        }
    }
}