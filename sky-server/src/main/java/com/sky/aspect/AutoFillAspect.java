package com.sky.aspect;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..))&&@annotation(com.sky.anno.AutoFillUpdate)")
    public void updatePointCut(){}
    @Pointcut("execution(* com.sky.mapper.*.*(..))&&@annotation(com.sky.anno.AutoFillInsert)")
    public void insertPointCut(){}

    @Before("updatePointCut()")
    public void updateBefore(JoinPoint joinPoint){
        //
        log.info("更新自动填充");
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            Object entity = args[0];
            try {
                // 获取setUpdateUser方法
                Method methodSetUpdateUser = entity.getClass().getMethod("setUpdateUser", Long.class);
                methodSetUpdateUser.invoke(entity, BaseContext.getCurrentId());
            } catch (NoSuchMethodException e) {
                log.error("实体类没有对应方法", e);
            } catch (Exception e) {

//        Object[] args = joinPoint.getArgs();
//        Object target = joinPoint.getTarget();
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取代理方法的信息
//        String methodName = signature.getName(); // 获取方法名
//        Class<?> returnType = signature.getReturnType(); // 获取返回类型
//        Class<?>[] parameterTypes = signature.getParameterTypes();
//        System.out.println(args[0]);
//        System.out.println("目标对象：" + target);
//        System.out.println("目标对象类型：" + target.getClass());
//        System.out.println("方法签名：" + signature);
//        System.out.println("方法名：" + methodName);
//        System.out.println("返回类型：" + returnType.getName());
//        for (Class<?> parameterType : parameterTypes) {
//            System.out.println("参数类型：" + parameterType.getName());
//        }
                //Employee(id=2, username=null, name=null, password=null, phone=null, sex=null, idNumber=null, status=1, createTime=null, updateTime=2025-03-25T14:11:54.103355600, createUser=null, updateUser=10)
                //目标对象：org.apache.ibatis.binding.MapperProxy@35cd3b2
                //目标对象类型：class jdk.proxy2.$Proxy84
                //方法签名：void com.sky.mapper.EmployeeMapper.updateEmp(Employee)
                //方法名：updateEmp
                //返回类型：void
                //参数类型：com.sky.entity.Employee
            }
        }
    }
    @Before("insertPointCut()")
    public void insertBefore(JoinPoint joinPoint){
        log.info("插入自动填充");
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
            Object entity = args[0];
            try {
                // 获取setUpdateUser方法
                Method methodSetUpdateUser = entity.getClass().getMethod("setUpdateUser", Long.class);
                Method methodSetCreateUser =  entity.getClass().getMethod("setCreateUser", Long.class);
                Method methodSetStatus = entity.getClass().getMethod("setStatus", Integer.class);
                // 调用setUpdateUser方法
                methodSetUpdateUser.invoke(entity, BaseContext.getCurrentId());
                methodSetCreateUser.invoke(entity, BaseContext.getCurrentId());
                methodSetStatus.invoke(entity, StatusConstant.ENABLE);
                //反射的用法例子
                //1.获取类
                //Class<?> clazz = entity.getClass();
                //2.获取方法
                //Method methodSetUpdateUser = clazz.getMethod("setUpdateUser", Long.class);
                //3.获取构造方法
                //Constructor<?> constructor = clazz.getConstructor();
                //4.获取所有构造方法，包括被保护，私有
                //Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                //5.获取所有方法
                //Method[] methods = clazz.getDeclaredMethods();
                //6.获取所有字段
                //Field[] fields = clazz.getDeclaredFields();
                //7.获取字段
                //Field field = clazz.getDeclaredField("id");
                //8.构造对象
                //Object obj = clazz.getConstructor().newInstance();
                //9.调用方法
                //methodSetUpdateUser.invoke(obj, 1L);
                //10.为字段设值
                //field.set(obj, 1L);
                //11.获取字段值
                //Object value = field.get(obj);
            } catch (NoSuchMethodException e) {
                log.error("实体类没有对应方法", e);
            } catch (Exception e) {
                log.error("调用方法时发生异常", e);
            }
        }
    }
}

