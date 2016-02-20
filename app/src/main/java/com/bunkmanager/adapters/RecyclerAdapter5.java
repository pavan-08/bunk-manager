package com.bunkmanager.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkmanager.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Pavan on 28/04/2015.
 */
public class RecyclerAdapter5 extends RecyclerView.Adapter<RecyclerAdapter5.Holder>{
    private ArrayList<String> mListData = new ArrayList<>(100);
    private ArrayList<String> attend = new ArrayList<>(100);
    private ArrayList<String> miss = new ArrayList<>(100);
    private ArrayList<String> percent=new ArrayList<>(100);
    private LayoutInflater mLayoutInflater;
    public Activity activity;
    public static int num;
    public static int sNum;
    public RecyclerAdapter5(Activity act){
        this.activity=act;
        mLayoutInflater= LayoutInflater.from(act);
    }
    public RecyclerAdapter5(Activity act, int number,String hour){
        num=number;
        sNum=Integer.parseInt(hour);
        this.activity=act;
        mLayoutInflater=LayoutInflater.from(act);
    }

    @Override
    public RecyclerAdapter5.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = mLayoutInflater.inflate(R.layout.recycler_layout1, parent, false);
        Holder holder = new Holder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter5.Holder holder, final int position) {
        if(mListData.get(position).equals("")){
            holder.sub_name.setText("Add Subject");
        }
        else{
            holder.sub_name.setText(mListData.get(position));
        }
        holder.cardView.setOnClickListener( new View.OnClickListener() {
            AlertDialog.Builder dialog =new AlertDialog.Builder(activity);
            StringBuffer stringBuffer =new StringBuffer();
            ArrayList<String> subs =new ArrayList<>();

            @Override
            public void onClick(View v) {
                if(holder.sub_name.getText().toString().equals("Add Subject")){

                    try {
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                                activity.openFileInput("Subjects")));
                        int inputChar;
                        while ((inputChar = inputReader.read()) != -1) {
                            if ((char) inputChar == '~') {
                                subs.add(stringBuffer.toString());
                                stringBuffer.delete(0, stringBuffer.length());
                            } else {
                                stringBuffer.append((char) inputChar);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final CharSequence[] subjects = subs.toArray(new CharSequence[subs.size()]);

                    if(subs.size()<1){
                        Toast.makeText(activity,"Add some subjects at tab, 'Subjects'",Toast.LENGTH_SHORT).show();
                    } else {

                        dialog.setItems(subjects, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.sub_name.setText(subjects[which]);
                                mListData.set(position, subjects[which].toString());
                                saveState(String.valueOf(num) + String.valueOf(position), subjects[which].toString(), activity, Context.MODE_PRIVATE);
                            }
                        });
                        subs.clear();
                        dialog.show();
                    }
                } else {

                }
            }
        });
        if(attend.get(position).equals("")) {
            attend.set(position,"0");
            holder.attended.setText("0");

        }
        else
        {
            String mAttend = attend.get(position);
            holder.attended.setText(mAttend);
        }
        if(miss.get(position).equals("")){
            miss.set(position,"0");
            holder.missed.setText("0");

        }
        else
        {
            String mMiss = miss.get(position);
            holder.missed.setText(mMiss);
        }
        holder.number.setText(String.format("%6d",position+1)+". ");
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(holder.sub_name.getText().toString().equals("Add Subject")){

                }else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);

                    final CharSequence options[] = {"Delete", "Reset"};
                    alert.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (options[which] == "Delete") {
                                removeItem(position);
                                removeAttended(position);
                                removeMissed(position);
                            }
                            if (options[which] == "Reset") {
                                int i=Integer.parseInt(holder.attended.getText().toString());
                                int j=Integer.parseInt(holder.missed.getText().toString());
                                StringBuffer stringBuffer = new StringBuffer();

                                try{
                                    BufferedReader inputReader=new BufferedReader(new InputStreamReader(activity.openFileInput("a"+holder.sub_name.getText().toString())));
                                    String input;
                                    while((input=inputReader.readLine())!=null){
                                        stringBuffer.append(input);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                int m=Integer.parseInt(stringBuffer.toString());

                                StringBuffer stringBuffer1=new StringBuffer();
                                try{
                                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(activity.openFileInput("m"+holder.sub_name.getText().toString())));
                                    String input;
                                    while((input=inputReader.readLine())!=null){
                                        stringBuffer1.append(input);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                int n= Integer.parseInt(stringBuffer1.toString());
                                m=m-i;
                                n=n-j;
                                saveState("a"+holder.sub_name.getText().toString(),String.valueOf(m),activity, Context.MODE_PRIVATE);
                                saveState("m"+holder.sub_name.getText().toString(),String.valueOf(n),activity, Context.MODE_PRIVATE);
                                attend.set(position, "0");
                                miss.set(position, "0");
                                saveState("a" + String.valueOf(num) + String.valueOf(position), "0", activity, Context.MODE_PRIVATE);
                                saveState("m" + String.valueOf(num) + String.valueOf(position), "0", activity, Context.MODE_PRIVATE);
                                holder.attended.setText(attend.get(position));
                                holder.missed.setText(miss.get(position));

                            }
                        }
                    });
                    alert.show();

                }

                return true;
            }
        });

        holder.attended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.sub_name.getText().toString().equals("Add Subject")) {

                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    try {
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(activity.openFileInput("a" + holder.sub_name.getText().toString())));
                        String input;
                        while ((input = inputReader.readLine()) != null) {
                            stringBuffer.append(input);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int m = Integer.parseInt(stringBuffer.toString());
                    m++;
                    saveState("a" + holder.sub_name.getText().toString(), String.valueOf(m), activity, Context.MODE_PRIVATE);
                    int i = Integer.parseInt(holder.attended.getText().toString());
                    i++;
                    attend.set(position, String.valueOf(i));
                    holder.attended.setText(String.valueOf(i));
                    saveState("a" + String.valueOf(num) + String.valueOf(position), String.valueOf(i), activity, Context.MODE_PRIVATE);
                }
            }
        });
        holder.missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.sub_name.getText().toString().equals("Add Subject")) {

                } else {
                    int j = Integer.parseInt(holder.missed.getText().toString());
                    j++;
                    StringBuffer stringBuffer = new StringBuffer();
                    try {
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(activity.openFileInput("m" + holder.sub_name.getText().toString())));
                        String input;
                        while ((input = inputReader.readLine()) != null) {
                            stringBuffer.append(input);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int m = Integer.parseInt(stringBuffer.toString());
                    m++;
                    saveState("m" + holder.sub_name.getText().toString(), String.valueOf(m), activity, Context.MODE_PRIVATE);
                    miss.set(position, String.valueOf(j));
                    holder.missed.setText(String.valueOf(j));
                    saveState("m" + String.valueOf(num) + String.valueOf(position), String.valueOf(j), activity, Context.MODE_PRIVATE);
                }
            }
        });
        holder.attended.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.sub_name.getText().toString().equals("Add Subject")) {
                    return false;
                } else {
                    int i = Integer.parseInt(holder.attended.getText().toString());
                    if(i==0)
                        return true;
                    i--;
                    StringBuffer stringBuffer = new StringBuffer();

                    try {
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(activity.openFileInput("a" + holder
                                .sub_name.getText().toString())));
                        String input;
                        while ((input = inputReader.readLine()) != null) {
                            stringBuffer.append(input);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int m = Integer.parseInt(stringBuffer.toString());
                    m--;
                    saveState("a" + holder.sub_name.getText().toString(), String.valueOf(m), activity, Context.MODE_PRIVATE);
                    attend.set(position, String.valueOf(i));
                    holder.attended.setText(String.valueOf(i));
                    saveState("a" + String.valueOf(num) + String.valueOf(position), String.valueOf(i), activity, Context.MODE_PRIVATE);
                    return true;
                }
            }
        });

        holder.missed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.sub_name.getText().toString().equals("Add Subject")) {
                    return false;
                } else {
                    int j = Integer.parseInt(holder.missed.getText().toString());
                    if(j==0)
                        return true;
                    j--;
                    StringBuffer stringBuffer = new StringBuffer();
                    try {
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(activity.openFileInput("m" + holder.sub_name
                                .getText().toString())));
                        String input;
                        while ((input = inputReader.readLine()) != null) {
                            stringBuffer.append(input);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int m = Integer.parseInt(stringBuffer.toString());
                    m--;
                    saveState("m" + holder.sub_name.getText().toString(), String.valueOf(m), activity, Context.MODE_PRIVATE);
                    miss.set(position, String.valueOf(j));
                    holder.missed.setText(String.valueOf(j));
                    saveState("m" + String.valueOf(num) + String.valueOf(position), String.valueOf(j), activity, Context.MODE_PRIVATE);
                    return true;
                }
            }
        });
    }
    public void saveState(String file, String content,Context context,int mode)
    {
        try {
            FileOutputStream fos=context.openFileOutput(file,mode);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void removeItem(int position) {
        int i=Integer.parseInt(attend.get(position));
        int j=Integer.parseInt(miss.get(position));
        StringBuffer stringBuffer = new StringBuffer();

        try{
            BufferedReader inputReader=new BufferedReader(new InputStreamReader(activity.openFileInput("a"+mListData.get(position))));
            String input;
            while((input=inputReader.readLine())!=null){
                stringBuffer.append(input);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int m=Integer.parseInt(stringBuffer.toString());

        StringBuffer stringBuffer1=new StringBuffer();
        try{
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(activity.openFileInput("m"+mListData.get(position))));
            String input;
            while((input=inputReader.readLine())!=null){
                stringBuffer1.append(input);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n= Integer.parseInt(stringBuffer1.toString());
        m=m-i;
        n=n-j;
        saveState("a"+mListData.get(position),String.valueOf(m),activity, Context.MODE_PRIVATE);
        saveState("m"+mListData.get(position),String.valueOf(n),activity, Context.MODE_PRIVATE);
        mListData.set(position,"");
        notifyDataSetChanged();
        saveState(String.valueOf(num) + String.valueOf(position), "", activity, Context.MODE_PRIVATE);

    }
    public void removeAttended(int position){
        attend.set(position,"");
        notifyDataSetChanged();
        saveState("a" + String.valueOf(num) + String.valueOf(position), "", activity, Context.MODE_PRIVATE);
    }
    public void removeMissed(int position){
        miss.set(position,"");
        notifyDataSetChanged();
        saveState("m" + String.valueOf(num) + String.valueOf(position), "", activity, Context.MODE_PRIVATE);
    }

    public void setdata(){
        attend.add("");
        miss.add("");
        mListData.add("");
        notifyDataSetChanged();
    }

    public void addAttended(String item,int position){
        attend.set(position,item);
        notifyDataSetChanged();
    }
    public void addMissed(String item,int position){
        miss.set(position,item);
        notifyDataSetChanged();
    }
    public void addItem(String item,int position) {

        mListData.add(position,item);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return sNum;
    }
    public static class Holder extends RecyclerView.ViewHolder {
        TextView number;
        TextView sub_name;
        CardView cardView;
        TextView status;
        Button attended;
        Button missed;
        public Holder(View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.subjectCard);
            number = (TextView) itemView.findViewById(R.id.textView4);
            sub_name = (TextView) itemView.findViewById(R.id.textView);
            attended = (Button) itemView.findViewById(R.id.button);
            missed = (Button) itemView.findViewById(R.id.button2);
            status = (TextView)itemView.findViewById(R.id.textView6);



        }
    }
}