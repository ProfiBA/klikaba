package ba.klika.vanja.employee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ba.klika.vanja.employee.data.EmployeeAdapter;
import ba.klika.vanja.employee.data.EmployeeService;
import ba.klika.vanja.employee.model.Employee;
import ba.klika.vanja.employee.utils.NetworkService;
import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    // Private
    private Toolbar toolbar;
    private ListView lvEmployees;
    private SwipeRefreshLayout swipeRefreshLayout;
    public boolean deleteMode;
    private ImageView btnDelete;

    public static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide title on ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Log.i(TAG,"OnCreate event");

        // ListView setup
        lvEmployees = (ListView)findViewById(R.id.lvEmployees);
        deleteMode = false;
        btnDelete = (ImageView)findViewById(R.id.imgDeleteIcon);

        // Refresh layout setup
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
             public void run()
            {
             swipeRefreshLayout.setRefreshing(true);
                fetchEmployees();
            }
          }
        );

        // ListView click event
        lvEmployees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                Intent i = new Intent(MainActivity.this, DetailsActivity.class);
                Employee emp = (Employee) parent.getItemAtPosition(position);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("ba.klika.vanja.employee.model.ser",emp);
                i.putExtras(mBundle);
                startActivity(i);
            }
        });
      }

    public void fetchEmployees()
    {
        deleteMode  = false;
        // Load anyway from local storage
        ArrayList<Employee> data;
        try {
            data = new ArrayList<>();
            // Iterate employees
            QueryResultIterable<Employee> itr = cupboard().withDatabase(MyApplication.getInstance().db).query(Employee.class).query();
            for (Employee employee : itr)
            {
            data.add(employee);
            Log.d(TAG,employee.getFirstName());
            }
        }finally { }
        EmployeeAdapter employeeArrayAdapter = new EmployeeAdapter(this, data);
        lvEmployees.setAdapter(employeeArrayAdapter);


        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);
        // Load data from REST service
        EmployeeService.LoadData(this, lvEmployees, swipeRefreshLayout);
    }

    @Override
    public  void onResume()
    {
        Log.i(TAG,"on Resume");
        super.onResume();
        deleteMode  = false;
        fetchEmployees();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (deleteMode  == false)
        {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        else if (deleteMode)
        {
            getMenuInflater().inflate(R.menu.menu_main_delete, menu);
        }
        else
            getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add)
        {
            if (NetworkService.isConnected(getApplicationContext()))
            {
                Intent i = new Intent(MainActivity.this, EditSaveActivity.class);
                i.putExtra("classFrom", MainActivity.class.toString());
                startActivity(i);
                return false;
            }
            else
            {
                Toast.makeText(MainActivity.this, getApplicationContext().getString(R.string.err_noConnection), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        if (id == R.id.action_delete)
        {
           deleteMode = true;
            EmployeeService.ToogleDelete(deleteMode);
            this.invalidateOptionsMenu();
        }
        if (id == R.id.action_cancel)
        {
            deleteMode = false;
            EmployeeService.ToogleDelete(deleteMode);
            this.invalidateOptionsMenu();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        fetchEmployees();
    }

    @Override
    public void onStart()
    {
         super.onStart();
        fetchEmployees();
    }
}
