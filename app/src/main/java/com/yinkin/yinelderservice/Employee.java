package com.yinkin.yinelderservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yinkin.yinelderservice.ui.main.EmployeeFragment;

public class Employee extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_activity);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(EmployerDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(EmployerDetailFragment.ARG_ITEM_ID));
            EmployeeFragment employeeFragment = new EmployeeFragment();
            employeeFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registerContainer, employeeFragment)
                    .commitNow();
        }
    }
}