using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace KlikaBa.Models
{
    public class EmployeeContext: DbContext
    {
        // Constructor and name of connection string in web.config
        public EmployeeContext(): base("Connection")
        {

        }
        public DbSet<Employee> Employees { get; set; }
    }
}