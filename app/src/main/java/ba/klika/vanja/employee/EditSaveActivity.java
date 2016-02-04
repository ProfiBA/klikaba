package ba.klika.vanja.employee;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ba.klika.vanja.employee.data.EmployeeService;
import ba.klika.vanja.employee.model.Employee;
import ba.klika.vanja.employee.utils.NetworkService;

public class EditSaveActivity extends AppCompatActivity {

    // Private
    private Toolbar toolbar;
    private EditText txtFirstName;
    private EditText txtLastName;
    private Boolean EditMode;
    private Employee employee;

    public static final String TAG = MyApplication.class.getSimpleName();

    @Override
    protected void onStop()
    {
        super.onStop();

        txtFirstName.clearFocus();
        txtLastName.clearFocus();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_save);

        // Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Is in editing mode
        EditMode = false;

        // Setup Action Bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);

        // Determine parent caller of this Activity
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("classFrom").equals(DetailsActivity.class.toString()))
        {
            employee = (Employee) getIntent().getSerializableExtra("ba.klika.vanja.employee.model.ser");
            setTitle(getResources().getString(R.string.ac_edit)+" "+employee.getFirstName());

            Log.i(TAG,employee.toString());

            txtFirstName.setText(employee.getFirstName());
            txtLastName.setText(employee.getLastName());
            EditMode= true;
        }
        else if (bundle.getString("classFrom").equals(MainActivity.class.toString()))
        {
            setTitle(getResources().getString(R.string.ac_newEmployee));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editsave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_editsave)
        {
            if (NetworkService.isConnected(getApplicationContext()))
            {
                if (isEmpty(txtFirstName) || isEmpty(txtLastName))
                {
                    Toast.makeText(EditSaveActivity.this, getApplicationContext().getString(R.string.err_EmptyFields), Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (EditMode == true)
                {
                    // Editing existing employee
                    Log.i(TAG,"EDIT EMPLOYEE");
                    employee.setFirstName(txtFirstName.getText().toString());
                    employee.setLastName(txtLastName.getText().toString());
                    EmployeeService.EditEmployee(this,employee);
                    finish();
                }
                else
                {
                    Log.i(TAG,"NEW EMPLOYEE");
                    // Saving new employee
                    Employee toAdd = new Employee();
                    toAdd.setFirstName(txtFirstName.getText().toString());
                    toAdd.setLastName(txtLastName.getText().toString());
                    EmployeeService.AddNewEmployee(this,toAdd);
                    finish();

                }
                return true;
            }
            else
            {
                Toast.makeText(EditSaveActivity.this, getApplicationContext().getString(R.string.err_noConnection), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if (id == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
