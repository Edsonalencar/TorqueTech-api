package br.com.starter.domain.usersGarage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersGarageService {

    private final UsersGarageRepository usersGarageRepository;

    public UsersGarage save(UsersGarage usersGarage) {
        return usersGarageRepository.save(usersGarage);
    }

    public UsersGarage getById(UUID id) {
        return usersGarageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Relação não encontrada"));
    }

    public UsersGarage getByUserAndGarage(UUID userId, UUID garageId) {
        return usersGarageRepository.findByUserAndGarage(userId, garageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Relação não encontrada"));
    }

    public List<UsersGarage> findByUser(UUID userId) {
        return usersGarageRepository.findByUser(userId);
    }

    public Page<UsersGarage> findByGarage(UUID garageId, Pageable pageable) {
        return usersGarageRepository.findByGarage(garageId, pageable);
    }

    public Page<UsersGarage> findPrimaryGarageByGarage(UUID garageId, Pageable pageable) {
        return usersGarageRepository.findPrimaryGarageByGarage(garageId, pageable);
    }

    public UsersGarage findPrimaryByUser(UUID userId) {
        return usersGarageRepository.findPrimaryByUser(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nenhuma garagem principal encontrada para este usuário"));
    }

    public boolean isUserGarage(UUID userId, UUID garageId) {
        return usersGarageRepository.isUserGarage(userId, garageId);
    }

    public boolean isUserGaragePrimary(UUID userId, UUID garageId) {
        return usersGarageRepository.isUserGaragePrimary(userId, garageId);
    }

    public boolean userAlreadyHasPrimaryGarage(UUID userId) {
        return usersGarageRepository.findPrimaryByUser(userId).isPresent();
    }
    

    public List<UsersGarage> getAll() {
        return usersGarageRepository.findAll();
    }

    @Transactional
    public UsersGarage update(UUID id, boolean isPrimary) {
        UsersGarage current = usersGarageRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Relação não encontrada"));

        if (isPrimary) {
            usersGarageRepository.findPrimaryByUser(current.getUser().getId())
            .ifPresent(usersGarage -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já possui uma garagem principal");
            });   
        }
        current.setPrimary(isPrimary);
        current.setIsPrimaryEdit(LocalDateTime.now());

        return usersGarageRepository.save(current);
    }

    @Transactional
    public boolean deleteById(UUID id) {
        if (!usersGarageRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Relação UsersGarage não encontrada");
        } 
        if (usersGarageRepository.findById(id).get().isPrimary()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível excluir a relação UsersGarage principal");
        }

        usersGarageRepository.deleteById(id);
        return true;
    }
}
