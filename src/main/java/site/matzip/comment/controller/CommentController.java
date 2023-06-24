package site.matzip.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.comment.dto.CommentCreationDTO;
import site.matzip.comment.service.CommentService;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/create")
    public String create(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long authorId = (Long) session.getAttribute("memberId");
        //TODO : 임시로 값을 부여 추후 session 을 활용할지, rq를 활용할지 팀원들과 상의예
        model.addAttribute("authorId", 1);

        return "/comment/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute CommentCreationDTO commentCreationDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/comment/create";
        }

        commentService.create(commentCreationDTO);
        return "redirect:/";
    }
}
