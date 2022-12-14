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
                throw new ConflictException("Bu hesab n??mr??si art??q m??vcuddur");
            }

        } else {
            throw new NotFoundException("??stifad????i tap??lmad??");
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
            throw new NotFoundException("Sizin aktiv bank hesab??n??z yoxdur");
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
            throw new NotFoundException("??stifad????i tap??lmad??");
        }

        Optional<Account> optAccount = accountRepo.findByUserAndAccountNumberAndActiveTrue(user, payload.getAccountNumber());

        if (optAccount.isPresent()) {
            account = optAccount.get();
            account.setBalance(payload.getAmount()+account.getBalance());
        } else {
            throw new NotFoundException("Bel?? bir aktiv hesab??n??z m??vcud deyil");
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
            throw new NotFoundException("??stifad????i tap??lmad??");
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
                        log.error("K??????rm?? etm??k ist??dyiniz hesab aktiv deyil!");
                        throw new ForbiddenException("K??????rm?? etm??k ist??dyiniz hesab aktiv deyil!");
                    } else if(from.getAccountNumber().equals(to.getAccountNumber())){
                        log.error("Eyni hesaba k??????rm?? ed?? bilm??zsiniz");
                        throw new ForbiddenException("Eyni hesaba k??????rm?? ed?? bilm??zsiniz");
                    } else if(from.getBalance()<payload.getAmount()){
                        log.error("Balans??n??zda kifay??t q??d??r m??bl???? yoxdur");
                        throw new ForbiddenException("Balans??n??zda kifay??t q??d??r m??bl???? yoxdur");
                    }
                }
            }else{
                log.error("K??????rm?? etm??k ist??dyiniz hesab m??vcud deyil");
                throw new NotFoundException("K??????rm?? etm??k ist??dyiniz hesab m??vcud deyil");
            }
        }else{ //
            log.error("Sizin bel?? bir aktiv hesab??n??z m??vcud olmad??????ndan k??????rm?? ed?? bilm??zsiniz");
            throw new ForbiddenException("Sizin bel?? bir aktiv hesab??n??z m??vcud olmad??????ndan k??????rm?? ed?? bilm??zsiniz");
        }

        return modelMapper.map(from, AccountDTO.class);
    }
}
