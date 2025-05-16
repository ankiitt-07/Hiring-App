package com.hiringapp.controller;

import com.hiringapp.utils.dtos.BankInfoDTO;
import com.hiringapp.service.BankInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank-info")
@Slf4j
public class BankInfoController {

    private final BankInfoService bankInfoService;

    public BankInfoController(BankInfoService bankInfoService) {
        this.bankInfoService = bankInfoService;
    }

    @PostMapping
    public ResponseEntity<BankInfoDTO> createBankInfo(@RequestBody @Valid BankInfoDTO bankInfoDTO) {
        BankInfoDTO saved = bankInfoService.saveBankInfo(bankInfoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankInfoDTO> getBankInfo(@PathVariable Long id) {
        BankInfoDTO bankInfo = bankInfoService.getBankInfoById(id);
        return ResponseEntity.ok(bankInfo);
    }
}
