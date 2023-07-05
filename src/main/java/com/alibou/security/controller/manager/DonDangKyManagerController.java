package com.alibou.security.controller.manager;

import com.alibou.security.DTO.DonDangKy.DonDangKyManagerDTO;
import com.alibou.security.Entity.DaoTrang;
import com.alibou.security.Entity.DonDangKy;
import com.alibou.security.Entity.User;
import com.alibou.security.service.daoTrang.DaoTrangService;
import com.alibou.security.service.donDangKy.DonDangKyService;
import com.alibou.security.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/manager/don-dang-ky")
@PreAuthorize("hasRole('MANAGER')")
public class DonDangKyManagerController {
    @Autowired
    private DonDangKyService donDangKyService;
    @Autowired
    private DaoTrangService daoTrangService;

    @Autowired
    private UserService userService;
    @GetMapping
    public Page<DonDangKy> pagination(@RequestParam(defaultValue = "0") int pageNo,
                                      @RequestParam(defaultValue = "10") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        List<DonDangKy> list = donDangKyService.getAll();
        return new PageImpl<>(list, pageable, list.size());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody DonDangKyManagerDTO donDangKyManagerDTO, @PathVariable Integer id) {

        return donDangKyService.getById(id).map(donDangKy -> {
            try {
                User user = userService.getUserById(donDangKyManagerDTO.getNguoiSuLyId()).get();
                donDangKy.setNgaySuLy(LocalDateTime.now());
                donDangKy.setNguoiSuLyId(donDangKyManagerDTO.getNguoiSuLyId());
                return new ResponseEntity<>(donDangKyService.save(donDangKy), HttpStatus.OK);
            } catch (NoSuchElementException e) {
                return new ResponseEntity<>("Update Fail", HttpStatus.NOT_FOUND);
            }
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Don dang ky not found"));

    }



}