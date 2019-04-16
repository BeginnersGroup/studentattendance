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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.studentattendance.model.Attendence;
import com.example.hp.studentattendance.model.Branch;
import com.example.hp.studentattendance.model.Student;
import com.example.hp.studentattendance.model.StudentAttendance;
import com.example.hp.studentattendance.model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class View_Attendance_Activity extends AppCompatActivity  {

    ViewAttendenceAdapter adapter;

    ArrayList<Attendence> attendences;
    ArrayList<Student> students;
    ArrayList<StudentAttendance> studentAttendances;
    RecyclerView recyclerView;
//    Button submitbtn;
    RequestQueue requestQueue;
    JSONArray result, result2;

//    JSONArray studentJsonArray;

    String date, subId;

    private String studentURL= "http://192.168.43.24/android/student/getStudent.php";
    private String attendenceURL= "http://192.168.43.24/android/student/getAttendance.php";

//    private String setAttendanceURL= "http://192.168.43.24/android/student/setAttendance.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__attendance_);

        attendences = new ArrayList<>();
        students = new ArrayList<>();
        studentAttendances = new ArrayList<>();

        recyclerView= (RecyclerView) findViewById(R.id.view_attendence_rv);

        adapter = new ViewAttendenceAdapter(this,attendences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        requestQueue = Volley.newRequestQueue(getApplicationContext());
//        requestStudent();
        getIntentData();
        getAttendance();


    }

    private void getIntentData() {
        Intent intent=getIntent();
        subId=intent.getStringExtra("subId");
        date=intent.getStringExtra("date");
    }

    public void getAttendance()
    {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, attendenceURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d("responseval",response.toString());
                    try {
                        //Parsing the fetched Json String to JSON Object
                        JSONObject j = new JSONObject(response);

                        //Storing the Array of JSON String to our JSON Array
                        result = j.getJSONArray("attendance");

//                        Log.d("resultval",result.getString(0));
                        viewAttendance(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "tarun:"+error.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("error", error.toString());
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    Log.d("datesubval",date+" sub="+subId);
                    parameters.put("date", date);
                    parameters.put("subject_id", subId);
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

    private void viewAttendance(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                //Adding the name of the student to array list
                //final String bnae = "username";
                Attendence attendence = new Attendence();
                attendence.setId(json.getInt("id"));
                attendence.setStudentId(json.getInt("student_id"));
                attendence.setAttendance(json.getString("attendance"));
                attendence.setStudentName(json.getString("name"));
                attendence.setRollNo(json.getString("rollno"));

                attendences.add(attendence);

                Log.i("Data", "id="+attendence.getId()+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerView.setAdapter(adapter);
       }

}



