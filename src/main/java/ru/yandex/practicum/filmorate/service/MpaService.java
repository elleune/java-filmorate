package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDaoImpl;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class MpaService {
    private final MpaDaoImpl mpaDaoimpl;

    @Autowired
    public MpaService(MpaDaoImpl mpaDaoimpl) {
        this.mpaDaoimpl = mpaDaoimpl;
    }

    public Optional<Mpa> getMpaById(long id) {
        if (mpaDaoimpl.getMpaById(id).isEmpty()) {
            throw new DataNotFoundException("Нет такого id");
        }
        return mpaDaoimpl.getMpaById(id);
    }

    public Collection<Mpa> findAll() {
        return mpaDaoimpl.getAllMpa();
    }
}