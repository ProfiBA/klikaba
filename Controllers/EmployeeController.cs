using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using System.Web.Http.Results;
using System.Web.Mvc;
using KlikaBa.Models;

namespace KlikaBa.Controllers
{
    public class EmployeeController : ApiController
    {
        // Database context
        private readonly EmployeeContext _db = new EmployeeContext();
       
        
        // GET /employee - Get list of all employees in JSON format
        public JsonResult<List<Employee>> Get()
        {
            try
            {
                var list = _db.Employees.ToList();
                return Json(list);
            }
            catch (Exception ex)
            {
                throw new Exception(ex.Message);
            }
        }

        // GET /employee/5 - Get employee by ID param
        public JsonResult<Employee> Get(int id)
        {
            var emp = _db.Employees.Find(id);
            if (emp == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }
            return Json(emp);
        }


        
        public JsonResult<Employee> Put(int id, Employee employee)
        {
            Employee foundEmployee = _db.Employees.Where(x => x.Id == id).FirstOrDefault();
            if (foundEmployee != null)
            {
                foundEmployee.FirstName = employee.FirstName;
                foundEmployee.LastName = employee.LastName;
            }
            _db.SaveChanges();
            return Json(employee);
        }

        // POST /employee - Create new employee
        public JsonResult<Employee> Post(Employee employee)
        {
                _db.Employees.Add(employee);
                _db.SaveChanges();
               return Json(employee);
        }

        // DELETE /employee/5 - Delete employee by ID param
        public void Delete(int ?id)
        {
            if (id == null)
            {
                throw new HttpResponseException(HttpStatusCode.BadRequest);
            }

            var emp = _db.Employees.Find(id);
            if (emp == null)
            {
                throw new HttpResponseException(HttpStatusCode.NotFound);
            }

            _db.Employees.Remove(emp);
            _db.SaveChanges();
        }
    }
}
