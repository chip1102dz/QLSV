package com.example.roomdb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.example.roomdb.database.UserDataBase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    TextView tv_deleteAll;
    EditText edt_Name;
    EditText edt_Address;
    Button btn_AddUser;
    RadioButton rdb_Yes;
    RecyclerView recyclerView;
    User mUser;
    UserAdapter userAdapter;
    private List<User> mList;
    EditText edt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //anh xa view
        initUI();

        //set RCB
        mList = new ArrayList<>();
        recyclerView = findViewById(R.id.rcv);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        userAdapter = new UserAdapter(new UserAdapter.onButtonClickListener() {

            @Override
            public void onClickUpdateUser(User user) {
                clickUpdateUser(user);
            }

            @Override
            public void onClickDeleteUser(User user) {
                clickDeleteUser(user);
            }
        });
        recyclerView.setAdapter(userAdapter);
        clickDeleteAll();
        InsertUser();
        SearchUser();
        loadData();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void SearchUser() {
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    handleSearch();
                }
                return false;
            }
        });
    }

    private void handleSearch() {
        String strKeyWord = edt_search.getText().toString().trim();
        mList = new ArrayList<>();
        mList = UserDataBase.getInstance(this).userDAO().searchUser(strKeyWord);
        userAdapter.setData(mList);
    }

    private void clickDeleteAll(){
        tv_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xóa tất cả")
                        .setMessage("Bạn chắc chắn?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UserDataBase.getInstance(MainActivity.this).userDAO().DeleteAll();
                                loadData();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void clickDeleteUser(User user) {

        new AlertDialog.Builder(this)
                .setTitle("Confirm delete user")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserDataBase.getInstance(MainActivity.this).userDAO().DeleteUser(user);
                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clickUpdateUser(User user) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", user);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    public void InsertUser(){
        btn_AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_Name.getText().toString();
                String address = edt_Address.getText().toString();
                mUser = new User(name, address);
                if(rdb_Yes.isChecked()) {
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
                        Toast.makeText(MainActivity.this, "Hãy nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isUserExits(mUser)) {
                        Toast.makeText(MainActivity.this, "User này đã tồn tại !", Toast.LENGTH_SHORT).show();
                    } else {
                        UserDataBase.getInstance(MainActivity.this).userDAO().InsertUser(mUser);
                        edt_Name.setText("");
                        edt_Address.setText("");
                        loadData();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Hãy chọn Yes để thêm thông tin !",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadData(){
        mList = UserDataBase.getInstance(this).userDAO().getListUser();
        userAdapter.setData(mList);
    }

    private void initUI() {
        edt_Name = findViewById(R.id.edt_name);
        edt_Address = findViewById(R.id.edt_adress);
        btn_AddUser = findViewById(R.id.btn_adduser);
        rdb_Yes = findViewById(R.id.btn_yes);
        tv_deleteAll = findViewById(R.id.tv_deleteAll);
        edt_search = findViewById(R.id.edt_search);
    }
    private boolean isUserExits(User user){
        List<User> list = UserDataBase.getInstance(this).userDAO().checkUser(user.getName());
        return list != null && !list.isEmpty();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE && resultCode == MainActivity.RESULT_OK){
            loadData();
        }
    }
}