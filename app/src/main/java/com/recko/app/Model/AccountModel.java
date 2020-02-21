package com.recko.app.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class AccountModel implements Serializable {
    String payee_name, bank_name, account_no, ifsc_code;

    public AccountModel(String payee, String bank, String account, String ifsc) {
        payee_name = payee;
        bank_name = bank;
        account_no = account;
        ifsc_code = ifsc;
    }

    public AccountModel(){}

    public void populateUsingJson(JSONObject json_data) throws JSONException {
        payee_name = bank_name = account_no = ifsc_code = "";
        payee_name = json_data.has("payee_name")?json_data.getString("payee_name"):payee_name;
        bank_name = json_data.has("bank_name")?json_data.getString("bank_name"):bank_name;
        account_no = json_data.has("account_no")?json_data.getString("account_no"):account_no;
        ifsc_code = json_data.has("ifsc_code")?json_data.getString("ifsc_code"):ifsc_code;
    }

    public String getPayee_name(){return payee_name;}
    public String getBank_name(){return bank_name;}
    public String getIfsc_code(){return ifsc_code;}
    public String getAccount_no(){return account_no;}

    public void setPayee_name(String name){payee_name =  name;}
    public void setBank_name(String name){bank_name =  name;}
    public void setAccount_no(String no){account_no =  no;}
    public void setIfsc_code(String ifsc){ifsc_code =  ifsc;}

}
