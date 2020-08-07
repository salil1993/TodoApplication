package com.deftskill.todoapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.deftskill.todoapplication.Model.User;
import com.deftskill.todoapplication.R;
import com.deftskill.todoapplication.db.TaskDbHelper;

public class RegisterActivity extends AppCompatActivity {
     EditText email,password,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        name=findViewById(R.id.name);
    }

    public void registerbtn(View view) {
        checkvalidation();
    }

    private void checkvalidation() {
        if (isError(name)){
            setError(name);
        }
        else if (isError(email)) {
            setError(email);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid Email.");
            email.requestFocus();
        } else if (isError(password)) {
            setError(password);
        }else{
            TaskDbHelper db =new TaskDbHelper(RegisterActivity.this);
            db.addUser(new User(name.getText().toString(),email.getText().toString(),password.getText().toString()));
            //Toast.makeText(this, "Registration done successfully!!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isError(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());

    }

    public void setError(EditText editText) {
        editText.setError("Field is required.");
        editText.requestFocus();
    }

    public void loginpage(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}