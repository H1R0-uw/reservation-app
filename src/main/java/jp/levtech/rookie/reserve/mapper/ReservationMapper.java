package jp.levtech.rookie.reserve.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import jp.levtech.rookie.reserve.model.Reservation;

@Mapper
public interface ReservationMapper {
    List<Reservation> findAll();
    void insert(Reservation reservation);
    List<Map<String, Object>> findCountByDate();
    List<Map<String, Object>> findDailyTotalPeople();
    
    Reservation findById(Long id);
    void update(Reservation reservation);
    void deleteById(Long id);
}
