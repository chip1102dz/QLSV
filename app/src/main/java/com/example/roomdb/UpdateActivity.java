package com.example.roomdb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomdb.database.UserDataBase;

import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    EditText edt_Name;
    EditText edt_Address;
    Button btn_UpdateUser;
    RadioButton rdb_Yes;
    private User mUser;
    private List<User> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);

        initUI();
        mUser = (User) getIntent().getExtras().get("object_user");
        if(mUser != null){
            edt_Name.setText(mUser.getName());
            edt_Address.setText(mUser.getAddress());
        }

        btn_UpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void updateUser() {
        String name = edt_Name.getText().toString();
        String address = edt_Address.getText().toString();

        if(TextUtils.isEmpty(name)||TextUtils.isEmpty(address)){
            Toast.makeText(this,"Hãy nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        } else if (rdb_Yes.isChecked()) {
            mUser.setName(name);
            mUser.setAddress(address);

            UserDataBase.getInstance(this).userDAO().UpdateUser(mUser);
            Toast.makeText(this, "Upate User Thành Công !", Toast.LENGTH_SHORT).show();

            Intent intentResult = new Intent();
            setResult(Activity.RESULT_OK, intentResult);
            finish();
        }
    }

    private void initUI() {
        edt_Name = findViewById(R.id.edt_name);
        edt_Address = findViewById(R.id.edt_adress);
        btn_UpdateUser = findViewById(R.id.btn_updateuser);
        rdb_Yes = findViewById(R.id.btn_yes);
    }
}