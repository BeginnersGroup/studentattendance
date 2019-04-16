package com.example.hp.studentattendance;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.hp.studentattendance.R.id.branch1;
import static com.example.hp.studentattendance.R.id.semester;


public class Add_Student_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    /////insert
    Button save;
    EditText rollno, sname, fname,dob, semester,  mobno, email;
    RequestQueue requestQueue;
    TextView rst,branch2;
    private int pos;

    ////show branch
    JSONArray result;
    Spinner branch;
    private ArrayList<String> students;
    private String branchURL= "http://192.168.43.125/android/student/getBranch.php";
    private  String insertUrl="http://192.168.43.125/android/student/add_student.php";
    //private String semesterURL = "http://192.168.43.125/android/student/getSemester.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__student_);

        rollno =(EditText) findViewById(R.id.rollno) ;
        rst = (TextView) findViewById(R.id.rst);
        sname = (EditText )findViewById(R.id.sname);
        fname = (EditText) findViewById(R.id.fname);
        dob = (EditText) findViewById(R.id.dob);
        semester = (EditText) findViewById(R.id.semester);

        mobno = (EditText) findViewById(R.id.mobno);
        email = (EditText) findViewById(R.id.email);
        save = (Button)findViewById(R.id.submit);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        students = new ArrayList<String>();
        branch = (Spinner) findViewById(branch1);

        branch.setOnItemSelectedListener(this);

        getData();


        ////////////insert
//
//

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StringRequest request = new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("responseval2",response.toString());
                            System.out.println(response.toString());
                           // Toast.makeText(getApplicationContext(),"success")
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("error",error.toString());
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("rollno", rollno.getText().toString());
                            parameters.put("name", sname.getText().toString());
                            parameters.put("fname", fname.getText().toString());
                            parameters.put("dob", dob.getText().toString());
                            parameters.put("semester", semester.getText().toString());
                            parameters.put("m_no", mobno.getText().toString());
                            parameters.put("email", email.getText().toString());
                            parameters.put("branch_id", String.valueOf(pos));
                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                    rst.setText("Saved");
                } catch(Exception e){
                    Toast.makeText(getApplicationContext(), "error :: "+e.toString(), Toast.LENGTH_SHORT).show();

                    Log.e("error", e.toString());
                }


            }


        });
        ////////end insert



    }
    private void getData() {

        //Creating a string request
        StringRequest stringRequest = new StringRequest(branchURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //Parsing the fetched Json String to JSON Object
                            Log.d("responsevals",response);
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


    }

    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                //final String bnae = "username";
                students.add(json.getString("bname"));
                Log.i("Dataval", students.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        branch.setAdapter(new ArrayAdapter<String>(Add_Student_Activity.this, android.R.layout.simple_spinner_dropdown_item, students));
    }

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Branch ",getName(position));
        pos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}




