package com.deftskill.todoapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.deftskill.todoapplication.R;
import com.deftskill.todoapplication.db.TaskDbHelper;
import com.deftskill.todoapplication.util.Constants;
import com.deftskill.todoapplication.util.PrefsManager;

public class LoginActivity extends AppCompatActivity {
    EditText edtemail,edtPassword;
    Button btnLogin;
    TaskDbHelper databaseHelper;
    boolean isLoggedin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PrefsManager.with(this).getBoolean(Constants.PREF_IS_LOGGED, false) == true) {
            isLoggedin = true;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            setContentView(R.layout.activity_login);
            isLoggedin = false;
            btnLogin = (Button) findViewById(R.id.login);
            edtemail = (EditText) findViewById(R.id.email);
            edtPassword = (EditText) findViewById(R.id.password);
            databaseHelper = new TaskDbHelper(LoginActivity.this);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkvalidation();
                }
            });
        }
    }

    private void checkvalidation() {
      if (isError(edtemail)) {
            setError(edtemail);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString()).matches()) {
          edtemail.setError("Invalid Email.");
          edtemail.requestFocus();
        } else if (isError(edtPassword)) {
            setError(edtPassword);
        }else{
          boolean isExist = databaseHelper.checkUserExist(edtemail.getText().toString(), edtPassword.getText().toString());
          if (isExist) {
              PrefsManager.with(LoginActivity.this).save(Constants.PREF_IS_LOGGED, true);
              Intent intent = new Intent(LoginActivity.this, MainActivity.class);
              intent.putExtra("email", edtemail.getText().toString());
              startActivity(intent);
              finish();
              Toast.makeText(LoginActivity.this, "Login successfully.", Toast.LENGTH_SHORT).show();
          } else {
              edtPassword.setText(null);
              edtemail.setText(null);
              Toast.makeText(LoginActivity.this, "Login failed. Invalid email or password.", Toast.LENGTH_SHORT).show();
          }
          
          
      }
    }

    public boolean isError(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());

    }

    public void setError(EditText editText) {
        editText.setError("Field is required.");
        editText.requestFocus();
    }

    public void registerpage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}