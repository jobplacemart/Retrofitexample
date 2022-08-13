package com.example.realmdatabasedemo.desborad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.realmdatabasedemo.R;
import com.example.realmdatabasedemo.model.RealmModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.firstName)
    EditText  firstName;

    @BindView(R.id.LastName)
    EditText LastName;

    @BindView(R.id.submit)
    Button submit;

    @BindView(R.id.dataList)
    Button dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.submit)
    void submit(){
        Insert();
    }

    @OnClick(R.id.dataList)
    void dataList(){
        Intent intent = new Intent(MainActivity.this, DataListActivity.class);
        startActivity(intent);
    }
    public void Insert(){
        Realm realm=Realm.getDefaultInstance();
        RealmModel realmModel =new RealmModel();
        Number current_id=realm.where(RealmModel.class).max("id");
        long nextId;
        if(current_id==null){
            nextId=1;
        }
        else{
            nextId=current_id.intValue()+1;
        }
        realmModel.setId(nextId);
        realmModel.setFirstname("" +firstName.getText().toString().trim());
        realmModel.setLastname("" +LastName.getText().toString().trim());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(realmModel);
            }
        });
    }
}