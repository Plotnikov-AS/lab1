package ru.unvier.pis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.unvier.pis.model.entity.Report;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepo extends JpaRepository<Report, Long> {
    @Query(value = "SELECT rep.report_number " +
            "FROM local.table_report rep " +
            "order by rep.report_number desc " +
            "limit 1", nativeQuery = true)
    Long getMaxReportNumber();

    @Query(value = "SELECT r.* FROM local.table_report r " +
            "WHERE r.report_number = :reportNumber " +
            "AND r.sale_date = :saleDate ", nativeQuery = true)
    List<Report> getByReportNumberAndSaleDate(@Param("reportNumber") Long reportNumber,
                                              @Param("saleDate") String saleDate);

    List<Report> getByReportNumber(Long reportNumber);

    @Query(value = "SELECT * " +
            "FROM local.table_report r " +
            "WHERE r.sale_date >= :dateFrom " +
            "AND r.sale_date <= :dateTo " +
            "AND r.sale_type = :sellType " +
            "order by r.sale_date", nativeQuery = true)
    List<Report> getByDateFromAndDateToAndSellType(@Param("dateFrom") String dateFrom,
                                                   @Param("dateTo") String dateTo,
                                                   @Param("sellType") String sellType);

    @Query(value = "SELECT * " +
            "FROM local.table_report r " +
            "WHERE r.sale_date >= :dateFrom " +
            "AND r.sale_date <= :dateTo " +
            "order by r.sale_date", nativeQuery = true)
    List<Report> getByDateFromAndDateTo(@Param("dateFrom") String dateFrom,
                                        @Param("dateTo") String dateTo);
}
