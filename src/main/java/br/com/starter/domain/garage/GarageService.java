package br.com.starter.domain.garage;

import br.com.starter.domain.manager.ManagerService;
import br.com.starter.domain.mechanic.MechanicService;
import br.com.starter.domain.user.User;
import br.com.starter.domain.user.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GarageService {
    private final GarageRepository garageRepository;
    private final MechanicService mechanicService;
    private final ManagerService managerService;

    public Garage save(Garage garage) {
        return garageRepository.save(garage);
    }

    public List<Garage> getAll() {
        return garageRepository.findAll();
    }

    public Optional<Garage> getById(UUID id) {
        return garageRepository.findById(id);
    }

    public Page<Garage> findAllPage(Pageable pageable) {
        return garageRepository.findAll(pageable);
    }

    public Page<Garage> getPageByOwnerStatusAndName(
        String query,
        UserStatus userStatus,
        Pageable pageable
    ) {
        return garageRepository.findPageByStatusAndNames(
            query,
            userStatus,
            pageable
        );
    }

    public Garage getByUser(User user) {
        var mechanic = mechanicService.getByUser(user);
        if (mechanic.isPresent())
            return mechanic.get().getGarage();

        var manager= managerService.getByUser(user);
        if (manager.isPresent())
            return manager.get().getGarage();

        return  garageRepository.findByOwner(user).orElseThrow(() ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O usuário não possui uma oficina registrada"
            )
        );
    }
}
