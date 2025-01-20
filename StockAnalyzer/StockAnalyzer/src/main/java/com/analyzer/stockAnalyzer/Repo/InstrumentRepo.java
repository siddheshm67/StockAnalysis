package com.analyzer.stockAnalyzer.Repo;

import com.analyzer.stockAnalyzer.Models.Instruments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentRepo extends JpaRepository<Instruments, Long> {
    Optional<List<Instruments>> findByTradingSymbolContaining(String name);


}
