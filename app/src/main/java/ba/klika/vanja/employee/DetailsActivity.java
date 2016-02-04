package ba.klika.vanja.employee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import ba.klika.vanja.employee.data.EmployeeService;
import ba.klika.vanja.employee.model.Employee;
import ba.klika.vanja.employee.utils.NetworkService;

/**
 * Created by vanja on 03.02.2016..
 */
public class DetailsActivity extends AppCompatActivity
{
    // Private
    private Toolbar toolbar;
    private Employee mPerson;
    private TextView dtvFirstName;
    private TextView dtvLastName;

    public static final String TAG = DetailsActivity.class.getSimpleName();

    /* NOTE

    This Activity can be implemented in different ways using Fragements and Details can be shown by passing just ID and then querying over rest to get additional details.
    For sample usage this activity is implemented using serialization and passing object to intent.

     */

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"on Create");
        if (android.os.Build.VERSION.SDK_INT >= 21)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        setContentView(R.layout.content_employee_details);
        // Getting object model from Main Activiy
        mPerson = (Employee) getIntent().getSerializableExtra("ba.klika.vanja.employee.model.ser");

        // This can be set to textview also instead querying rest service

        // Setting up text fields
        dtvFirstName = (TextView) findViewById(R.id.dtvFirstName);
        dtvLastName = (TextView) findViewById(R.id.dtvLastName);

        // Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Setup Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        GetEmployeeDetails();
    }

    @Override
    public  void onResume()
    {
        super.onResume();
        Log.i(TAG,"on Resume");
        GetEmployeeDetails();
    }

    public void GetEmployeeDetails()
    {
        // Get employee from service (REST)
        EmployeeService.LoadDataDetails(getApplicationContext(),mPerson.getId(),dtvFirstName,dtvLastName,mPerson);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit)
        {
            if (NetworkService.isConnected(getApplicationContext()))
            {
                Intent i = new Intent(DetailsActivity.this, EditSaveActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("ba.klika.vanja.employee.model.ser",mPerson);
                i.putExtras(mBundle);
                i.putExtra("classFrom", DetailsActivity.class.toString());
                startActivity(i);
                return false;
            }
            else
            {
               Toast.makeText(DetailsActivity.this, getApplicationContext().getString(R.string.err_noConnection), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        if (id == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
