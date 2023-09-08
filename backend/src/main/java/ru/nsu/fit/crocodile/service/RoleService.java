package ru.nsu.fit.crocodile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.fit.crocodile.exception.ElementAlreadyExistException;
import ru.nsu.fit.crocodile.model.Role;
import ru.nsu.fit.crocodile.repository.RoleRepository;

import javax.management.InstanceAlreadyExistsException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> mapStringToRoles(List<String> list) {
        List<Role> roles = roleRepository.findAllByNameIn(list);
        if (roles.size() != list.size()) throw new IllegalArgumentException("no such role");
        return roles;
    }

    @Transactional
    public Long saveRole(String name) throws ElementAlreadyExistException {
        if (roleRepository.existsByName(name)) {
            throw new ElementAlreadyExistException("Role with such name already exists");
        }
        Role role = new Role();
        role.setName(name);
        roleRepository.save(role);
        return role.getId();
    }
}
