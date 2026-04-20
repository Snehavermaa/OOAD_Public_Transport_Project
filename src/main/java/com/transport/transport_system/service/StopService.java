package com.transport.transport_system.service;

import com.transport.transport_system.model.Stop;
import com.transport.transport_system.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StopService {

    @Autowired
    private StopRepository stopRepository;

    public Stop saveStop(Stop stop) {
        return stopRepository.save(stop);
    }

    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }
}