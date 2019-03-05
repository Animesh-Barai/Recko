package com.example.neelanshsethi.recko;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementTextEmail;
import me.riddhimanadib.formmaster.model.FormHeader;

public class PaymentDynamicActivity  extends Activity {

    RecyclerView mRecyclerView;
    FormBuilder mFormBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payment_dynamic);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(this, mRecyclerView);

        //FormHeader header = FormHeader.createInstance("Enter client’s details");
        FormElementTextEmail element = FormElementTextEmail.createInstance().setTitle("Name").setHint("Enter User Name");
        FormElementTextEmail element1 = FormElementTextEmail.createInstance().setTitle("Email").setHint("Enter User Email");

// add them in a list
        List<BaseFormElement> formItems = new ArrayList<>();
        //formItems.add(header);
        formItems.add(element);
        formItems.add(element1);
        formItems.add(FormElementTextEmail.createInstance().setTitle("Amount").setHint("Enter amount"));
        formItems.add(FormElementTextEmail.createInstance().setTitle("Mobile No").setHint("Enter Mobile No"));
        formItems.add(FormElementTextEmail.createInstance().setTitle("Location").setHint("Enter Location"));

// build and display the form
        mFormBuilder.addFormElements(formItems);
        //mFormBuilder.refreshView();
    }
}
