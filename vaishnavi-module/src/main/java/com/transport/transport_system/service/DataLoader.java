package com.transport.transport_system.service;

import com.transport.transport_system.factory.UserFactory;
import com.transport.transport_system.model.Route;
import com.transport.transport_system.model.Schedule;
import com.transport.transport_system.model.User;
import com.transport.transport_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ScheduleService scheduleService;

    @Override
    public void run(String... args) {
        // Load sample users if database is empty
        if (userRepository.findAll().isEmpty()) {
            // Create admin user
            User admin = UserFactory.createAdmin(
                "admin@transport.com", "admin123", "Admin", "User", "9999999999",
                "Management", "ALL_PERMISSIONS"
            );
            userRepository.save(admin);

            // Create conductor user
            User conductor = new com.transport.transport_system.model.ConductorUser(
                "conductor@transport.com", "conductor123", "Conductor", "One", "1111111111"
            );
            userRepository.save(conductor);

            // Create regular users
            User user1 = UserFactory.createUser(
                UserFactory.UserType.REGULAR_USER.getValue(),
                "john@email.com", "john123", "John", "Doe", "9876543210"
            );
            userRepository.save(user1);

            User user2 = UserFactory.createUser(
                UserFactory.UserType.REGULAR_USER.getValue(),
                "jane@email.com", "jane123", "Jane", "Smith", "8765432109"
            );
            userRepository.save(user2);

            User user3 = UserFactory.createRegularUser(
                "bob@email.com", "bob123", "Bob", "Johnson", "7654321098",
                "123 Main St", "New York", "10001"
            );
            userRepository.save(user3);
        }

        if (routeService.getAllRoutes().isEmpty()) {
            Route route1 = new Route();
            route1.setSource("Hyderabad");
            route1.setDestination("Vijayawada");
            route1.setDistance(275.0);
            routeService.saveRoute(route1);

            Route route2 = new Route();
            route2.setSource("Hyderabad");
            route2.setDestination("Bangalore");
            route2.setDistance(570.0);
            routeService.saveRoute(route2);

            Route route3 = new Route();
            route3.setSource("Vijayawada");
            route3.setDestination("Chennai");
            route3.setDistance(430.0);
            routeService.saveRoute(route3);

            Route route4 = new Route();
            route4.setSource("Bangalore");
            route4.setDestination("Chennai");
            route4.setDistance(345.0);
            routeService.saveRoute(route4);

            Route route5 = new Route();
            route5.setSource("Hyderabad");
            route5.setDestination("Pune");
            route5.setDistance(560.0);
            routeService.saveRoute(route5);

            Schedule schedule1 = new Schedule();
            schedule1.setBusId(101L);
            schedule1.setTravelDate("2026-05-01");
            schedule1.setDepartureTime("07:00");
            schedule1.setRoute(route1);
            scheduleService.saveSchedule(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setBusId(102L);
            schedule2.setTravelDate("2026-05-02");
            schedule2.setDepartureTime("09:30");
            schedule2.setRoute(route2);
            scheduleService.saveSchedule(schedule2);

            Schedule schedule3 = new Schedule();
            schedule3.setBusId(103L);
            schedule3.setTravelDate("2026-05-03");
            schedule3.setDepartureTime("08:15");
            schedule3.setRoute(route3);
            scheduleService.saveSchedule(schedule3);

            Schedule schedule4 = new Schedule();
            schedule4.setBusId(104L);
            schedule4.setTravelDate("2026-05-04");
            schedule4.setDepartureTime("10:00");
            schedule4.setRoute(route4);
            scheduleService.saveSchedule(schedule4);

            Schedule schedule5 = new Schedule();
            schedule5.setBusId(105L);
            schedule5.setTravelDate("2026-05-05");
            schedule5.setDepartureTime("06:30");
            schedule5.setRoute(route5);
            scheduleService.saveSchedule(schedule5);

            Schedule schedule6 = new Schedule();
            schedule6.setBusId(106L);
            schedule6.setTravelDate("2026-05-06");
            schedule6.setDepartureTime("13:45");
            schedule6.setRoute(route1);
            scheduleService.saveSchedule(schedule6);

            Schedule schedule7 = new Schedule();
            schedule7.setBusId(107L);
            schedule7.setTravelDate("2026-05-07");
            schedule7.setDepartureTime("16:30");
            schedule7.setRoute(route4);
            scheduleService.saveSchedule(schedule7);
        }
    }
}