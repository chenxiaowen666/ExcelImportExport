package com.example.demo.mapper;

import com.example.demo.dto.ProjectDTO;
import com.example.demo.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ProjectMapper {
    Project findByProjectName(String projectName);
    void insert(Project project);
    void update(Project project);
    List<Project> findByConditions(@Param("projectDTO") ProjectDTO projectDTO);
    int deleteByConditions(@Param("projectDTO") ProjectDTO projectDTO);
    // 新增批量方法
    void batchInsert(@Param("projects") List<Project> projects);
    void batchUpdate(@Param("projects") List<Project> projects);
}