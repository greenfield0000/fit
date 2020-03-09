package greenfield.group.com.authservice.security.service.impl;

import greenfield.group.com.authservice.model.Account;
import greenfield.group.com.authservice.model.Role;
import greenfield.group.com.authservice.model.Status;
import greenfield.group.com.authservice.security.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link AccountService} interface.
 * Wrapper for {@link AccountRepository} + business logic.
 *
 * @author Ivanov Roman
 * @version 1.0
 */

@Slf4j
@Service
public class AccountService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account) {
        Role roleAccount = new Role();
        roleAccount.setName("Пользователь");
        roleAccount.setSysname("ROLE_USER");
        List<Role> accountRoles = new ArrayList<>();
        accountRoles.add(roleAccount);

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setStatus(Status.ACTIVE.name());

        Account registeredAccount = accountRepository.save(account);

        log.info("IN register - Account: {} successfully registered", registeredAccount);

        return registeredAccount;
    }

    public List<Account> getAll() {
        List<Account> result = accountRepository.findAll();
        log.info("IN getAll - {} Accounts found", result.size());
        return result;
    }

    public Account findByLogin(String nickName) {
        Account result = accountRepository.findByNickName(nickName);
        log.info("IN findByLogin - Account: {} found by Accountname: {}", result, nickName);
        return result;
    }

    public Account findById(Long id) {
        Account result = accountRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no Account found by id: {}", id);
            return null;
        }

        log.info("IN findById - Account: {} found by id: {}", result);
        return result;
    }

    public void delete(Long id) {
        accountRepository.deleteById(id);
        log.info("IN delete - Account with id: {} successfully deleted");
    }
}
