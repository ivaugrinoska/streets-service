package mk.ukim.finki.streetsservice.service;

import lombok.extern.slf4j.Slf4j;
import mk.ukim.finki.streetsservice.entity.Street;
import mk.ukim.finki.streetsservice.entity.exceptions.StreetByNameNotFoundException;
import mk.ukim.finki.streetsservice.entity.exceptions.StreetNotFoundException;
import mk.ukim.finki.streetsservice.repository.StreetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StreetService {

    @Autowired
    private StreetRepository streetRepository;

    public Optional<Street> findByName(String name) {
        return Optional.of(this.streetRepository.findByName(name)
                .orElseThrow(() -> new StreetByNameNotFoundException(name)));
    }

    public Optional<Street> findById(Long id) {
        return Optional.of(this.streetRepository.findById(id)
                .orElseThrow(() -> new StreetNotFoundException(id)));
    }

    public List<Street> findAll() {
        return this.streetRepository.findAll();
    }


    @Transactional
    public Street addNewStreet(String name, Float latitude, Float longitude) {

        if (name.isEmpty())
            return null;

        this.streetRepository.deleteByName(name);
        return this.streetRepository.save(new Street(name, latitude, longitude));
    }


    public void deleteById(Long id) {
        this.streetRepository.deleteById(id);
    }

    @Transactional
    public Street editStreet(Long id, String name, Float latitude, Float longitude) {
        Street street = this.streetRepository.findById(id)
                .orElseThrow(() -> new StreetNotFoundException(id));

        street.setName(name);
        street.setLatitude(latitude);
        street.setLongitude(longitude);

        return this.streetRepository.save(street);
    }
}
