package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 登出
      * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> selectEmpByID(@PathVariable Integer id){
        log.info("员工通过id查询{}",id);
        Employee emp = employeeService.selectByID(id);
        return Result.success(emp);
    }

    @PostMapping
    public Result<String> addEmp(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工: {}",employeeDTO);
        employeeService.addEmp(employeeDTO);
        return Result.success(new String("新增成功") );
    }

    @GetMapping("/page")
//    @Cacheable(cacheNames = "admin:emp", key = "'pageSelect'")
    public Result<PageResult> pageEmpSelect(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("分页查询");
        PageResult pageResult = employeeService.pageSelect(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    public Result<String> changeEmpStatus(@PathVariable Integer status,Long id){
        log.info("员工禁用启用");
        employeeService.changeStatus(status,id);
        return Result.success();
    }

    @PutMapping
    public Result<String> updateEmp(@RequestBody EmployeeDTO employeeDTO){
        log.info("更新员工信息");
        employeeService.updateEmp(employeeDTO);
        return Result.success();
    }

}
