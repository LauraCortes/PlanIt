package com.example.laura.planit.Activities.SoyContacto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.laura.planit.Activities.Eventos.RegresoRecyclerViewAdapter;
import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Modelos.Movimiento;
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

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by Laura on 25/11/2016.
 */

public class MovimientosActivity extends AppCompatActivity {

    private String numero_contacto;
    private List<Movimiento> elementos;
    private RecyclerView recyclerView;

    public MovimientosActivity(){
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

    //Brahian agregó el siguiente método
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        numero_contacto=(String)getIntent().getExtras().get("numero_contacto");


        super.onCreate(savedInstanceState);
        elementos= new ArrayList();
        final MovimientoRecyclerViewAdapter adapter=new MovimientoRecyclerViewAdapter(this, (List<Movimiento>)elementos);
        setContentView(R.layout.movimiento_activity);
        //ToolBar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_movimientos));
        getSupportActionBar().setTitle("Movimientos del usuario");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMovimientos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child("movimientos/" + numero_contacto);
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        GenericTypeIndicator<HashMap<String,Movimiento>> t = new GenericTypeIndicator<HashMap<String,Movimiento>>(){};
                        HashMap<String, Movimiento> map =dataSnapshot.getValue(t);
                        if(map!=null)
                        {
                            List<Movimiento> nuevos = new ArrayList(map.values());
                            elementos=nuevos;
                            ((MovimientoRecyclerViewAdapter)adapter).swapData(nuevos);
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
