package ba.klika.vanja.employee.data;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ba.klika.vanja.employee.R;
import ba.klika.vanja.employee.model.Employee;

/**
 * Created by vanja on 03.02.2016..
 */
public class EmployeeAdapter extends ArrayAdapter<Employee>
{
    ArrayList<Employee> data = new ArrayList<Employee>();
    ImageView btnDelete;
    private boolean mode = false;

    public static final String TAG = EmployeeAdapter.class.getSimpleName();

        public EmployeeAdapter(Context context, ArrayList<Employee> users)
        {
            super(context, 0, users);
            data = users;
        }
        public void Toogle(Boolean mode)
        {
            this.mode = mode;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            Log.d(TAG,"Enter getView");
            // Get the data item for this position
            final Employee user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_employee, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.lblFirstName);
            TextView tvSurname = (TextView) convertView.findViewById(R.id.lblLastName);
            btnDelete = (ImageView) convertView.findViewById(R.id.imgDeleteIcon);
            btnDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    Resources res = getContext().getResources();

                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.msg_deleteConfirm)
                            .setMessage(String.format(res.getString(R.string.msg_deleteConfirm2), user.getFirstName()))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d("EmployeeAdapter","DELETE"+user.getId());
                                    EmployeeService.DeleteEmployee(getContext(), user.getId(),data, position);
                                    notifyDataSetChanged();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            // Populate the data into the template view using the data object
            tvName.setText(user.FirstName);
            tvSurname.setText(user.LastName);
            // Return the completed view to render on screen

            btnDelete.setVisibility(mode? View.VISIBLE: View.INVISIBLE);
            if (mode)
                convertView.setClickable(true);
            else if(mode == false)
                convertView.setClickable(false);

            return convertView;
        }
    }

