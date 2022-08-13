package com.example.realmdatabasedemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.realmdatabasedemo.desborad.DataListActivity;
import com.example.realmdatabasedemo.R;
import com.example.realmdatabasedemo.model.RealmModel;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    List<RealmModel> realmModels;
    Context context;
    ArrayList<Boolean> setSelection;

    public ExampleAdapter(RealmResults<RealmModel> realmModels,ArrayList<Boolean> setSelection, Context context) {
        this.realmModels = realmModels;
        this.context = context;
        this.setSelection = setSelection;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detile_list, parent, false);
        return new ExampleViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ExampleAdapter.ExampleViewHolder holder, int position) {
        RealmModel realmModel = (RealmModel) realmModels.get(position);
        holder.name.setText("" +realmModel.getFirstname());
        holder.lastname.setText("" +realmModel.getLastname());

        if(setSelection.get(position)){
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.lastname.setTextColor(ContextCompat.getColor(context, R.color.white));

        }else {
            holder.cardView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.name.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.lastname.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm=Realm.getDefaultInstance();
                int id = (int) realmModels.get(position).getId();
                final RealmModel realmModel1 =realm.where(RealmModel.class).equalTo("id",id).findFirst();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realmModel1.deleteFromRealm();
                    }
                });
                notifyDataSetChanged();
                ((DataListActivity)context).dataFound();
            }
        });
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm=Realm.getDefaultInstance();
                int id = (int) realmModels.get(position).getId();
                final RealmModel realmModel=realm.where(RealmModel.class).equalTo("id",id).findFirst();
                ShowUpdateDialog(realmModel);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < realmModels.size(); i++ ){
                    if(i == position ){
                        setSelection.set(i,true);
                    }else {
                        setSelection.set(i,false);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return realmModels.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.lastname)
        TextView lastname;

        @BindView(R.id.iv_delete)
        ImageView iv_delete;

        @BindView(R.id.iv_edit)
        ImageView iv_edit;

        @BindView(R.id.cardView)
        CardView cardView;

        public ExampleViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void ShowUpdateDialog(final RealmModel realmModel) {

        Realm realm=Realm.getDefaultInstance();
        final EditText firstnameEditText = new EditText(context);
        final EditText lastnameEditText = new EditText(context);

        firstnameEditText.setText(realmModel.getFirstname());
        lastnameEditText.setText(realmModel.getLastname());
        firstnameEditText.setWidth(100);
        lastnameEditText.setWidth(100);

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(firstnameEditText);
        linearLayout.addView(lastnameEditText);

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(context)
                .setTitle("Edit User")
                .setView(linearLayout)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realmModel.setFirstname(firstnameEditText.getText().toString());
                                realmModel.setLastname(lastnameEditText.getText().toString());
                                realm.copyToRealmOrUpdate(realmModel);
                                notifyDataSetChanged();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
}
