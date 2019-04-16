package com.example.hp.studentattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.studentattendance.model.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Take_Attendance_Activity extends AppCompatActivity implements View.OnClickListener {
    AttendenceAdapter adapter;

    ArrayList<Student> students;
    RecyclerView recyclerView;
    Button submitbtn;
    RequestQueue requestQueue;
    JSONArray result;

    String date,branchId, semester,period, subId;

    private String studentURL= "http://192.168.43.24/android/student/getStudent.php";
    private String setAttendanceURL= "http://192.168.43.24/android/student/setAttendance.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take__attendance_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        getIntentData();
        requestStudent();
        adapter = new AttendenceAdapter(this,students);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        submitbtn.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getIntentData() {
        Intent intent=getIntent();
        branchId=intent.getStringExtra("branch_id");
        semester=intent.getStringExtra("sem");
        subId=intent.getStringExtra("subId");
        period=intent.getStringExtra("period");
        date=intent.getStringExtra("date");
        Toast.makeText(this, "bid="+branchId+" sem="+semester+" sub="+subId+" period="+period+" date="+date, Toast.LENGTH_SHORT).show();
    }

    private void initView() {

        students = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.student_attendence);
        submitbtn = (Button) findViewById(R.id.submitBtn);
    }
public void requestStudent()
{
        try {
            StringRequest request = new StringRequest(Request.Method.POST, studentURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                Log.d("responseval",response.toString());
                    try {
                        //Parsing the fetched Json String to JSON Object
                        JSONObject j = new JSONObject(response);

                        //Storing the Array of JSON String to our JSON Array
                        result = j.getJSONArray("students");

                        Log.d("resultval",result.getString(0));
                        getStudents(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("error", error.toString());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("branch_id", branchId);
                    parameters.put("semester", semester);
                    return parameters;
                }
            };
            requestQueue.add(request);
            // rst.setText("Saved");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error :: " + e.toString(), Toast.LENGTH_SHORT).show();

            Log.e("error", e.toString());
        }
    }
    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                //final String bnae = "username";
                Student student=new Student();
                student.setId(json.getInt("id"));
                student.setRollno(json.getString("rollno"));
                student.setS_name(json.getString("name"));

                students.add(student);
                Log.i("Dataval", student.getS_name().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("studentsval",students.toString());
        adapter.notifyDataSetChanged();

        //Setting adapter to show the items in the spinner


    }


    @Override
    public void onClick(View v) {
        for (int i=0;i<students.size();i++) {
            if (students.get(i).isCheck())
                setStudentAttendence(students.get(i).getId()+"","1",subId,period,date,"P");
            else
                setStudentAttendence(students.get(i).getId()+"","1",subId,period,date,"A");
        }
    }

    private void setStudentAttendence(final String studentId, final String teachersId, final String subjectId, final String period, final String date, final String attendance) {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, setAttendanceURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("responseval",response);
//                    try {
//                        //Parsing the fetched Json String to JSON Object
//                        JSONObject j = new JSONObject(response);
//
//                        //Storing the Array of JSON String to our JSON Array
//                        result = j.getJSONArray("students");
//
//                        Log.d("resultval",result.getString(0));
//                        getStudents(result);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("error", error.toString());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("student_id", studentId);
                    parameters.put("teachers_id", teachersId);
                    parameters.put("subject_id", subjectId);
                    parameters.put("date", date);
                    parameters.put("periods", period);
                    parameters.put("attendance", attendance);
                    return parameters;
                }
            };
            requestQueue.add(request);
            // rst.setText("Saved");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error :: " + e.toString(), Toast.LENGTH_SHORT).show();

            Log.e("error", e.toString());
        }
    }
}
