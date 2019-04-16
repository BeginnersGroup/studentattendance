package com.example.hp.studentattendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.hp.studentattendance.model.Attendence;
import com.example.hp.studentattendance.model.Student;

import java.util.ArrayList;

public class ViewAttendenceAdapter extends RecyclerView.Adapter<ViewAttendenceAdapter.ViewAttendenceHolder> {

    Context context;
    ArrayList<Attendence> attendences;

    public ViewAttendenceAdapter(Context context, ArrayList<Attendence> attendences)  {
        this.context = context;
        this.attendences = attendences;

    }

    @Override
    public ViewAttendenceAdapter.ViewAttendenceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.student_attendence_list,parent,false);

        return new ViewAttendenceAdapter.ViewAttendenceHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewAttendenceAdapter.ViewAttendenceHolder holder, final int position) {

        holder.srno.setText(position+1+"");
        holder.sName.setText(attendences.get(position).getStudentName());
        holder.rollNo.setText(attendences.get(position).getRollNo());
        holder.attendance.setText(attendences.get(position).getAttendance());

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
        return attendences.size();
    }


    public class ViewAttendenceHolder extends RecyclerView.ViewHolder {
        TextView srno, sName, rollNo, attendance;
        public ViewAttendenceHolder(View itemView) {
            super(itemView);

            srno = (TextView) itemView.findViewById(R.id.view_srNo);
            sName = (TextView) itemView.findViewById(R.id.view_s_name);
            rollNo = (TextView) itemView.findViewById(R.id.view_r_no);
            attendance = (TextView) itemView.findViewById(R.id.view_attendence);
        }

    }
}
