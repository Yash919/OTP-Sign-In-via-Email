import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.otp.verification.Entity.User;
import com.otp.verification.Service.UserService;

@Controller
public class DashBoardController {

	@Autowired
	private UserService userService;

	@GetMapping("/dashboard")
	public String getDashboard(@RequestParam("email") String email, Model model) {
		User user = userService.findByEmail(email)
							   .orElseThrow(() -> new RuntimeException("User not found"));
		model.addAttribute("user", user);
		return "dashboard"; // Ensure this matches the Thymeleaf template name
	}
}