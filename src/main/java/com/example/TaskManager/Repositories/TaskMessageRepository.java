package com.example.TaskManager.Repositories;

import com.example.TaskManager.DataModels.TaskMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskMessageRepository extends JpaRepository<TaskMessage, Long> {
    TaskMessage findTaskMessageById(Long id);

    @Query(value = "SELECT * FROM TASK_MESSAGES WHERE TM_TK_ID = ?1 ORDER BY TM_DATE ASC", nativeQuery = true)
    List<TaskMessage> findTaskMessagesByTaskId(Long id);

    @Query(value = "SELECT T2.t2_1, T1.t1_2 FROM (SELECT TRUNC(TM.TM_DATE) t1_1, COUNT(*) t1_2 FROM TASK_MESSAGES tm WHERE  TM.TM_DATE >= ADD_MONTHS(TRUNC(SYSDATE), -1) AND TM.TM_DATE <  TRUNC(SYSDATE) + INTERVAL '1' DAY AND TM.TM_EME_ID = ?1 GROUP BY TRUNC(TM.TM_DATE)) t1 RIGHT JOIN (SELECT TRUNC(SYSDATE - ROWNUM) t2_1 FROM DUAL CONNECT BY ROWNUM < 31) t2 ON T1.t1_1 = T2.t2_1 ORDER BY T2.t2_1 ASC", nativeQuery = true)
    List<Object[]> getEmployeeEffectiveness(Long id);
}
