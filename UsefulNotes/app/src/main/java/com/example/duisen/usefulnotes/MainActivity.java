package com.example.duisen.usefulnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    Boolean error=false;
    ImageButton addButton;
    TextView notification;
    ListView lv_notes;
    ListAdapter adapter;
    ArrayList<String> notes;
    DBHelper helper;
    View view1,view2;
    int counter =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DBHelper(this,null,1);
        notes = helper.getAllNotes();
        adapter = new ListAdapter(this,notes);
        lv_notes = (ListView)findViewById(R.id.listView);
        addButton = (ImageButton)findViewById(R.id.imageButton);
        notification = (TextView)findViewById(R.id.textView3);
        lv_notes.setAdapter(adapter);

        MainActivity.NotificationVisiblityforMainActivity(notes, notification);

        lv_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (error != false)
                    return;
                else if (counter == 4)
                    counter = 1;
                else
                    counter++;

                try {
                    getIntent().getExtras().get("isWidget");
                    Intent in = new Intent();
                    in.putExtra("Note",notes.get(position).split("&")[0]);
                    setResult(Activity.RESULT_OK, in);
                    finish();
                }catch (Exception e) {
                     e.getMessage();
                }
                String note = notes.get(position).split("&")[0];
                String date = notes.get(position).split("&")[2];
                String tmp = note + "&" + counter + "&" + date;
                notes.set(position, tmp);
                adapter.notifyDataSetChanged();
                helper.updateBG(counter, note, date);
            }
        });

        lv_notes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                error = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inf = LayoutInflater.from(MainActivity.this);
                view2 = inf.inflate(R.layout.add_note, null);
                EditText e1 = (EditText) view2.findViewById(R.id.editText);
                e1.setText(notes.get(position).split("&")[0]);
                builder.setView(view2);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        error=false;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText e1 = (EditText) view2.findViewById(R.id.editText);
                        String new_note = e1.getText().toString();
                        error=false;
                        if (new_note.length() > 0) {
                            String old_note = notes.get(position).split("&")[0];
                            String old_date = notes.get(position).split("&")[2];
                            String background = notes.get(position).split("&")[1];
                            String new_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            notes.remove(position);
                            helper.deleteNote(old_note, old_date);
                            notes.add(0, new_note + "&" + background + "&" + new_date);
                            adapter.notifyDataSetChanged();
                            helper.addNote(new_note, Integer.parseInt(background), new_date);

                        }
                    }
                });
                builder.show();
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inf = LayoutInflater.from(MainActivity.this);
                view1 = inf.inflate(R.layout.add_note, null);
                builder.setView(view1);
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText e1 = (EditText) view1.findViewById(R.id.editText);
                        String note = e1.getText().toString().trim().replaceAll("[']","");
                        if (e1.length() > 0) {
                            Random r = new Random();
                            int background = r.nextInt(4);
                            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            notes.add(0, note + "&" + background + "&" + date);
                            adapter.notifyDataSetChanged();
                            helper.addNote(note, background, date);
                        }
                        MainActivity.NotificationVisiblityforMainActivity(notes,notification);
                    }
                });
                builder.create().show();
            }
        });
       }
    static void NotificationVisiblityforMainActivity(ArrayList<String> notes, TextView notification){
        if(notes.size()>0)
            notification.setVisibility(View.INVISIBLE);
        else
            notification.setVisibility(View.VISIBLE);
    }

}
