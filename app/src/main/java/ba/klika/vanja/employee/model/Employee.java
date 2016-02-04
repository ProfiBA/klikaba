package ba.klika.vanja.employee.model;

import java.io.Serializable;
import nl.qbusict.cupboard.annotation.Index;

/**
 * Created by vanja on 03.02.2016..
 */
public class Employee implements Serializable
{
    public int getId() {
        return Id;
    }

    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    @Index
    public int Id;
    public String FirstName;
    public String LastName;

    public String ToString()
    {
        return  Id+" "+FirstName+" "+LastName;
    }

    public Employee(int Id, String FirstName, String LastName)
    {
        this.Id = Id;
        this.FirstName = FirstName;
        this.LastName = LastName;
    }

    public Employee() {
        this.Id = 0;
        this.FirstName = "";
        this.LastName = "";
    }
}
