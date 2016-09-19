package com.example.laura.planit.Activities.Contactos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.R;

import java.util.List;

/**
 * Created by Laura on 17/09/2016.
 */
public class AddContactAdapter extends ArrayAdapter<Contacto> {

    private LayoutInflater layoutInflater;

    public AddContactAdapter(Context context, List<Contacto> contactos) {
        super(context, 0, contactos);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // holder pattern
        Holder holder = null;
        if (convertView == null)
        {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.row_contacto_agregar, null);
            holder.setTextViewTitle((TextView) convertView.findViewById(R.id.textViewNombreAgregar));
            holder.setTextViewSubtitle((TextView) convertView.findViewById(R.id.textViewTelefonoAgregar));
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        Contacto row = getItem(position);
        holder.getTextViewTitle().setText(row.getNombre());
        holder.getTextViewSubtitle().setText(row.getNumeroTelefonico());
        return convertView;
    }

    static class Holder
    {
        TextView textViewTitle;
        TextView textViewSubtitle;
        CheckBox checkBox;

        public TextView getTextViewTitle()
        {
            return textViewTitle;
        }

        public void setTextViewTitle(TextView textViewTitle)
        {
            this.textViewTitle = textViewTitle;
        }

        public TextView getTextViewSubtitle()
        {
            return textViewSubtitle;
        }

        public void setTextViewSubtitle(TextView textViewSubtitle)
        {
            this.textViewSubtitle = textViewSubtitle;
        }
        public CheckBox getCheckBox()
        {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox)
        {
            this.checkBox = checkBox;
        }

    }
}
