package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.TaskViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskViewLogRepository extends JpaRepository<TaskViewLog, Long>{
    @Query(value = "SELECT * FROM TASK_VIEW_LOG WHERE TVL_TK_ID = ?1 ORDER BY TVL_DATE DESC", nativeQuery = true)
    List<TaskViewLog> getTaskViewLogByTaskId(Long id);
}
