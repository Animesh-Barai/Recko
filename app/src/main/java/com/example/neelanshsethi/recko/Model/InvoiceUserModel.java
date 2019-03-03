package com.example.neelanshsethi.recko.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InvoiceUserModel implements Serializable {

     private String customer_name;
     private String customer_mobile_no;
     private  String customer_email;
     private Map< String,Double> prod_uuid_price_map;

     public InvoiceUserModel() {
          prod_uuid_price_map = new HashMap< String,Double>();
     }

     public String getCustomer_name() {
          return customer_name;
     }

     public String getCustomer_mobile_no() {
          return customer_mobile_no;
     }

     public String getCustomer_email() {
          return customer_email;
     }

     public void setCustomer_name(String customer_name) {
          this.customer_name = customer_name;
     }

     public void setCustomer_mobile_no(String customer_mobile_no) {
          this.customer_mobile_no = customer_mobile_no;
     }

     public void setCustomer_email(String customer_email) {
          this.customer_email = customer_email;
     }

     public void setProd_uuid_price_map(Map<String, Double> prod_uuid_price_map) {
          this.prod_uuid_price_map = prod_uuid_price_map;
     }

     public Map<String, Double> getProd_uuid_price_map() {
          return prod_uuid_price_map;
     }

     public String getStringPrice(String prod_uuid) throws IllegalAccessException {
          if (!prod_uuid_price_map.containsKey(prod_uuid)) throw new IllegalAccessException(prod_uuid);
          return prod_uuid_price_map.get(prod_uuid).toString();
     }

     public void setStringPrice(String prod_uuid, String price) {
          prod_uuid_price_map.put(prod_uuid, Double.parseDouble(price));
     }



}
