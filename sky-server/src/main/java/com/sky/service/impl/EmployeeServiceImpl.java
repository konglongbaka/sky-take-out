package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    public Employee selectByID(Integer id) {
        Employee emp = employeeMapper.selectByID(id);
        return emp;
    }

    @Override
    public void addEmp(EmployeeDTO employeeDTO) {
        Employee emp = new Employee();
        BeanUtils.copyProperties(employeeDTO,emp);
        //设置默认状态与密码，以及创建时间更新时间。
        emp.setStatus(StatusConstant.ENABLE);
        emp.setPassword(PasswordConstant.DEFAULT_PASSWORD);
        employeeMapper.addEmp(emp);
    }

    @Override
    public PageResult pageSelect(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> pageEmp = employeeMapper.pageSelect(employeePageQueryDTO);
        long total = pageEmp.getTotal();
        List<Employee> empList = pageEmp.getResult();
        return new PageResult(total,empList);
    }

    @Override
    public void changeStatus(Integer status, Long id) {
        Employee employee = new Employee();
        employee.setStatus(status);
        System.out.println(status);
        employee.setId(id);
        employeeMapper.updateEmp(employee);
    }

    @Override
    public void updateEmp(EmployeeDTO employeeDTO) {
        Employee emp = new Employee();
        BeanUtils.copyProperties(employeeDTO,emp);
        //设置更新时间和更新用户
        employeeMapper.updateEmp(emp);
    }

}
