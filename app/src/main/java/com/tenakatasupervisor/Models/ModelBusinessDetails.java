package com.tenakatasupervisor.Models;

public class ModelBusinessDetails {

    /**
     * status : 200
     * message : Show Business Details !!
     * result : {"cash_sale":27600,"cash_purchase":180000,"credit_sale":201,"credit_purchase":30128.52}
     * total_cash : 27801
     * total_credit : 210128.52
     * total_avrage_cash : 13900.5
     * total_avrage_credit : 105064.26
     */

    private String status;
    private String message;
    private ResultBean result;
    private int total_cash;
    private double total_credit;
    private double total_avrage_cash;
    private double total_avrage_credit;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getTotal_cash() {
        return total_cash;
    }

    public void setTotal_cash(int total_cash) {
        this.total_cash = total_cash;
    }

    public double getTotal_credit() {
        return total_credit;
    }

    public void setTotal_credit(double total_credit) {
        this.total_credit = total_credit;
    }

    public double getTotal_avrage_cash() {
        return total_avrage_cash;
    }

    public void setTotal_avrage_cash(double total_avrage_cash) {
        this.total_avrage_cash = total_avrage_cash;
    }

    public double getTotal_avrage_credit() {
        return total_avrage_credit;
    }

    public void setTotal_avrage_credit(double total_avrage_credit) {
        this.total_avrage_credit = total_avrage_credit;
    }

    public static class ResultBean {
        /**
         * cash_sale : 27600
         * cash_purchase : 180000
         * credit_sale : 201
         * credit_purchase : 30128.52
         */

        private int cash_sale;
        private int cash_purchase;
        private int credit_sale;
        private double credit_purchase;

        public int getCash_sale() {
            return cash_sale;
        }

        public void setCash_sale(int cash_sale) {
            this.cash_sale = cash_sale;
        }

        public int getCash_purchase() {
            return cash_purchase;
        }

        public void setCash_purchase(int cash_purchase) {
            this.cash_purchase = cash_purchase;
        }

        public int getCredit_sale() {
            return credit_sale;
        }

        public void setCredit_sale(int credit_sale) {
            this.credit_sale = credit_sale;
        }

        public double getCredit_purchase() {
            return credit_purchase;
        }

        public void setCredit_purchase(double credit_purchase) {
            this.credit_purchase = credit_purchase;
        }
    }
}
