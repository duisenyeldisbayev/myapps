package com.example.duisen.usefulnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Duisen on 10.03.2016.
 */
public class ListAdapter extends ArrayAdapter {
    LinearLayout Layout;
    ArrayList<String> notes2;
    TextView note_date,note;
    DBHelper helper2 = new DBHelper(getContext(),null,1);
    ImageButton delete_button;
    GridLayout changable_layout;
    public ListAdapter(Context context, ArrayList<String> notes) {
        super(context,R.layout.item_row, notes);
        notes2 = notes;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = LayoutInflater.from(getContext());
        View noteView = inf.inflate(R.layout.item_row,parent,false);
        Layout = (LinearLayout)noteView.findViewById(R.id.deleteView);
        note = (TextView)noteView.findViewById(R.id.textView);
        note_date =(TextView)noteView.findViewById(R.id.textView2);
        delete_button = (ImageButton)noteView.findViewById(R.id.imageButton2);
        changable_layout = (GridLayout)noteView.findViewById(R.id.changable_layout);
        changable_layout.setBackgroundResource(R.drawable.shape1);
        int num = Integer.parseInt(notes2.get(position).split("&")[1]);
        String date = notes2.get(position).split("&")[2];

        changeBG(changable_layout,num);
        note.setText(notes2.get(position).split("&")[0].trim().replaceAll("[']",""));
        note_date.setText(date.split("-")[2].substring(0,2)+"."+date.split("-")[1]+"."+date.split("-")[0]);

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete this note?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper2.deleteNote(notes2.get(position).split("&")[0], notes2.get(position).split("&")[2]);
                        notes2.remove(position);
                        notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });
        return noteView;
    }
    public void changeBG(GridLayout layout, int num){
        if(num==1){
            layout.setBackgroundResource(R.drawable.shape1);
        }
        else if(num==2){
            layout.setBackgroundResource(R.drawable.shape2);
        }
        else if(num==3){
            layout.setBackgroundResource(R.drawable.shape3);
        }
        else if(num==4){
            layout.setBackgroundResource(R.drawable.shape4);
        }

    }
}
