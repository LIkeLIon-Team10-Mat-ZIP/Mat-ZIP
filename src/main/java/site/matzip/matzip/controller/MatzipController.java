package site.matzip.matzip.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.dto.MatzipListDTO;
import site.matzip.matzip.service.MatzipService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public String create(@Valid MatzipCreationDTO matzipCreationDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/matzip/create";
        }
        matzipService.create(matzipCreationDTO);
        return "redirect:/matzip/list";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Matzip> matzipList = matzipService.findAll();
        List<MatzipListDTO> matzipDtoList = matzipList.stream()
                .map(matzip -> MatzipListDTO.builder()
                        .matzipName(matzip.getMatzipName())
                        .description(matzip.getDescription())
                        .address(matzip.getAddress())
                        .phoneNumber(matzip.getPhoneNumber())
                        .matzipType(matzip.getMatzipType())
                        .openingTime(matzip.getOpeningTime())
                        .closingTime(matzip.getClosingTime())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("matzipList", matzipDtoList);
        return "/matzip/list";
    }
}
