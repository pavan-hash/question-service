package com.pk.question_service_v3.Repository;



import com.pk.question_service_v3.model.question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface questionRepo extends JpaRepository<question,Integer> {

    @Query(value = "SELECT q.id FROM question q Where q.category=:category ORDER BY RANDOM() LIMIT :numq", nativeQuery=true)
    List<Integer> findRandomQuestionsByCategory(String category, int numq);
}
