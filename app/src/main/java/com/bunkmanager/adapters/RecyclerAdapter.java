package com.bunkmanager.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bunkmanager.MainActivity;
import com.bunkmanager.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Pavan on 24/04/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder>{
    private ArrayList<String> mListData = new ArrayList<>();
    private ArrayList<String> attend = new ArrayList<>();
    private ArrayList<String> miss = new ArrayList<>();
    private ArrayList<String> percent=new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    public Activity activity;
    public static int num;
    public RecyclerAdapter(Activity act){
        this.activity=act;
        mLayoutInflater=LayoutInflater.from(act);
    }
    public RecyclerAdapter(Activity act, int number)
    {
        num=number;
        this.activity=act;
        mLayoutInflater=LayoutInflater.from(act);
    }

    @Override
    public RecyclerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View row = mLayoutInflater.inflate(R.layout.recycler_layout, parent, false);
            Holder holder = new Holder(row);
            return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerAdapter.Holder holder, final int position) {
        final String data = mListData.get(position);
    final String per = percent.get(position);
    if(attend.size()>position) {
        String mAttend = attend.get(position);
        holder.attended.setText(mAttend);
    }
    else
    {
        attend.add(position,"0");
    }
    if(miss.size()>position){
        String mMiss = miss.get(position);
        holder.missed.setText(mMiss);
    }
    else
    {
        miss.add(position,"0");
    }

    final int i = Integer.parseInt(holder.attended.getText().toString());
    final int j = Integer.parseInt(holder.missed.getText().toString());
    holder.sub_name.setText(data);
            int t =i+j;
            float p =Float.parseFloat(per);
            if(t!=0){
                if(p>99) {
                  if(j>0){
                      holder.detail.setText("Defaulter forever!!");
                  } else{
                      holder.detail.setText("Dare not bunk a single time!");
                  }
                } else {
                    int x = (int) (((100 / p) * i) - i);
                    int y = (int) Math.ceil((100 / (100 - p) * j) - j);
                    if (x - j < 0) {
                        holder.detail.setText("Attend atleast " + String.valueOf(y - i) + " to be safe");

                    } else {
                        holder.detail.setText("Bunks available : " + String.valueOf(x - j));
                    }
                }

            } else{
                holder.detail.setText("");
            }


    holder.status.setText(getStatus(i,j,Integer.parseInt(per)));
    holder.status.setTextColor(getColor(i, j, Integer.parseInt(per)));
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            ArrayList<String> hour = new ArrayList<String>();
            @Override
            public boolean onLongClick(View v) {
                final int hours;
                AlertDialog.Builder add=new AlertDialog.Builder(activity);
                final View layout =mLayoutInflater.inflate(R.layout.add_subject,null);
                add.setView(layout);
                final EditText sub = (EditText) layout.findViewById(R.id.editText);
                final EditText perc = (EditText) layout.findViewById(R.id.editText2);
                Toolbar toolbar =(Toolbar)layout.findViewById(R.id.view2);
                toolbar.setTitle("Edit Subject");
                sub.setText(data);
                perc.setText(per);
                add.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(activity,"save.",Toast.LENGTH_SHORT).show();
                        String subject = sub.getText().toString();
                        String Percent = perc.getText().toString();
                        if (subject.equals("")) {
                            Toast.makeText(activity, "Empty Subject Inputs", Toast.LENGTH_SHORT).show();
                        } else if (percent.equals("")) {
                            Toast.makeText(activity, "Empty Percent Inputs", Toast.LENGTH_SHORT).show();
                        } else if (MainActivity.isNotNumeric(Percent)) {
                            Toast.makeText(activity, percent + "is not a number", Toast.LENGTH_LONG).show();
                        } else if (Integer.parseInt(Percent) > 100 || Integer.parseInt(Percent) < 0) {
                            Toast.makeText(activity, "invalid percent limit", Toast.LENGTH_SHORT).show();
                        } else {
                            delete(mListData.get(position));
                            String att = new String();
                            String miss = new String();
                            try {
                                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                                        activity.openFileInput("a" + mListData.get(position))));
                                String input;
                                while ((input = inputReader.readLine()) != null) {
                                    att=input;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                                        activity.openFileInput("m" + mListData.get(position))));
                                String input;
                                while ((input = inputReader.readLine()) != null) {
                                    miss=input;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            delete("a" + mListData.get(position));
                            delete("m" + mListData.get(position));
                            StringBuffer stringBuffer1 = new StringBuffer();
                            for (int i = 1; i <= 6; i++) {
                                try {
                                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                                            activity.openFileInput("hours" + String.valueOf(i))));
                                    String input;
                                    while ((input = inputReader.readLine()) != null) {
                                        hour.add(input);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (int i = 2; i < 8; i++) {
                                for (int j = 0; j < Integer.parseInt(hour.get(i - 2)); j++) {
                                    try {
                                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(
                                                activity.openFileInput(String.valueOf(i) + String.valueOf(j))));
                                        String input;
                                        while ((input = inputReader.readLine()) != null) {
                                            if (input.equals(mListData.get(position))) {
                                                save(String.valueOf(i) + String.valueOf(j), subject, activity.MODE_PRIVATE);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    /*save(String.valueOf(i) + String.valueOf(j), "", MODE_PRIVATE);
                                    save("a" + String.valueOf(i) + String.valueOf(j), "", MODE_PRIVATE);
                                    save("m" + String.valueOf(i) + String.valueOf(j), "", MODE_PRIVATE);*/
                                }
                            }
                            mListData.set(position, subject);
                            percent.set(position, Percent);
                            save(subject,subject+"~"+Percent+"`",activity.MODE_PRIVATE);
                            save("Subjects","",activity.MODE_PRIVATE);
                            for (int z = 0; z < mListData.size(); z++) {
                                save("Subjects",mListData.get(z)+"~",activity.MODE_APPEND);
                            }
                            save("a"+subject,att,activity.MODE_PRIVATE);
                            save("m"+subject,miss,activity.MODE_PRIVATE);
                            notifyItemChanged(position);
                        }
                    }
                });
                add.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                add.show();
                return true;
            }
        });

}



    public void addPercent(String item) {
                percent.add(item);
    }
      public int getColor(int a, int b,int l) {
          int t = a + b;
          if (t != 0) {
              float p = (float)(a)/(float)(t) * 100;
              if(p>=l+5&&p<=100)
                  return (Color.parseColor("#00C853"));
              else if(p>=l&&p<l+5)
                  return(Color.parseColor("#FF6D00")) ;
              else
                  return(Color.parseColor("#D50000")) ;
          } else
              return(Color.parseColor("#607D8B")) ;

      }

       public String getStatus(int a, int b,int l){
        int t = a + b;
        if (t != 0) {
            float p = (float)(a)/(float)(t) * 100;
            if(p>=l+5&&p<=100)
                return("Safe-"+String.format("%.2f",p)+"%");
            else if(p>=l&&p<l+5)
                return("Careful-"+String.format("%.2f",p)+"%");
            else
                return("Defaulter-"+String.format("%.2f",p)+"%");
        } else
            return("Start with lectures");
    }
    public void addAttended(String item){
        attend.add(item);
    }
    public void addMissed(String item){
        miss.add(item);
    }
    public void addItem(String item) {
                mListData.add(item);
        notifyItemInserted(mListData.size()-1);
    }
    @Override
    public int getItemCount() {
        return mListData.size();
    }
    public static class Holder extends RecyclerView.ViewHolder {

        TextView sub_name;
        TextView detail;
        TextView status;
        Button attended;
        Button missed;
        CardView cardView;
        public Holder(View itemView) {
            super(itemView);
                cardView = (CardView)itemView.findViewById(R.id.cardViewMain);
                sub_name = (TextView) itemView.findViewById(R.id.textView);
                attended = (Button) itemView.findViewById(R.id.button);
                detail=(TextView)itemView.findViewById(R.id.textView14);
                missed = (Button) itemView.findViewById(R.id.button2);
                status = (TextView) itemView.findViewById(R.id.textView2);


        }
    }
    public void delete(String filename){
        File dir = activity.getFilesDir();
        File file = new File(dir, filename);
        boolean deleted=file.delete();
    }

    public  void save(String file, String data, int mode){
        FileOutputStream fos;
        try{
            fos=activity.openFileOutput(file, mode);
            fos.write(data.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
