package com.kgk.betsettlement.repository;

import com.kgk.betsettlement.model.Bet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, String> {

  List<Bet> findByEventId(String eventId);
}
