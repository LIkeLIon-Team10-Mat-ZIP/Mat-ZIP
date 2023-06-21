package site.matzip.matzip.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.service.MatzipService;

@Controller
@RequestMapping("/matzip")
@RequiredArgsConstructor
public class MatzipController {
    private final MatzipService matzipService;

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("matzipCreationDTO", new MatzipCreationDTO());
        return "/matzip/create";
    }

    @PostMapping("/create")
    public String create(@Valid MatzipCreationDTO matzipCreationDTO, BindingResult result){
        if(result.hasErrors()){
            return "/matzip/create";
        }
        matzipService.create(matzipCreationDTO);
        return "redirect:/matzip/list";
    }
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("matzipList", matzipService.findAll());
        return "/matzip/list";
    }
}
