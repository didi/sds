package com.didiglobal.sds.admin.dao;

import com.didiglobal.sds.admin.dao.bean.UserPrivilege;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户权限DAO
 *
 * @auther manzhizhen
 * @date 2019/1/18
 */
public interface UserPrivilegeDao {

    /**
     * 给某个用户新增权限
     *
     * @param userName
     * @param appGroupName
     * @param appName
     * @return
     */
    @Insert("<script> insert into user_privilege(user_name, app_group_name, app_name) " +
            "values" +
            "<foreach collection=\"appNameList\" start=\"(\" end=\")\" item=\"appName\">" +
            "#{userName}, #{appGroupName}, #{appName}" +
            "</foreach>" +
            " </script>")
    int addUserPrivilege(@Param("userName") String userName,
                         @Param("userPrivilegeList") List<UserPrivilege> userPrivilegeList);

    /**
     * 查询某个用户权限
     *
     * @param userName
     * @param appGroupName
     * @param appName
     * @return
     */
    @Select("select * from user_privilege where user_name = #{userName} limit #{start}, #{size}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userName", column = "user_name"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "sdsSchemeName", column = "sds_scheme_name"),
            @Result(property = "modifiedTime", column = "modify_time"),
            @Result(property = "createTime", column = "create_time")
    })
    List<UserPrivilege> queryUserPrivilegeByPage(@Param("userName") String userName, @Param("start") Integer start,
                                                 @Param("size") Integer size);

}
