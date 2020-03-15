package com.didiglobal.sds.admin.dao;

import com.didiglobal.sds.admin.dao.bean.PointDictDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by tianyulei on 2019/7/10
 **/
@Mapper
public interface PointDictDao {

    /**
     * 批量新增降级点字典数据
     *
     * @param pointDictDOList
     * @return
     */
    @Insert("<script> insert into point_dict(app_group_name, app_name, point) values" +
            " <foreach collection=\"pointDictDOList\" item=\"pointDictDO\" separator=\",\">" +
            "( #{pointDictDO.appGroupName}, #{pointDictDO.appName}, #{pointDictDO.point})</foreach> </script>")
    int addPointDictList(@Param("pointDictDOList") List<PointDictDO> pointDictDOList);

    /**
     * 新增降级点字典记录
     *
     * @param pointDictDO
     * @return
     */
    @Insert("insert into point_dict (app_group_name, app_name, point) values(#{appGroupName}, #{appName}, #{point})")
    int addPointDict(PointDictDO pointDictDO);

    /**
     * 批量删除降级点字典数据
     *
     * @param ids
     * @return
     */
    @Delete("<script> delete from point_dict where id in ( " +
            " <foreach collection=\"ids\" item=\"id\" separator=\",\">" +
            " #{id} </foreach> )</script>")
    int deletePointDictList(@Param("ids") List<Long> ids);


    /**
     * 分页查询该应用的降级字典数据
     *
     * @param appGroupName
     * @param appName
     * @return
     */
    @Select("<script>  select * from point_dict <where>" +
            "<if test='appGroupName != null'> app_group_name = #{appGroupName} and </if>" +
            "<if test='appName != null'> app_name = #{appName} and </if>" +
            "<if test='point != null'> app_name like concat('%', #{appName}, '%') </if>" +
            " </where>" +
            " order by modify_time desc " +
            " limit #{start}, #{size} </script> ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point")
    })
    List<PointDictDO> queryPointDictsByPage(@Param("appGroupName") String appGroupName,
                                            @Param("appName") String appName,
                                            @Param("point") String point, @Param("start") Integer start, @Param("size"
    ) Integer size);

    /**
     * 查询该应用所有降级字典数据
     *
     * @param appGroupName
     * @param appName
     * @return
     */
    @Select("select * from point_dict where app_group_name = #{appGroupName}  and app_name = #{appName}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point")
    })
    List<PointDictDO> queryAllPointDicts(@Param("appGroupName") String appGroupName, @Param("appName") String appName);

    /**
     * 查询降级点
     *
     * @param appGroupName
     * @param appName
     * @param point
     * @return
     */
    @Select("select * from point_dict where app_group_name = #{appGroupName} and app_name = #{appName} and point = "
            + "#{point}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "appGroupName", column = "app_group_name"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "point", column = "point")
    })
    PointDictDO queryPointDict(@Param("appGroupName") String appGroupName, @Param("appName") String appName, @Param(
            "point") String point);

}
