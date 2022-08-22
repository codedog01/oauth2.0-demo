package com.lengao.oauth2.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>description goes here</p>
 *
 * @author 冷澳
 * @date 2022/8/4
 */
@Mapper
@Component
public interface TbUserMapper {
    /**
     * 根据用户名查询用户
     */
    @Select(" SELECT client_secret from oauth_client_details where client_id = #{clientId}")
    String findByClientId(String clientId);


    /**
     * 查询用户的权限根据用户查询权限
     */
    @Select("SELECT " +
            "concat(service_name,service_url) " +
            "FROM " +
            "oauth_service_url  " +
            "WHERE " +
            "client_id = #{clientId}")
    List<String> findPermissionByClientId(String clientId);
    @Select("SELECT code FROM oauth_code WHERE client_id = #{client_id}")
    List<String> findCodeByClientId(String client_id);
}
