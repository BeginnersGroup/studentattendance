package com.example.hp.studentattendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.studentattendance.model.Student;

import java.util.ArrayList;

/**
 * Created by HP on 10/04/2019.
 */

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.AttendenceHolder> {

    Context context;
    ArrayList<Student> students;

    public AttendenceAdapter(Context context, ArrayList<Student> students)  {
        this.context = context;
        this.students = students;

    }

    @Override
    public AttendenceAdapter.AttendenceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.student_list,parent,false);

        return new AttendenceHolder(view);
    }

    @Override
    public void onBindViewHolder(final AttendenceAdapter.AttendenceHolder holder, final int position) {

        holder.srno.setText(position+1+"");
        holder.sName.setText(students.get(position).getS_name());
        holder.rollNo.setText(students.get(position).getRollno());
        holder.workingDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                students.get(position).setCheck(isChecked);
            }
        });
       /* holder.workingDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(context, "check="+holder.workingDay.isChecked(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    public class AttendenceHolder extends RecyclerView.ViewHolder {
        TextView srno, sName, rollNo;
        CheckBox workingDay;
        public AttendenceHolder(View itemView) {
            super(itemView);

            srno = (TextView) itemView.findViewById(R.id.srNo);
            sName = (TextView) itemView.findViewById(R.id.s_name);
            rollNo = (TextView) itemView.findViewById(R.id.r_no);
            workingDay = (CheckBox)itemView.findViewById(R.id.w_day);
        }

    }
}
