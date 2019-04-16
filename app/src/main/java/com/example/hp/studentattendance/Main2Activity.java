package com.example.hp.studentattendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.studentattendance.model.Branch;
import com.example.hp.studentattendance.model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.hp.studentattendance.R.id.branch1;
import static com.example.hp.studentattendance.R.id.period1;
import static com.example.hp.studentattendance.R.id.semester1;
import static com.example.hp.studentattendance.R.id.subject1;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    TextView select,branch, semester,or, period,rst;
    EditText date;
    Button take,view;
    Spinner sbranch, ssemester, speriod, ssubject;
    RequestQueue requestQueue;
    JSONArray result, result2, result3;

    private int pos;
    ArrayAdapter adapter, adapter2;

    private ArrayList<Branch> students;
    private ArrayList<String> subVal, branchVal;
    private ArrayList<Subject> subjects;
    private String branchURL= "http://192.168.43.24/android/student/getBranch.php";
    private String subjectURL= "http://192.168.43.24/android/student/getSubject.php";

    private String datePeriodUPL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sbranch = (Spinner)findViewById(branch1);
        // ssemester = (Spinner)findViewById(semester1);
        ssubject = (Spinner) findViewById(subject1);
        ssemester = (Spinner) findViewById(semester1);
        speriod = (Spinner)findViewById(period1);

        date = (EditText)findViewById(R.id.date1);
        take = (Button)findViewById(R.id.take);
        view = (Button)findViewById(R.id.view);


        students = new ArrayList<Branch>();
        subjects = new ArrayList<Subject>();
        subVal=new ArrayList<>();
//        subVal.add("select subject");

//        progressBar=new ProgressBar();
        branchVal=new ArrayList<>();
//        branchVal.add("select branch");
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        sbranch.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ssemester.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ssubject.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        getSubject();
        getData();

//        setData();
        //change();


// Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                R.array.Semester, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        ssemester.setAdapter(adapter);

        ////////// PERIOPD

//        speriod = (Spinner) findViewById(R.id.period1);
// Create an ArrayAdapter using the string array and a default spinner layout
        adapter2 = ArrayAdapter.createFromResource(this,
                R.array.Period, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        speriod.setAdapter(adapter2);



        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String branch=sbranch.getSelectedItem().toString();
                try {
                    String bid = students.get(sbranch.getSelectedItemPosition()).getId() + "";
                    String sem = ssemester.getSelectedItem().toString();
                    String dateval = date.getText().toString();
                    String periodval = speriod.getSelectedItem().toString();
                    String subId = students.get(ssubject.getSelectedItemPosition()).getId() + "";

                    Toast.makeText(Main2Activity.this, "dateval="+dateval, Toast.LENGTH_SHORT).show();
                    if (!bid.isEmpty() && !sem.isEmpty() && !dateval.isEmpty() && !periodval.isEmpty() && !subId.isEmpty()) {

                        Intent intent = new Intent(Main2Activity.this, Take_Attendance_Activity.class);
                        intent.putExtra("branch_id", bid);
                        intent.putExtra("sem", sem);
                        intent.putExtra("subId",subId);
                        intent.putExtra("period",periodval);
                        intent.putExtra("date",dateval);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Main2Activity.this, "one or more field is empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(Main2Activity.this, "one or more field is empty", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(View_Attendance_Activity.this, "bid2="+bid, Toast.LENGTH_SHORT).show();
//                String sem=ssemester.getSelectedItem().toString();



            }


        });
        ////////end insert

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String dateval = date.getText().toString();
                    String subId = students.get(ssubject.getSelectedItemPosition()).getId() + "";

                    Toast.makeText(Main2Activity.this, "dateval="+dateval, Toast.LENGTH_SHORT).show();
                    if (!dateval.isEmpty() && !subId.isEmpty()) {
                        Intent intent = new Intent(Main2Activity.this, View_Attendance_Activity.class);
                        intent.putExtra("subId",subId);
                        intent.putExtra("date",dateval);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Main2Activity.this, "one or more field is empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    Toast.makeText(Main2Activity.this, "one or more field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getData() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(branchURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("branch");

                            getStudents(result);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
//        return result;

    }

    private void getSubject() {
        //Creating a string request
//        Toast.makeText(this, "getsub", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(subjectURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(View_Attendance_Activity.this, "getsub2", Toast.LENGTH_SHORT).show();

                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result2 = j.getJSONArray("subject");

                            getSubjects(result2);
                            Log.d("result2","hello");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Main2Activity.this, "error:"+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    private void getSubjects(JSONArray j) {
//        Toast.makeText(this, "getsubjects", Toast.LENGTH_SHORT).show();
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                //final String bnae = "username"
                Subject sub=new Subject();
                sub.setSubName(json.getString("subname"));
                sub.setSemster(json.getInt("semester"));
                sub.setBranchId(json.getInt("branch_id"));
                subjects.add(sub);
//                Subj.add(json.getString("subject"));
                Log.i("Data2", "hello");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "error:"+e, Toast.LENGTH_SHORT).show();
            }
        }

        //Setting adapter to show the items in the spinner
//        ssubject.setAdapter(new ArrayAdapter<String>(View_Attendance_Activity.this, android.R.layout.simple_spinner_dropdown_item, subVal));
    }


    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                //final String bnae = "username";
                Branch branch=new Branch();
                branch.setId(json.getInt("id"));
                branch.setbName(json.getString("bname"));
                students.add(branch);
                branchVal.add(branch.getbName());
                Log.i("Data", "id="+branch.getId()+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Setting adapter to show the items in the spinner
        sbranch.setAdapter(new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_spinner_dropdown_item, branchVal));
//        ssubject.setAdapter(new ArrayAdapter<String>(View_Attendance_Activity.this, android.R.layout.simple_spinner_dropdown_item,sub3));
    }

    /*private void getSubject(JSONArray j){
        //Traversing through all the items in the json array
        Subject sub;

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                //final String bnae = "username";
//                students.add(json.getString("subject"));
                String sub2 =json.getString("subname");
                int sem = json.getInt("semester");
                sub = new Subject(sub2, sem);
//                Log.i("Data", students.toString());
                subjects.add(sub);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
    //Setting adapter to show the items in the spinner
//        sbranch.setAdapter(new ArrayAdapter<String>(View_Attendance_Activity.this, android.R.layout.simple_spinner_dropdown_item, students));


    private String getName(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString("bname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;

    }
    ////////////close branch

    private String getSem(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString("bname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Log.i("Branch ",getName(position));
//        pos = position;


//        Log.d("subjects",subjects.get(0).getSubName());
        if(parent.getId()== R.id.branch1){
            pos=position;
            subVal.clear();
            for (int i=0;i<subjects.size();i++) {
//                Log.d("selecteddata","sem="+subjects.get(i).getSemster()+ "selsem="+selectedSem+" selbid="+
// selectedBranchId+" bid="+subjects.get(i).getBranchId());
                int selectedSem=Integer.parseInt(ssemester.getSelectedItem().toString());
                int selectedBranchId=students.get(sbranch.getSelectedItemPosition()).getId();

                if (subjects.get(i).getSemster()==selectedSem &&
                        subjects.get(i).getBranchId()==selectedBranchId)
                    subVal.add(subjects.get(i).getSubName());
                Log.d("spinner",pos+" selsem="+selectedSem+" selbranch="+selectedBranchId);
            }
           /* branchId=students.get(pos).getId();
            Log.i("Branch ","id="+branchId);
            Log.d("studentsval",students.get(0).getId()+" "+students.get(3).getId());*/
//            pos = position;

        }else if(parent.getId() == R.id.semester1){
            subVal.clear();
//            Log.d("studentsval","id="+subjects.get(0));

            for (int i=0;i<subjects.size();i++) {
//                Log.d("selecteddata","sem="+subjects.get(i).getSemster()+ "selsem="+selectedSem+" selbid="+
// selectedBranchId+" bid="+subjects.get(i).getBranchId());
                int selectedSem=Integer.parseInt(ssemester.getSelectedItem().toString());
                int selectedBranchId=students.get(sbranch.getSelectedItemPosition()).getId();

                if (subjects.get(i).getSemster()==selectedSem &&
                        subjects.get(i).getBranchId()==selectedBranchId)
                    subVal.add(subjects.get(i).getSubName());
//                Log.d("spinner",pos+" selsem="+selectedSem+" selbranch="+selectedBranchId);
            }

            ssubject.setAdapter(new ArrayAdapter<String>(Main2Activity.this,
                    android.R.layout.simple_spinner_dropdown_item, subVal));

            Log.d("semesterval","sem="+parent.getSelectedItem());

        }else if(parent.getId() == R.id.subject1) {

        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        if(parent.getId()== R.id.branch1){

        }else if(parent.getId() == R.id.semester1){


        }else if(parent.getId() == R.id.subject1) {

        }
    }

}


