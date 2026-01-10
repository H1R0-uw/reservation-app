package jp.levtech.rookie.reserve.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.levtech.rookie.reserve.mapper.ReservationMapper;
import jp.levtech.rookie.reserve.model.Reservation;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationMapper mapper;
	
	//予約一覧（管理画面用）
	public List<Reservation> findAll() {
		return mapper.findAll();
	}
	
	public Reservation findById(Long id) {
		return mapper.findById(id);
	}
	
	@Transactional
	public void update(Reservation reservation) {
	    mapper.update(reservation);
	}

	@Transactional
	public void deleteById(Long id) {
	    mapper.deleteById(id);
	}
	
	@Transactional
	public void register(Reservation reservation) {
		mapper.insert(reservation);
	}
	//日別予約人数（生データ）
	public List<Map<String, Object>> findCountByDate() {
		return mapper.findCountByDate();
	}
	// カレンダー表示用（○△×判定）
	public Map<String, String> getCalendarStatus(LocalDate start, LocalDate end) {

	    List<Map<String, Object>> list = mapper.findDailyTotalPeople();
	    Map<String, String> result = new HashMap<>();

	    // ① 予約データから ○△× を作る
	    for (Map<String, Object> row : list) {

	        Object dateObj = row.get("date");
	        Object totalObj = row.get("total_people");

	        if (dateObj == null || totalObj == null) {
	            continue;
	        }

	        LocalDate date;
	        if (dateObj instanceof java.sql.Date) {
	            date = ((java.sql.Date) dateObj).toLocalDate();
	        } else if (dateObj instanceof LocalDate) {
	            date = (LocalDate) dateObj;
	        } else {
	            continue;
	        }

	        int totalPeople = ((Number) totalObj).intValue();

	        String status;
	        if (totalPeople <= 5) {
	            status = "○";
	        } else if (totalPeople <= 9) {
	            status = "△";
	        } else {
	            status = "×";
	        }

	        result.put(date.toString(), status);
	    }

	    // ② 月曜日は強制的に ×（定休日）
	    for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
	        if (d.getDayOfWeek() == DayOfWeek.MONDAY) {
	            result.put(d.toString(), "×");
	        }
	    }

	    return result;
	}
	
}
