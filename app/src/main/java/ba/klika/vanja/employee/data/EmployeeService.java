package ba.klika.vanja.employee.data;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ba.klika.vanja.employee.MyApplication;
import ba.klika.vanja.employee.R;
import ba.klika.vanja.employee.model.Employee;
import ba.klika.vanja.employee.utils.Contants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by vanja on 03.02.2016..
 */
public class EmployeeService
{
    // Private
    private static EmployeeAdapter employeeArrayAdapter;

    public static void ToogleDelete(Boolean mode)
    {
        if (employeeArrayAdapter != null)
         employeeArrayAdapter.Toogle(mode);
    }

    // Load list of employees from REST to listview (async)
    public static void LoadData(final Context ct, final ListView listView,final SwipeRefreshLayout swipeRefreshLayout)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Contants.REST_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Call<ArrayList<Employee>> call = service.loadEmployees();

        call.enqueue(new Callback<ArrayList<Employee>>() {
            @Override
            public void onResponse(Response<ArrayList<Employee>> response) {
               try {
                   if (response.isSuccess())
                   {
                       // stopping swipe refresh
                       swipeRefreshLayout.setRefreshing(false);
                       employeeArrayAdapter = new EmployeeAdapter(ct, response.body());
                       listView.setAdapter(employeeArrayAdapter);

                       // Drop local DB to prevent duplicate records
                       Log.d("Employee Service","Deleting records");
                       cupboard().withDatabase(MyApplication.getInstance().db).delete(Employee.class, null);
                       Log.d("Employee Service","Adding new records");
                       cupboard().withDatabase(MyApplication.getInstance().db).put(response.body());
                       Toast.makeText(ct.getApplicationContext(), ct.getString(R.string.msg_loadedFromRemote), Toast.LENGTH_SHORT).show();
                   } else
                   {
                       swipeRefreshLayout.setRefreshing(false);
                       Toast.makeText(ct, ct.getText(R.string.err_FailRequest), Toast.LENGTH_SHORT).show();
                   }
               }catch (Exception e)
               {
                   swipeRefreshLayout.setRefreshing(false);
                   Toast.makeText(ct, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
               }
            }
            @Override
            public void onFailure(Throwable t)
            {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ct, ct.getText(R.string.err_RestNotResponding), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Load employee details to detailsview (sync)
    public static void LoadDataDetails(final Context ct, int Id, final TextView firstName, final TextView lastName, final Employee employee)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Contants.REST_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);

        Call<Employee> call = service.employeeDetails(Id);
        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Response<Employee> response) {
                try {
                    if (response.isSuccess())
                    {
                        firstName.setText(response.body().getFirstName());
                        lastName.setText(response.body().getLastName());
                        employee.setFirstName(response.body().getFirstName());
                        employee.setLastName(response.body().getLastName());

                     } else
                    {
                        firstName.setText(ct.getResources().getString(R.string.err_FailFetch));
                        lastName.setText(ct.getResources().getString(R.string.err_FailFetch));
                    }
                }catch (Exception e)
                {
                    firstName.setText(ct.getResources().getString(R.string.err_FailFetch));
                    lastName.setText(ct.getResources().getString(R.string.err_FailFetch));
                }
            }
            @Override
            public void onFailure(Throwable t)
            {
                Toast.makeText(ct.getApplicationContext(), ct.getText(R.string.err_FailCreateEmployee), Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }

    public static void AddNewEmployee(final Context ct,Employee employee)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Contants.REST_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Call<Employee> call = service.createEmployee(employee);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Response<Employee> response) {
                try {
                    if (response.isSuccess())
                    {
                        Toast.makeText(ct.getApplicationContext(), ct.getResources().getString(R.string.msg_employeeCreated), Toast.LENGTH_SHORT).show();
                        employeeArrayAdapter.data.clear();
                        employeeArrayAdapter.notifyDataSetChanged();

                    } else
                    {
                        Toast.makeText(ct.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        Log.d("Error", response.body().toString());
                    }
                }catch (Exception e)
                {
                    Log.d("Error", e.getMessage());
                }
            }
            @Override
            public void onFailure(Throwable t)
            {
                Toast.makeText(ct.getApplicationContext(), ct.getText(R.string.err_FailCreateEmployee), Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }

    public static void EditEmployee(final Context ct,Employee employee)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Contants.REST_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Call<Employee> call = service.editEmployee(employee.getId(),employee);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Response<Employee> response) {
                try {
                    if (response.isSuccess())
                    {
                        Toast.makeText(ct.getApplicationContext(), ct.getResources().getString(R.string.msg_employeeEdited), Toast.LENGTH_SHORT).show();
                        employeeArrayAdapter.notifyDataSetChanged();
                    } else
                    {
                        Toast.makeText(ct.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        Log.d("Error", response.body().toString());
                    }
                }catch (Exception e)
                {
                    Log.d("Error", e.getMessage());
                }
            }
            @Override
            public void onFailure(Throwable t)
            {
                Toast.makeText(ct.getApplicationContext(), ct.getText(R.string.err_FailEditEmployee), Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }

    public static void DeleteEmployee(final Context ct, int employeeId, final ArrayList<Employee> data, final int position)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Contants.REST_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService service = retrofit.create(ApiService.class);
        Call<Employee> call = service.deleteEmployee(employeeId);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Response<Employee> response) {
                try {
                    if (response.isSuccess())
                    {
                        Toast.makeText(ct.getApplicationContext(), ct.getResources().getString(R.string.msg_employeeDeleted), Toast.LENGTH_SHORT).show();
                        employeeArrayAdapter.notifyDataSetChanged();
                        data.remove(position);

                    } else
                    {
                        Toast.makeText(ct.getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        Log.d("Error", response.body().toString());
                    }
                }catch (Exception e)
                {
                    Log.d("Error", e.getMessage());
                }
            }
            @Override
            public void onFailure(Throwable t)
            {
                Toast.makeText(ct.getApplicationContext(), ct.getText(R.string.err_FailDelete), Toast.LENGTH_SHORT).show();
                Log.d("Error", t.getLocalizedMessage());
            }
        });
    }


}
