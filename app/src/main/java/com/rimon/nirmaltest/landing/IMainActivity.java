package com.rimon.nirmaltest.landing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rimon.nirmaltest.R;
import com.rimon.nirmaltest.appconfig.Constances;
import com.rimon.nirmaltest.landing.adapter.SmsAdapter;
import com.rimon.nirmaltest.landing.model.Sms;
import com.rimon.nirmaltest.landing.presenter.IMainPresenter;
import com.rimon.nirmaltest.landing.presenter.MainPresenter;
import com.rimon.nirmaltest.landing.view.IMainView;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class IMainActivity extends AppCompatActivity implements IMainView,EasyPermissions.PermissionCallbacks {

    IMainPresenter iMainPresenter;
    Button btnSubmit;
    EditText etPhoneNo;
    LinearLayout llEmptyView;
    String address = "";
    String msg_id = "";
    String thread_id = "";
    String date = "";
    String type = "";
    List<Sms> smsArrayList = new ArrayList<>();
    List<Sms> smsDeleatedArrayList = new ArrayList<>();
    RecyclerView rvSmsList;
    SmsAdapter smsAdapter;
    private static final int RC_READ_SMS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSubmit = findViewById(R.id.btnSubmit);
        etPhoneNo = findViewById(R.id.etPhoneNo);
        rvSmsList = findViewById(R.id.rvSmsList);
        rvSmsList = findViewById(R.id.rvSmsList);
        llEmptyView = findViewById(R.id.llEmptyView);
        iMainPresenter = new MainPresenter(this);
        iMainPresenter.fetchData();
        getSMSTask();



    }
    public void btnRetry(View v){
        getSMSTask();
    }

    private boolean hasSMSReadPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_SMS);
    }

    @AfterPermissionGranted(RC_READ_SMS)
    public void getSMSTask() {
        if (hasSMSReadPermission()) {
            // Have permission, do the thing!

            address = etPhoneNo.getText().toString();
            smsArrayList = getAllSms();
            smsDeleatedArrayList = getFilteredList(smsArrayList);
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final String myPackageName = getPackageName();
                   if (  !Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {

                    Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                    intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                    startActivityForResult(intent, 1);
                }else {
                    smsArrayList = getSingleUserSms(address);                }
            }else {
                smsArrayList = getSingleUserSms(address);
            }
*/

            smsAdapter = new SmsAdapter(smsDeleatedArrayList);
            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(getParent(), RecyclerView.VERTICAL, false);
            rvSmsList.setLayoutManager(linearLayoutManager);
            rvSmsList.setAdapter(smsAdapter);
            if (smsAdapter.getItemCount() == 0){
                llEmptyView.setVisibility(View.VISIBLE);
                rvSmsList.setVisibility(View.GONE);
            }
            Toast.makeText(this, "TODO: Has SMS Read Permission", Toast.LENGTH_LONG).show();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_camera),
                    RC_READ_SMS,
                    Manifest.permission.READ_SMS);
        }
    }

    public List<Sms> getFilteredList(List<Sms> smsArrayList){

        for(int i=0;i<smsArrayList.size();i++){
            Sms sms = smsArrayList.get(i);
            if(Constances.isValidPhoneNumber(sms.getAddress())){
                System.out.println("Yes this is a valid no ");

            }else {
                if(sms.getMsg().contains("OTP")){
                    //hideItem(position);
                }else {
                    smsDeleatedArrayList.add(sms);
                }

            }
        }
        return smsDeleatedArrayList;
    }


    public List<Sms> getSingleUserSms(String phNo){
        List<Sms> smsList = new ArrayList<>();
        try {

            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            Cursor cur = getContentResolver().query(uriSMSURI, new String[] { "_id", "thread_id", "address", "date", "body",
                    "type" }, "address=?", new String[] { address },null);
            String sms = "";
            int totalSMS = cur.getCount();
            System.out.println("totalSMS : "+totalSMS);
            if(totalSMS==0){
                Toast.makeText(getApplicationContext(),"Please input Right No :",Toast.LENGTH_LONG).show();
            }

            if (cur.moveToFirst()) {
                for (int i = 0; i < totalSMS; i++) {
                    msg_id = cur.getString(0);
                    thread_id = cur.getString(1);
                    String  address_b = cur.getString(2);
                    date = cur.getString(3);
                    String   msg_body = cur.getString(4);
                    type = cur.getString(5);
                    System.out.println("this is the SMS : "+sms);
                    System.out.println("this is the msg_id : "+msg_id);
                    System.out.println("this is the thread_id : "+thread_id);
                    System.out.println("this is the address_b : "+address_b);
                    System.out.println("this is the date : "+date);
                    System.out.println("this is the msg_body : "+msg_body);
                    System.out.println("this is the type : "+type);

                    Sms objSms = new Sms();
                    objSms.setId(cur.getString(0));
                    objSms.setMsg(msg_body);
                    objSms.setFolderName(address_b);
                    smsList.add(objSms);
                    cur.moveToNext();
                }
            }
            // else {
            // throw new RuntimeException("You have no SMS");
            // }
            cur.close();

                    /*while (cur.moveToNext()) {
                        msg_id = cur.getString(0);
                        thread_id = cur.getString(1);
                        address_b = cur.getString(2);
                        date = cur.getString(3);
                        msg_body = cur.getString(4);
                        type = cur.getString(5);
                        System.out.println("this is the SMS : "+sms);
                        System.out.println("this is the msg_id : "+msg_id);
                        System.out.println("this is the thread_id : "+thread_id);
                        System.out.println("this is the address_b : "+address_b);
                        System.out.println("this is the date : "+date);
                        System.out.println("this is the msg_body : "+msg_body);
                        System.out.println("this is the type : "+type);
                        //sms += "From :" + cur.getString(2) + " : " + cur.getString(11)+"\n";

                    }
                    tvHeading.setText(""+address_b);
                    tvBody.setText(""+msg_body);*/


        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception : "+e.getMessage());
            Toast.makeText(getApplicationContext(),"Please give permission and input Right No :",Toast.LENGTH_LONG).show();
        }


        return smsList;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public List<Sms> getAllSms() {
        llEmptyView.setVisibility(View.GONE);
        rvSmsList.setVisibility(View.VISIBLE);
        List<Sms> lstSms = new ArrayList<Sms>();
        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        startManagingCursor(c);
        int totalSMS = c.getCount();

        System.out.println("totalSMS : "+totalSMS);
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                lstSms.add(objSms);
                c.moveToNext();
            }
        }else {
            llEmptyView.setVisibility(View.VISIBLE);
            rvSmsList.setVisibility(View.GONE);
            Toast.makeText(
                    this, "Their is no Message to read",
                    Toast.LENGTH_LONG)
                    .show();
        }
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();

        return lstSms;
    }

    @Override
    public void setData(String setData) {
        System.out.println("setData : "+setData);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this, "Please enable Sms Read Permission manually ",
                    Toast.LENGTH_LONG)
                    .show();
        }


        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    final String myPackageName = getPackageName();
                    if (Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {

                        smsArrayList = getSingleUserSms(address);
                        smsAdapter = new SmsAdapter(smsArrayList);
                        LinearLayoutManager linearLayoutManager;
                        linearLayoutManager = new LinearLayoutManager(getParent(), RecyclerView.VERTICAL, false);
                        rvSmsList.setLayoutManager(linearLayoutManager);
                        rvSmsList.setAdapter(smsAdapter);
                        Toast.makeText(this, "TODO: Has SMS Read Permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        llEmptyView.setVisibility(View.VISIBLE);
        rvSmsList.setVisibility(View.GONE);
    }
}
