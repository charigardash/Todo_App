package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView=findViewById(R.id.listView);
        final TextAdapter adapter=new TextAdapter();
        listView.setAdapter(adapter);
        readInfo();
        adapter.setData(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete this task?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);

                                adapter.setData(list);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("No",null)
                        .create();
                        dialog.show();
            }
        });
        final Button newTaskButton=findViewById(R.id.button);
        newTaskButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                final EditText taskInput=new EditText(MainActivity.this);
                taskInput.setSingleLine();
                AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add a new Task...")
                        .setMessage("What is your new Task?")
                        .setView(taskInput)
                        .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskInput.getText().toString());

                                adapter.setData(list);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
            }
        });

        final Button deleteAll=findViewById(R.id.deleteTaskButton);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete All Task")
                        .setPositiveButton("DeleteAll", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.clear();
                                adapter.setData(list);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
            }
        });
    }

    private void readInfo(){
        File file=new File(this.getFilesDir(),"saved");
        if(!file.exists()){
            return;
        }try {
            FileInputStream fin = new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fin));
            String line=br.readLine();
            while(line!=null){
                list.add(line);
                line=br.readLine();
            }br.close();
            fin.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void saveInfo(){
        try{
            File file=new File(this.getFilesDir(),"saved");
            FileOutputStream fout=new FileOutputStream(file);
            BufferedWriter br=new BufferedWriter(new OutputStreamWriter(fout));
            for(int i=0;i<list.size();i++){
                br.write(list.get(i));
                br.newLine();
            }br.close();
            fout.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    class TextAdapter extends BaseAdapter {
        ArrayList<String> list=new ArrayList<>();
        void setData(List<String> l){
            list.clear();
            list.addAll(l);
            notifyDataSetChanged();
        }
        @Override
        public int getCount(){
           return list.size();
        }
        @Override
        public Object getItem(int position){
           return list.get(position);
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView==null) {
                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                convertView = inflater.inflate(R.layout.item, parent, false);
            }
            TextView textView=convertView.findViewById(R.id.task);
            textView.setTextSize(17);
            textView.setText(list.get(position));
            return convertView;
        }

    }
}
