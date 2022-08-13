package com.example.realmdatabasedemo.desborad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.realmdatabasedemo.adapter.ExampleAdapter;
import com.example.realmdatabasedemo.R;
import com.example.realmdatabasedemo.model.RealmModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class DataListActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @BindView(R.id.allDeleteText)
    TextView allDeleteText;

    private ExampleAdapter mAdapter;
    RealmResults<RealmModel> realmModels;
    ArrayList<Boolean> setSelection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        ButterKnife.bind(this);
        setAdapter();
    }

    void setAdapter(){
        Realm realm=Realm.getDefaultInstance();
        realmModels=realm.where(RealmModel.class).findAll();
        setAdapterSelection();
        allDeleteText.setVisibility(View.GONE);
        mAdapter = new ExampleAdapter(realmModels,setSelection,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(mAdapter);
        dataFound();
    }

    public void dataFound(){
        if(realmModels.size() == 0){
            allDeleteText.setVisibility(View.VISIBLE);
        }else {
            allDeleteText.setVisibility(View.GONE);
        }
    }

    void setAdapterSelection(){
        for ( int i = 0; i < realmModels.size(); i++){
            setSelection.add(false);
        }
    }
}