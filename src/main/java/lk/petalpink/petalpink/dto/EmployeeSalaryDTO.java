package lk.petalpink.petalpink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeSalaryDTO {
    private Integer salaryId;
    private Integer employeeId;
    private String  employeeName;
    private String  month;           // "YYYY-MM"
    private Double  basicSalary;
    private Double  allowances;
    private Double  overtime;
    private Double  deductions;
    private Double  netSalary;
    private String  paymentStatus;   // paid | pending
    private String  note;
}