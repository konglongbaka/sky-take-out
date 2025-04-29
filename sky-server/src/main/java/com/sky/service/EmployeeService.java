package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Employee selectByID(Integer id);

    void addEmp(EmployeeDTO employeeDTO);

    PageResult pageSelect(EmployeePageQueryDTO pageResult);

    void changeStatus(Integer status, Long id);

    void updateEmp(EmployeeDTO employeeDTO);
}
