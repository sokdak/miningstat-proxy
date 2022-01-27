package me.sokdak.miningstatproxy.repository;

import java.util.Optional;
import me.sokdak.miningstatproxy.domain.Miner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinerRepository extends JpaRepository<Miner, String> {
  Optional<Miner> findByIp(String ip);
}
