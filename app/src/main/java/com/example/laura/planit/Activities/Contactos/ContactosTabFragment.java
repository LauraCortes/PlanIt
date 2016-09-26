package com.example.laura.planit.Activities.Contactos;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.CharacterPickerDialog;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laura on 14/09/2016.
 */
public class ContactosTabFragment extends Fragment implements View.OnKeyListener
{
    private ContactAdapter contactAdapter;
    private boolean eliminar;
    List<Contacto> contactos;
    List<Contacto> contactosEliminar;
    FloatingActionButton btnFAB;


    public ContactosTabFragment()
    {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contactos=PlanIt.darInstancia().darContactos();
        contactosEliminar = new ArrayList<Contacto>();
        contactAdapter=new ContactAdapter(getActivity(), contactos);
        eliminar=false;

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_contactos, container, false);
        final ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(contactAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View v, int pos, long id)
            {
                eliminar=true;
                cambiarFAB(true);
                View rowView = listView.getChildAt(pos);
                Contacto contacto=contactos.get(pos);
                if (rowView != null) {
                    if(rowView.isSelected())
                    {
                        rowView.setBackgroundColor(Color.parseColor("#FFCCBC"));
                        contactosEliminar.add(contacto);
                    }
                    else
                    {
                        rowView.setBackgroundColor(Color.TRANSPARENT);
                        contactosEliminar.remove(contacto);
                    }
                }
                if(contactosEliminar.size()==0)
                {
                    cambiarFAB(false);
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
            {
                Toast.makeText(getContext(),"Click eliminar",Toast.LENGTH_SHORT).show();
                if(eliminar)
                {
                    View rowView = listView.getChildAt(pos);
                    Contacto contacto=contactos.get(pos);
                    if (rowView != null) {
                        if(rowView.isSelected())
                        {
                            rowView.setBackgroundColor(Color.parseColor("#FFCCBC"));
                            contactosEliminar.add(contacto);
                        }
                        else
                        {
                            rowView.setBackgroundColor(Color.TRANSPARENT);
                            contactosEliminar.remove(contacto);
                        }
                    }
                    if(contactosEliminar.size()==0)
                    {
                        cambiarFAB(false);
                    }

                }
            }
        });

        btnFAB= (FloatingActionButton) view.findViewById(R.id.fabContactos);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Activity:"+getActivity().getLocalClassName());
                ((MainActivity)getActivity()).agregarContactos(v);
            }
        });


        return view;
    }

    /**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        if (PlanIt.darInstancia().darContactos().size() == 0) {
            Intent i = new Intent(this, AgregarContactoActivity.class);
            finish();
            startActivity(i);
        }
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ContactAdapter(this,PlanIt.darInstancia().darContactos()));
    }
    **/

    public void cambiarFAB(boolean eliminar)
    {

        if(eliminar)
        {
            btnFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.delete));
            btnFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark)));
            btnFAB.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    //TODO método eliminar
                    eliminarContactos(v);
                }
            });
        }
        else
        {
            btnFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.agregar));
            btnFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.colorAccent)));
            btnFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).agregarContactos(v);
                }
            });
        }
    }


    public void eliminarContactos(View view)
    {
        contactos.removeAll(contactosEliminar);
        contactAdapter.notifyDataSetChanged();
        /**
       ListView listView = (ListView) findViewById(android.R.id.list);
        for (int i = 0; i <listView.getAdapter().getCount() ; i++)
        {
            ViewGroup row = (ViewGroup) listView.getChildAt(i);
            CheckBox tvTest = (CheckBox) row.findViewById(R.id.checkBox);
            //  Get your controls from this ViewGroup and perform your task on them =)
            if (tvTest.isChecked())
            {
                // DO SOMETHING
                TextView tN= (TextView) row.findViewById(R.id.textViewTelefono);
                PlanIt.darInstancia().eliminarContacto(i);
                Intent intent = new Intent(this, PersitenciaService.class);
                intent.putExtra("Requerimiento","EliminarContacto");
                intent.putExtra("Telefono",tN.getText().toString());
                startService(intent);

            }
        }

        listView.setAdapter(new ContactAdapter(this,PlanIt.darInstancia().darContactos()));
         */
    }


    public boolean onKey(View view, int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                //TODO cancelar borrado
                return true;
        }
        return false;
    }
}
