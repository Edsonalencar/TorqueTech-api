package br.com.starter.domain.local;

import br.com.starter.domain.garage.Garage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalService {
    private final LocalRepository localRepository;

    public Local save(Local local) {
        return localRepository.save(local);
    }

    public Optional<Local> getByIdAndGarageId(UUID localId, UUID garageId) {
        return localRepository.findByIdAndGarageId(garageId, localId);
    }

    public void delete(Local local) {
        localRepository.delete(local);
    }

    public List<Local> findAllByGarageAndQuery(Garage garage) {
        return localRepository.findAllByGarageAndQuery(
            garage.getId(),
            LocalStatus.ACTIVE
        );
    }

    public Page<Local> findAllByGarageAndQuery(Garage garage, String query, LocalStatus status, Pageable pageable) {
        return localRepository.findAllByGarageAndQuery(
            garage.getId(),
            status,
            query,
            pageable
        );
    }
}
