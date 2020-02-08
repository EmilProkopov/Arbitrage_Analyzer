package com.course_project.arbitrage_analyzer.model;

//Information about deal.
public class Deal {
        private DealType type;
        private String marketName;
        private Double amount;
        private Double price;

        public Deal(DealType t, String n, Double a, Double p) {
            type = t;
            marketName = n;
            amount = a;
            price = p;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        Double getAmount() {
            return amount;
        }

        void setAmount(Double amount) {
            this.amount = amount;
        }

        DealType getType() {
            return type;
        }

        public void setType(DealType type) {
            this.type = type;
        }

        String getMarketName() {
            return marketName;
        }

    }