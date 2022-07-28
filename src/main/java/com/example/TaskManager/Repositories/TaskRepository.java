package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.Employee;
import com.example.TaskManager.DataModels.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM TASK WHERE TK_ID LIKE (CASE WHEN :id IS NOT NULL THEN :id ELSE '%%' END) AND TK_STATUS LIKE %:status% AND TK_URGENCY LIKE %:urgency% AND TK_PR_DEP LIKE %:proponentDepartment% AND TK_RES_DEP LIKE %:responsibleDepartment% ORDER BY TK_LAST_ANSWER DESC", nativeQuery = true)
    List<Task> findByFilter(String id, String urgency, String status, String proponentDepartment, String responsibleDepartment);

    @Query(value = "SELECT * FROM (SELECT * FROM TASK ORDER BY TK_DATE_OF_CREATION DESC) WHERE ROWNUM = 1", nativeQuery = true)
    Task findLatestTask();

    @Query(value = "SELECT * FROM TASK WHERE TK_RES_DEP = ?1 AND TK_RESPONSIBLE IS NULL ORDER BY TK_DEADLINE ASC", nativeQuery = true)
    List<Task> findUnallocatedTasksByDepartment(Integer id);

    @Query(value = "SELECT * FROM TASK WHERE TK_RES_DEP = ?1 AND TK_DEADLINE IS NOT NULL ORDER BY TK_DEADLINE ASC", nativeQuery = true)
    List<Task> findDeadlineTasksByDepartment(Integer id);

    List<Task> findTasksByResponsibleOrderByLastAnswerDesc(Employee employee);

    List<Task> findTasksByProponentOrderByLastAnswerDesc(Employee employee);

    Task findTaskById(Long id);
}
