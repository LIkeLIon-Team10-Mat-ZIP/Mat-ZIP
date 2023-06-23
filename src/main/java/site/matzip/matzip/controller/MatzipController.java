package site.matzip.matzip.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import site.matzip.base.rsData.RsData;
import site.matzip.matzip.domain.Matzip;
import site.matzip.matzip.dto.MatzipCreationDTO;
import site.matzip.matzip.dto.MatzipListDTO;
import site.matzip.matzip.service.MatzipService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matzip")
@RequiredArgsConstructor
public class MatzipController {
    private final MatzipService matzipService;

    @GetMapping("/")
    public String create(Model model) {
        model.addAttribute("matzipCreationDTO", new MatzipCreationDTO());
        return "/matzip/create";
    }

    @PostMapping("/create")
    public String create(@RequestBody MatzipCreationDTO matzipCreationDTO, BindingResult result) {
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
                        .address(matzip.getAddress())
                        .phoneNumber(matzip.getPhoneNumber())
                        .matzipType(matzip.getMatzipType())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("matzipList", matzipDtoList);
        return "/matzip/list";
    }

    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<MatzipListDTO>> search() {
        List<Matzip> matzipList = matzipService.findAll();
        List<MatzipListDTO> matzipDtoList = matzipList.stream()
                .map(matzip -> MatzipListDTO.builder()
                        .matzipName(matzip.getMatzipName())
                        .address(matzip.getAddress())
                        .phoneNumber(matzip.getPhoneNumber())
                        .matzipUrl(matzip.getMatzipUrl())
                        .matzipType(matzip.getMatzipType())
                        .x(matzip.getX())
                        .y(matzip.getY())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(matzipDtoList);
    }
}
