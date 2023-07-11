package site.matzip.home;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import site.matzip.matzip.dto.MatzipRankDTO;
import site.matzip.matzip.service.MatzipService;
import site.matzip.member.dto.MemberRankDTO;
import site.matzip.member.service.MemberService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MatzipService matzipService;
    private final MemberService memberService;

    @GetMapping("/")
    public String showMain() {
        return "matzip/list";
    }

    @GetMapping("/ranking")
    public String showRanking(Model model) {
        List<MatzipRankDTO> matzipRankDTOS = matzipService.findAndConvertTopTenMatzip();
        List<MemberRankDTO> memberRankDtoList = memberService.findAndConvertTopTenMember();

        model.addAttribute("memberRankDtoList", memberRankDtoList);
        model.addAttribute("matzipRankDTOS", matzipRankDTOS);

        return "/ranking/ranking";
    }
}
