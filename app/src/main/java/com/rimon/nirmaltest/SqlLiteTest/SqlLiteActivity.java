package com.rimon.nirmaltest.SqlLiteTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.rimon.nirmaltest.AppController;
import com.rimon.nirmaltest.R;

import com.rimon.nirmaltest.SqlLiteTest.model.UserData;

import java.util.List;

public class SqlLiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sql_lite);

        Log.d("Insert: ", "Inserting ..");
        AppController.addUserData(new UserData("Ravi", "9100000000"));
        AppController.addUserData(new UserData("Srinivas", "9199999999"));
        AppController.addUserData(new UserData("Tommy", "9522222222"));
        AppController.addUserData(new UserData("Karthik", "9533333333"));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<UserData> data = AppController.getAllUserData();

        for (UserData dt : data) {
            String log = "Id: "+dt.getId()+" ,Name: " + dt.getName() + " ,Phone: " + dt.getEmail();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }
    }
}
