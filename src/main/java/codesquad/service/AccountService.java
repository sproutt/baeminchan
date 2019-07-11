package codesquad.service;

import codesquad.domain.Account;
import codesquad.domain.AccountRepository;
import codesquad.dto.SignUpDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account create(SignUpDTO signUpDTO) {
        if (!signUpDTO.isCheckingPassWordMatch()) {
            throw new RuntimeException();
        }

        return accountRepository.save(new Account(signUpDTO));
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }
}
