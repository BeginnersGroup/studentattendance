package com.example.hp.studentattendance;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private EditText userid;
    private EditText password;
    String userNameVal,passVal;
    RequestQueue requestQueue;
    String showUrl = "http://192.168.43.125/android/showStudents.php";
    String loginURL = "http://192.168.43.125/android/loginandy.php";
    //TextInputEditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        userid = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        login = (Button)findViewById(R.id.button);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getUserData();
//                validate();
               // login(v);
                change();
            }
        });
    }

    public void change(){

        Intent intent = new Intent(MainActivity.this,TeacherActivity.class);
        startActivity(intent);

    }

        public void login(View view) {
            System.out.println("ww");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    loginURL,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());
                    try {
                        JSONArray students = response.getJSONArray("user");
                        for (int i = 0; i < students.length(); i++) {
                            JSONObject student = students.getJSONObject(i);

                            Log.d("testdata",student+"");
                            String uname = student.getString("userid");
                            String pass = student.getString("password");

                            userNameVal= userid.getText().toString();
                            passVal = password.getText().toString();
                            //String email = student.getString("email");
                           // Toast.makeText(getApplication(),userNameVal+" "+passVal,Toast.LENGTH_LONG).show();
                            Log.i("user", userNameVal+" "+passVal );
                            try {
                                if ((userNameVal.equals(uname)) && (passVal.equals(pass))) {

                                    Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                                    Toast.makeText(getApplicationContext(), "Login Successed" +"", Toast.LENGTH_SHORT).show();

                                    startActivity(intent);
                                  //  finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "invalid username and password", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                Toast.makeText(getApplication(),"error : "+e.toString(),Toast.LENGTH_LONG).show();
                                Log.e("error",e.toString());
                            }
                        }
                        //aresult.append("===\n");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.append(error.getMessage());

                }
            });
            requestQueue.add(jsonObjectRequest);
        }

/*
    private void validate() {
        userNameVal= userid.getText().toString();
        passVal = password.getText().toString();
        if ((userNameVal.equals("Admin@")) && (passVal.equals("Admin@123"))){
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
    } else {
            Toast.makeText(getApplicationContext(), "invalid username and password", Toast.LENGTH_SHORT).show();
        }

    }*/

// private void getUserData() {
////     uname = (EditText) findViewById(R.id.editText);
////     password = (EditText) findViewById(R.id.editText2);
//
//     //Toast.makeText(this, "getuserdata", Toast.LENGTH_SHORT).show();
//     requestQueue = Volley.newRequestQueue(getApplicationContext());
//             System.out.println("ww");
//             JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                     showUrl,null, new Response.Listener<JSONObject>() {
//                 @Override
//                 public void onResponse(JSONObject response) {
//                     System.out.println(response.toString());
//                     try {
//                         JSONArray students = response.getJSONArray("user");
//                         for (int i = 0; i < students.length(); i++) {
//                             JSONObject student = students.getJSONObject(i);
//
//                             Log.d("tstata",student+"");
//                              userNameVal = student.getString("userid");
//                              passVal = student.getString("password");
//
//                             String userId, pass;
//                             userId = userid.getText().toString();
//                             pass = password.getText().toString();
//
//                             if ((userNameVal.equals(userId)) && (passVal.equals(pass))){
//                                 Intent intent = new Intent(MainActivity.this, selects.class);
//                                 Toast.makeText(getApplicationContext(),"Logged IN",Toast.LENGTH_LONG).show();
//
//                                 startActivity(intent);
//                             } else {
//                                 Toast.makeText(getApplicationContext(), "invalid username and password", Toast.LENGTH_SHORT).show();
//                             }
//                             Log.d("datatest",userNameVal+passVal);
////                             String email = student.getString("email");
//
////                             result.append( uname +" " + password + " " + email + " \n");
//                         }
////                         result.append("===\n");
//
//                     } catch (JSONException e) {
//                         e.printStackTrace();
//                     }
//
//                 }
//             }, new Response.ErrorListener() {
//                 @Override
//                 public void onErrorResponse(VolleyError error) {
//                     System.out.append(error.getMessage());
//
//                 }
//             });
//             requestQueue.add(jsonObjectRequest);
//
//
// }
}
