package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Activities.Main.LoginActivity;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Regreso;
import com.example.laura.planit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Laura on 25/11/2016.
 */

public class RegresosActivity extends AppCompatActivity {

    private String id_evento;
    private List<Regreso> elementos;
    private RecyclerView recyclerView;

    public RegresosActivity(){
        super();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        id_evento=(String)getIntent().getExtras().get("id_evento");


        super.onCreate(savedInstanceState);
        elementos= new ArrayList();
        final RegresoRecyclerViewAdapter adapter=new RegresoRecyclerViewAdapter(this, (List<Regreso>)elementos);
        setContentView(R.layout.activity_inscribir_regreso);
        //ToolBar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_regresos));
        getSupportActionBar().setTitle("Regresos del evento");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRegresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child("regresos/" + id_evento);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        GenericTypeIndicator<HashMap<String,Regreso>> t = new GenericTypeIndicator<HashMap<String, Regreso>>(){};
                        HashMap<String, Regreso> map =dataSnapshot.getValue(t);
                        if(map!=null)
                        {
                            List<Regreso> nuevos = new ArrayList(map.values());
                            for(int i=0;i<nuevos.size();i++)
                            {
                                nuevos.get(i).id_evento=id_evento;
                            }
                            elementos=nuevos;
                            ((RegresoRecyclerViewAdapter)adapter).swapData(nuevos);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}
