package ru.unvier.pis.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.Report;
import ru.unvier.pis.repository.ReportRepo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Repository
@Transactional
@RequiredArgsConstructor
public class ReportDao {
    @PersistenceContext
    private final EntityManager entityManager;
    private final ReportRepo reportRepo;

    public void saveReport(Report report) {
        if (isNull(report)) {
            throw new IllegalArgumentException("Report is empty");
        }

        entityManager.persist(report);
    }

    public Long getNextReportNumber() {
        Long maxReportNumber = reportRepo.getMaxReportNumber();
        return isNull(maxReportNumber) ? 1 : ++maxReportNumber;
    }


    public List<Report> getReportByNumberAndDate(Long reportNumber, String sellDate) {
        if (isNull(reportNumber) || isNull(sellDate)) {
            throw new IllegalArgumentException("ReportNumber or sellDate is null");
        }

        return reportRepo.getByReportNumberAndSaleDate(reportNumber, sellDate);
    }

    public List<Report> getReportByReportNumber(Long reportNumber) {
        if (isNull(reportNumber)) {
            throw new IllegalArgumentException("Report number is null");
        }

        return reportRepo.getByReportNumber(reportNumber);
    }

    public List<Report> getReports(String dateFrom, String dateTo, String sellType) {
        if (sellType.equalsIgnoreCase("ALL")) {
            return reportRepo.getByDateFromAndDateTo(dateFrom, dateTo);
        }
        return reportRepo.getByDateFromAndDateToAndSellType(dateFrom, dateTo, sellType);
    }

}
