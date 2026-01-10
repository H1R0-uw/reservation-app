package jp.levtech.rookie.reserve;


import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.levtech.rookie.reserve.model.Reservation;
import jp.levtech.rookie.reserve.service.ReservationService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reserve")
  //@SessionAttributes("reservation")
public class ReservationController {

	private final ReservationService reservationService;

	// @ModelAttribute("reservation")
	// public Reservation setupReservation() {
	//    return new Reservation();
	// }
	
	@GetMapping("")
	public String reserveRoot() {
	    return "redirect:/reserve/form";
	}
	
	  @GetMapping("/form")
	    public String showForm(
	            @RequestParam(required = false)
	            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	            LocalDate date,
	            Model model) {

	        Reservation reservation = new Reservation();

	        if (date != null) {
	            reservation.setDate(date);
	        }

	        model.addAttribute("reservation", reservation);
	        return "reserve/form";
	    }

	  @PostMapping("/confirm")
	  public String confirm(@ModelAttribute Reservation reservation,
	                        RedirectAttributes ra) {
	      ra.addFlashAttribute("reservation", reservation);
	      return "redirect:/reserve/confirm";
	  }

	  @GetMapping("/confirm")
	  public String confirmView() {
	      return "reserve/confirm";
	  }
	
	

	 @PostMapping("/completed")
	 public String completedPost(
	         @ModelAttribute Reservation reservation,
	         Model model) {

	    
		    System.out.println("★★★ completed POST 到達 ★★★");
		    
		   
		    try {
		        System.out.println("【DB登録前】");
		        reservationService.register(reservation);
		        System.out.println("【DB登録後】");
		    } catch (Exception e) {
		        e.printStackTrace();
		        model.addAttribute("error", "内部エラーが発生しました");
		        return "reserve/form";
		    }

	    // reservationService.register(reservation);
	     model.addAttribute("reservation", reservation);
	     return "reserve/completed";
	 }

	 //@GetMapping("/completed")
	 //public String completedGet() {
	 //    return "reserve/completed";
	 //}

	
	@GetMapping("/list") // 管理者用一覧
	public String list(Model model) {
		model.addAttribute("reservations", reservationService.findAll());
		return "reserve/list";
	}
	
	@GetMapping("/edit/{id}")
	public String adminEditForm(@PathVariable("id") Long id, Model model) {
		Reservation r = reservationService.findById(id);
		model.addAttribute("reservation", r);
		return "reserve/edit";
	}
	
	@PostMapping("/update")
	public String adminUpdate(@ModelAttribute Reservation reservation, Model model) {
		reservationService.update(reservation);
		return "redirect:/reserve/list";
	}
	
	@PostMapping("/delete")
	public String adminDelete(@RequestParam("id") Long id) {
		reservationService.deleteById(id);
		return "redirect:/reserve/list";
	}
	
	@ResponseBody
	@GetMapping("/calendar")
	public Map<String, String> getCalendarStatus(
	        @RequestParam int year,
	        @RequestParam int month) {

	    YearMonth ym = YearMonth.of(year, month);

	    LocalDate start = ym.atDay(1);
	    LocalDate end = ym.atEndOfMonth();

	    return reservationService.getCalendarStatus(start, end);
	}
}
