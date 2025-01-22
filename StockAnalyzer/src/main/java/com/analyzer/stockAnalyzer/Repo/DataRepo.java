package com.analyzer.stockAnalyzer.Repo;

import com.analyzer.stockAnalyzer.Models.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepo extends JpaRepository<HistoricalData,Long> {

}
