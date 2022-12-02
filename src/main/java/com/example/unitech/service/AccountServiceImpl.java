package com.example.unitech.service;

import com.example.unitech.DTO.AccountDTO;
import com.example.unitech.entity.Account;
import com.example.unitech.entity.User;
import com.example.unitech.exception.ConflictException;
import com.example.unitech.exception.ForbiddenException;
import com.example.unitech.exception.NotFoundException;
import com.example.unitech.payload.AccountToAccountPayload;
import com.example.unitech.payload.AddAccountPayload;
import com.example.unitech.payload.TopUpPayload;
import com.example.unitech.repository.AccountRepository;
import com.example.unitech.repository.UserRepository;
import com.example.unitech.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final ModelMapper modelMapper;

    @Override
    public AccountDTO add(AddAccountPayload payload, String bearerToken) {
        Account account = new Account();

        String token = bearerToken.substring("Bearer ".length());
        String pin = jwtUtil.extractPin(token);

        Optional<User> byPin = userRepo.findByPin(pin);
        long accountCount = accountRepo.countByAccountNumber(payload.getAccountNumber());

        if (byPin.isPresent()) {

            User user = byPin.get();

            if (accountCount == 0) {
                account.setAccountNumber(payload.getAccountNumber());
                account.setActive(true);
                account.setUser(user);

                accountRepo.save(account);
            } else {
                throw new ConflictException("Bu hesab nömrəsi artıq mövcuddur");
            }

        } else {
            throw new NotFoundException("İstifadəçi tapılmadı");
        }

        return modelMapper.map(account, AccountDTO.class);
    }

    @Override
    public List<AccountDTO> getAccounts(String bearerToken) {

        String token = bearerToken.substring("Bearer ".length());
        String pin = jwtUtil.extractPin(token);
        User user = null;

        Optional<User> byPin = userRepo.findByPin(pin);
        if (byPin.isPresent()) {
            user = byPin.get();
        }

        List<Account> allByUserAndActiveTrue = accountRepo.findAllByUserAndActiveTrue(user);

        List<AccountDTO> accountDTOS = allByUserAndActiveTrue.stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());

        if (accountDTOS.size() == 0) {
            throw new NotFoundException("Sizin aktiv bank hesabınız yoxdur");
        }

        return accountDTOS;

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public AccountDTO topUp(String bearerToken, TopUpPayload payload) {

        String token = bearerToken.substring("Bearer ".length());
        String pin = jwtUtil.extractPin(token);
        User user;
        Account account;

        Optional<User> byPin = userRepo.findByPin(pin);
        if (byPin.isPresent()) {
            user = byPin.get();
        } else {
            throw new NotFoundException("İstifadəçi tapılmadı");
        }

        Optional<Account> optAccount = accountRepo.findByUserAndAccountNumberAndActiveTrue(user, payload.getAccountNumber());

        if (optAccount.isPresent()) {
            account = optAccount.get();
            account.setBalance(payload.getAmount()+account.getBalance());
        } else {
            throw new NotFoundException("Belə bir aktiv hesabınız mövcud deyil");
        }

        return modelMapper.map(account,AccountDTO.class);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Override
    public AccountDTO accountToAccount(AccountToAccountPayload payload, String bearerToken) {

        String token = bearerToken.substring("Bearer ".length());
        String pin = jwtUtil.extractPin(token);
        User user;
        Optional<User> byPin = userRepo.findByPin(pin);
        if (byPin.isPresent()) {
            user = byPin.get();
        } else {
            throw new NotFoundException("İstifadəçi tapılmadı");
        }

        Optional<Account> optFrom = accountRepo.findByUserAndAccountNumberAndActiveTrue(user,payload.getFromAccountNumber());
        Account from;
        Account to;

        if(optFrom.isPresent()){

            from = optFrom.get();

            Optional<Account> optTo = accountRepo.findByAccountNumber(payload.getToAccountNumber());

            if(optTo.isPresent()){

                to = optTo.get();

                if( to.isActive() &&  !from.getAccountNumber().equals(to.getAccountNumber()) && from.getBalance()>= payload.getAmount()){

                    from.setBalance(from.getBalance()-payload.getAmount());
                    to.setBalance(to.getBalance()+ payload.getAmount());

                    accountRepo.save(from);
                    accountRepo.save(to);

                } else{
                    if(!to.isActive()){
                        log.error("Köçürmə etmək istədyiniz hesab aktiv deyil!");
                        throw new ForbiddenException("Köçürmə etmək istədyiniz hesab aktiv deyil!");
                    } else if(from.getAccountNumber().equals(to.getAccountNumber())){
                        log.error("Eyni hesaba köçürmə edə bilməzsiniz");
                        throw new ForbiddenException("Eyni hesaba köçürmə edə bilməzsiniz");
                    } else if(from.getBalance()<payload.getAmount()){
                        log.error("Balansınızda kifayət qədər məbləğ yoxdur");
                        throw new ForbiddenException("Balansınızda kifayət qədər məbləğ yoxdur");
                    }
                }
            }else{
                log.error("Köçürmə etmək istədyiniz hesab mövcud deyil");
                throw new NotFoundException("Köçürmə etmək istədyiniz hesab mövcud deyil");
            }
        }else{ //
            log.error("Sizin belə bir aktiv hesabınız mövcud olmadığından köçürmə edə bilməzsiniz");
            throw new ForbiddenException("Sizin belə bir aktiv hesabınız mövcud olmadığından köçürmə edə bilməzsiniz");
        }

        return modelMapper.map(from, AccountDTO.class);
    }
}
