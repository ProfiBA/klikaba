using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace KlikaBa.Models
{
    public class Employee
    {
        public  int Id { get; set; }
        [Required]
        public  string FirstName { get; set; }
        [Required]
        public string LastName { get; set; }
    }
}