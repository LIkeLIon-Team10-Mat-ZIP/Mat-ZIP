package site.matzip.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public String create(Model model) {
        model.addAttribute("commentCreationDTO", new CommentCreationDTO());
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
