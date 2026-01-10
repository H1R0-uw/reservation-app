package jp.levtech.rookie.reserve;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.levtech.rookie.reserve.service.ReservationService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor

public class HomeController {
	//top
	@GetMapping("/")
	public String index() {
		return "top"; //template/top.html
	}
	//menu
	@GetMapping("/menu")
	public String menu() {
		return "menu"; //template/menu.html
	}
	//info

	private final ReservationService reservationService;

	@GetMapping("/info")
	public String info(
	        @RequestParam(required = false) Integer year,
	        @RequestParam(required = false) Integer month,
	        Model model) {

	    YearMonth ym = (year != null && month != null)
	            ? YearMonth.of(year, month)
	            : YearMonth.now();

	    List<List<LocalDate>> weeks = new ArrayList<>();

	    LocalDate firstDay = ym.atDay(1);
	    LocalDate start = firstDay.minusDays(firstDay.getDayOfWeek().getValue() % 7);

	    LocalDate current = start;

	    for (int w = 0; w < 6; w++) {
	        List<LocalDate> week = new ArrayList<>();
	        for (int d = 0; d < 7; d++) {
	            if (current.getMonth() == ym.getMonth()) {
	                week.add(current);
	            } else {
	                week.add(null);
	            }
	            current = current.plusDays(1);
	        }
	        weeks.add(week);
	    }

	    // ★ 表示月の業務ロジック用期間
	    LocalDate logicStart = ym.atDay(1);
	    LocalDate logicEnd   = ym.atEndOfMonth();

	    model.addAttribute("weeks", weeks);
	    model.addAttribute(
	        "calendarStatus",
	        reservationService.getCalendarStatus(logicStart, logicEnd)
	    );
	    model.addAttribute("currentMonth", ym);
	    model.addAttribute("prevMonth", ym.minusMonths(1));
	    model.addAttribute("nextMonth", ym.plusMonths(1));

	    return "info";
	}
}
