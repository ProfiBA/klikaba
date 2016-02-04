package ba.klika.vanja.employee.data;
import android.database.Observable;

import java.util.ArrayList;
import java.util.List;

import ba.klika.vanja.employee.model.Employee;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


/**
 * Created by vanja on 03.02.2016..
 */
public interface ApiService
{
    @GET("Employee")
    Call<ArrayList<Employee>> loadEmployees();

    @GET("Employee/{id}")
    Call<Employee> employeeDetails(@Path("id") int id);

    @PUT("Employee/{id}")
    Call<Employee> editEmployee(@Path("id") int id, @Body Employee body);

    @POST("/Employee")
    Call<Employee> createEmployee(@Body Employee employee);

    @DELETE("/Employee/{id}")
    Call<Employee> deleteEmployee(@Path("id") int id);

}
